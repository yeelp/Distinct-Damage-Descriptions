package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.api.impl.AbstractDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class DDDResistancesModifierRegistry extends DDDModifierRegistry<EntityLivingBase, IMobResistances, IDDDResistanceModifier> {

	protected DDDResistancesModifierRegistry() {
		super(() -> 0.0f, "Mob Resistances");
	}

	@Override
	public void applyModifiers(EntityLivingBase provider, IMobResistances cap) {
		ResistMap resists = cap.getAllResistancesCopy();
		Set<DDDDamageType> immunitiesToAdd = Sets.newHashSet(), immunitiesToRemove = Sets.newHashSet();
		float adaptiveAmount = 0.0f;
		Optional<Boolean> adaptability = Optional.empty();
		for(IDDDResistanceModifier modifier : this.poolApplicableModifiers(provider)) {
			if(modifier.shouldReallocate()) {
				float weightToSubtract  = (float) (YMath.sum(modifier.values()) / modifier.size());
				resists.replaceAll((type, resist) -> resist - weightToSubtract);
			}
			modifier.forEach((type, resist) -> resists.merge(type, resist, Float::sum));
			immunitiesToAdd.addAll(modifier.getImmunitiesToAdd());
			immunitiesToRemove.removeAll(modifier.getImmunitiesToAdd());
			immunitiesToRemove.addAll(modifier.getImmunitiesToRemove());
			immunitiesToAdd.removeAll(modifier.getImmunitiesToRemove());
			adaptiveAmount += modifier.getAdaptabilityMod();
			Optional<Boolean> newStatus = modifier.toggleAdaptability();
			if(newStatus.isPresent()) {
				adaptability = newStatus;
			}
		}
		resists.forEach((type, resist) -> {
			if(resist == 0.0f) {
				cap.removeResistance(type);
			}
			else {
				cap.setResistance(type, resist);				
			}
		});
		Iterator<Set<DDDDamageType>> immunitiesSets = Lists.newArrayList(immunitiesToAdd, immunitiesToRemove).iterator();
		Iterator<Boolean> status = Lists.newArrayList(true, false).iterator();
		while(status.hasNext()) {
			Set<DDDDamageType> immunities = immunitiesSets.next();
			boolean b = status.next();
			immunities.forEach((type) -> {
				cap.setImmunity(type, b);
			});
		}
		if(adaptiveAmount != 0) {
			cap.setAdaptiveAmount(cap.getAdaptiveAmount() + adaptiveAmount);
		}
		adaptability.ifPresent((b) -> {
			cap.setAdaptiveResistance(b, false);
		});
	}
	
	private final class PooledResistanceModifier extends AbstractDDDResistanceModifier {

		protected PooledResistanceModifier(IDDDResistanceModifier mod, boolean shouldReallocate) {
			super(DDDResistancesModifierRegistry.this.defaultVal, null, shouldReallocate, mod.getAdaptabilityMod());
			this.setAdaptability(mod.toggleAdaptability().orElse(null));
			mod.getImmunitiesToAdd().forEach(this::addImmunity);
			mod.getImmunitiesToRemove().forEach(this::removeImmunity);
			this.putAll(mod);
		}

		@Override
		public boolean applicable(EntityLivingBase t) {
			return false;
		}

		@Override
		public String getName() {
			return null;
		}
		
		PooledResistanceModifier combine(IDDDResistanceModifier other) {
			this.setAdaptabilityMod(this.getAdaptabilityMod() + other.getAdaptabilityMod());
			Boolean s1 = this.toggleAdaptability().orElse(null);
			Boolean s2 = other.toggleAdaptability().orElse(s1);
			if(s1 == null) {
				this.setAdaptability(s2);
			}
			else {
				this.setAdaptability(s1 || s2);
			}
			other.getImmunitiesToAdd().forEach(this::addImmunity);
			other.getImmunitiesToRemove().forEach(this::removeImmunity);
			other.forEach((type, resist) -> this.merge(type, resist, Float::sum));
			return this;
		}
	}

	@Override
	protected IDDDResistanceModifier getNewModifierAccumulator(IDDDResistanceModifier modifier) {
		return new PooledResistanceModifier(modifier, modifier.shouldReallocate());
	}

	@Override
	protected IDDDResistanceModifier combine(IDDDResistanceModifier m1, IDDDResistanceModifier m2) {
		if(m1 instanceof DDDResistancesModifierRegistry.PooledResistanceModifier) {
			return ((PooledResistanceModifier) m1).combine(m2);
		}
		return (new PooledResistanceModifier(m1, m1.shouldReallocate())).combine(m2);
	}
}
