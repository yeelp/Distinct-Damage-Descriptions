package yeelp.distinctdamagedescriptions.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.DDDDamageSource;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.ResistMap;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator {
	INSTANCE;

	private static final EntityEquipmentSlot[] armorSlots = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};

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
		return stack.isEmpty() ? null : stack.getCapability(DamageDistribution.cap, null);
	}

	@Override
	public IDamageDistribution getDamageDistribution(EntityLivingBase entity) {
		return entity.getCapability(DamageDistribution.cap, null);
	}

	@Override
	public IDamageDistribution getDamageDistribution(IProjectile projectile) {
		if(projectile instanceof Entity) {
			return ((Entity) projectile).getCapability(DamageDistribution.cap, null);
		}
		return null;
	}

	@Override
	@Nullable
	public IArmorDistribution getArmorResistances(ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ItemArmor)) {
			return null;
		}
		return stack.getCapability(ArmorDistribution.cap, null);
	}

	@Override
	public IMobResistances getMobResistances(EntityLivingBase entity) {
		return entity.getCapability(MobResistances.cap, null);
	}

	@Override
	public ICreatureType getMobCreatureType(EntityLivingBase entity) {
		return entity.getCapability(CreatureType.cap, null);
	}

	@Override
	@Nullable
	public ShieldDistribution getShieldDistribution(ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ItemShield)) {
			return null;
		}
		return stack.getCapability(ShieldDistribution.cap, null);
	}

	@Override
	public Map<EntityEquipmentSlot, IArmorDistribution> getArmorDistributionsForEntity(EntityLivingBase entity) {
		HashMap<EntityEquipmentSlot, IArmorDistribution> map = new HashMap<EntityEquipmentSlot, IArmorDistribution>();
		for(EntityEquipmentSlot slot : armorSlots) {
			map.put(slot, DDDAPI.accessor.getArmorResistances(entity.getItemStackFromSlot(slot)));
		}
		return map;
	}

	@Override
	public ArmorMap getArmorValuesForEntity(EntityLivingBase entity, Iterable<EntityEquipmentSlot> slots) {
		ArmorMap map = new ArmorMap();
		for(EntityEquipmentSlot slot : slots) {
			ItemStack stack = entity.getItemStackFromSlot(slot);
			if(stack.isEmpty()) {
				continue;
			}
			if(stack.getItem() instanceof ItemArmor) {
				ItemArmor armorItem = (ItemArmor) stack.getItem();
				IArmorDistribution armorResists = getArmorResistances(stack);
				// merge this distribution with all previous armor distribution maps
				armorResists.distributeArmor(armorItem.damageReduceAmount, armorItem.toughness).forEach((k, v) -> map.merge(k, v, ArmorValues::merge));
			}
		}
		return map;
	}

	@Override
	public Optional<IDamageDistribution> classifyDamage(@Nonnull DamageSource src, EntityLivingBase target) {
		IDamageDistribution dist = DDDRegistries.distributions.getDamageDistribution(src, target);
		if(dist.getWeight(DDDBuiltInDamageType.NORMAL) == 1) {
			Entity sourceEntity = src.getImmediateSource();
			if(sourceEntity == null && src.getTrueSource() instanceof EntityPlayer) {
				dist = getDistForLivingEntity((EntityLivingBase) src.getTrueSource());
			}
			else if(sourceEntity instanceof EntityLivingBase) {
				dist = getDistForLivingEntity((EntityLivingBase) sourceEntity);
			}
			else if(isValidProjectile(src)) {
				IProjectile projectile = (IProjectile) src.getImmediateSource();
				dist = getDamageDistribution(projectile);
			}
		}
		return Optional.ofNullable(dist);
	}

	@Override
	public ResistMap classifyResistances(Set<DDDDamageType> types, IMobResistances resists) {
		ResistMap map = new ResistMap();
		if(types.isEmpty() || resists == null) {
			return map;
		}
		for(DDDDamageType s : types) {
			map.put(s, resists.getResistance(s));
		}
		return map;
	}

	@Override
	public boolean isPhysicalDamageOnly(DDDDamageSource src) {
		for(DDDDamageType type : src.getExtendedTypes()) {
			if(isPhysicalDamage(type)) {
				continue;
			}
			return false;
		}
		return true;
	}

	private static boolean isValidProjectile(DamageSource src) {
		if(src.getImmediateSource() == null) {
			return false;
		}
		return src.getImmediateSource() instanceof IProjectile;
	}

	private IDamageDistribution getDistForLivingEntity(EntityLivingBase attacker) {
		ItemStack heldItem = attacker.getHeldItemMainhand();
		boolean hasEmptyHand = heldItem.isEmpty();
		if(!hasEmptyHand) {
			return getDamageDistribution(heldItem);
		}
		return getDamageDistribution(attacker);
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