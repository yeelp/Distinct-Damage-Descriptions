package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public final class DDDInstantEffectsDist implements DDDPredefinedDistribution {
	private static final Field CLOUD_POTIONS = ObfuscationReflectionHelper.findField(EntityAreaEffectCloud.class, "field_184503_f");

	@Override
	public boolean enabled() {
		return ModConfig.dmg.extraDamage.enablePotionDamage;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.classify(src, target).map(ImmutableSet::of).orElse(ImmutableSet.of());
	}

	@Override
	public String getName() {
		return "instantPotions";
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return this.classify(src, target).map(DDDDamageType::getBaseDistribution);
	}

	private Optional<DDDDamageType> classify(DamageSource source, EntityLivingBase target) {
		DDDDamageType type = null;
		if(this.enabled()) {
			Entity sourceEntity = source.getImmediateSource();
			List<PotionEffect> effects;
			if(sourceEntity instanceof EntityPotion) {
				EntityPotion potion = (EntityPotion) sourceEntity;
				ItemStack stack = potion.getPotion();
				if(stack.getItem() instanceof ItemPotion) {
					effects = PotionUtils.getEffectsFromStack(potion.getPotion());
				}
				else {
					return Optional.ofNullable(type);
				}
			}
			else if(sourceEntity instanceof EntityAreaEffectCloud) {
				EntityAreaEffectCloud cloud = (EntityAreaEffectCloud) sourceEntity;
				effects = getEffectsForCloud(cloud);
			}
			else {
				return Optional.ofNullable(type);
			}
			for(PotionEffect effect : effects) {
				Potion appliedPotion = effect.getPotion();
				if(appliedPotion.isInstant()) {
					switch(target.getCreatureAttribute()) {
						case UNDEAD:
							type = appliedPotion == MobEffects.INSTANT_HEALTH ? DDDBuiltInDamageType.RADIANT : DDDBuiltInDamageType.NORMAL;
						default:
							type = appliedPotion == MobEffects.INSTANT_DAMAGE ? DDDBuiltInDamageType.NECROTIC : DDDBuiltInDamageType.NORMAL;
					}
				}
			}
		}
		return Optional.ofNullable(type);
	}

	@SuppressWarnings("unchecked")
	private static List<PotionEffect> getEffectsForCloud(EntityAreaEffectCloud cloud) {
		try {
			return (List<PotionEffect>) CLOUD_POTIONS.get(cloud);
		}
		catch(IllegalArgumentException | IllegalAccessException e) {
			DistinctDamageDescriptions.err("Could not get potion effects for Area Effect Cloud!");
			return Collections.emptyList();
		}
	}
}
