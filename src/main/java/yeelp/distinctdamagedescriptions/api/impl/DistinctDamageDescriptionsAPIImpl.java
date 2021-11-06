package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.ResistMap;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator {
	INSTANCE;

	private DistinctDamageDescriptionsAPIImpl() {
		DDDAPI.accessor = this;
		DDDAPI.mutator = this;
	}

	/************
	 * ACCESSOR *
	 ************/

	@Override
	@Nullable
	public IDamageDistribution getDamageDistribution(ItemStack stack) {
		return getDDDCap(DamageDistribution.cap, stack);
	}

	@Override
	@Nullable
	public IDamageDistribution getDamageDistribution(EntityLivingBase entity) {
		return getDDDCap(DamageDistribution.cap, entity);
	}

	@Override
	@Nullable
	public IDamageDistribution getDamageDistribution(IProjectile projectile) {
		if(projectile != null && projectile instanceof Entity) {
			return ((Entity) projectile).getCapability(DamageDistribution.cap, null);
		}
		return null;
	}

	@Override
	@Nullable
	public IArmorDistribution getArmorResistances(ItemStack stack) {
		return getDDDCap(ArmorDistribution.cap, stack);
	}

	@Override
	@Nullable
	public IMobResistances getMobResistances(EntityLivingBase entity) {
		return getDDDCap(MobResistances.cap, entity);
	}

	@Override
	@Nullable
	public ICreatureType getMobCreatureType(EntityLivingBase entity) {
		return getDDDCap(CreatureType.cap, entity);
	}

	@Override
	@Nullable
	public ShieldDistribution getShieldDistribution(ItemStack stack) {
		return getDDDCap(ShieldDistribution.cap, stack);
	}

	private static <Cap extends DDDCapabilityBase<? extends NBTBase>> Cap getDDDCap(Capability<Cap> cap, ICapabilityProvider thing) {
		if(thing == null) {
			return null;
		}
		return thing.getCapability(cap, null);
	}

	/***********
	 * MUTATOR *
	 ***********/
	@Override
	public void setPlayerResistances(EntityPlayer player, ResistMap newResists, Set<DDDDamageType> newImmunities, boolean adaptive, float adaptiveAmount) {
		IMobResistances mobResists = getMobResistances(player);
		for(Entry<DDDDamageType, Float> entry : newResists.entrySet()) {
			mobResists.setResistance(entry.getKey(), entry.getValue());
		}
		mobResists.clearImmunities();
		for(DDDDamageType type : newImmunities) {
			mobResists.setImmunity(type, true);
		}
		mobResists.setAdaptiveResistance(adaptive);
		mobResists.setAdaptiveAmount(adaptiveAmount);
		mobResists.sync(player);
	}

	@Override
	public boolean updateAdaptiveResistances(EntityLivingBase entity, DamageMap dmgMap) {
		IMobResistances resists = getMobResistances(entity);
		if(resists.hasAdaptiveResistance()) {
			boolean sync = resists.updateAdaptiveResistance(dmgMap);
			if(entity instanceof EntityPlayer) {
				resists.sync((EntityPlayer) entity);
			}
			return sync;
		}
		return false;
	}
}