package yeelp.distinctdamagedescriptions.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.util.ArmorResistancesProvider;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorResistances;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.MobResistancesProvider;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator
{
	INSTANCE;
	
	private DistinctDamageDescriptionsAPIImpl()
	{
		DDDAPI.accessor = this;
		DDDAPI.mutator = this;
	}
	
	private static final EntityEquipmentSlot[] armorSlots = {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS};
	
	/************
	 * ACCESSOR *
	 ************/
	@Override
	@Nullable
	public IDamageDistribution getDamageDistribution(ItemStack stack)
	{
		return stack.isEmpty() ? null : DamageDistributionProvider.getDamageDistribution(stack);
	}

	@Override
	public IDamageDistribution getDamageDistribution(EntityLivingBase entity)
	{
		return DamageDistributionProvider.getDamageDistribution(entity);
	}

	@Override
	@Nullable
	public IArmorResistances getArmorResistances(ItemStack stack)
	{
		return ArmorResistancesProvider.getArmorResistances(stack);
	}

	@Override
	public IMobResistances getMobResistances(EntityLivingBase entity)
	{
		return MobResistancesProvider.getMobResistances(entity);
	}
	
	@Override
	public Map<EntityEquipmentSlot, IArmorResistances> getArmorResistancesForEntity(EntityLivingBase entity)
	{
		HashMap<EntityEquipmentSlot, IArmorResistances> map = new HashMap<EntityEquipmentSlot, IArmorResistances>();
		for(EntityEquipmentSlot slot : armorSlots)
		{
			map.put(slot, DDDAPI.accessor.getArmorResistances(entity.getItemStackFromSlot(slot)));
		}
		return map;
	}
	
	@Override
	public float[] getResistanceValuesForEntity(EntityLivingBase entity)
	{
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(entity);
		float[] vals = new float[] {mobResists.getSlashingResistance(), mobResists.getPiercingResistance(), mobResists.getBludgeoningResistance(), 0};
		for(IArmorResistances armorResists : getArmorResistancesForEntity(entity).values())
		{
			if(armorResists == null)
			{
				continue;
			}
			vals[0] += armorResists.getSlashingResistance();
			vals[1] += armorResists.getPiercingResistance();
			vals[2] += armorResists.getBludgeoningResistance();
			vals[3] += armorResists.getToughness();
		}
		return vals;
	}
	
	@Override
	public Map<DamageType, Float> classifyDamage(@Nonnull IMobResistances resistances, @Nonnull DamageSource src, float damage)
	{
		HashMap<DamageType, Float> map = new HashMap<DamageType, Float>();
		if(src.getImmediateSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) src.getImmediateSource();
			ItemStack heldItem = attacker.getHeldItemMainhand();
			boolean hasEmptyHand = heldItem.isEmpty();
			DamageCategories damageCat;
			if(!hasEmptyHand)
			{
				IDamageDistribution weaponDist = DDDAPI.accessor.getDamageDistribution(heldItem);
				damageCat = weaponDist.distributeDamage(damage);
			}
			else
			{
				IDamageDistribution mobDist = DDDAPI.accessor.getDamageDistribution(attacker);
				damageCat = mobDist.distributeDamage(damage);
			}
			
			DistinctDamageDescriptions.debug(String.format("Damage Categories: %s", damageCat.toString()));
			float slashing = damageCat.getSlashingDamage();
			float piercing = damageCat.getPiercingDamage();
			float bludgeoning = damageCat.getBludgeoningDamage();
			if(!resistances.isSlashingImmune() || slashing > 0)
			{
				map.put(DamageType.SLASHING, slashing);
			}
			if(!resistances.isPiercingImmune() || piercing > 0)
			{
				map.put(DamageType.PIERCING, piercing);
			}
			if(!resistances.isBludgeoningImmune() || bludgeoning > 0)
			{
				map.put(DamageType.BLUDGEONING, bludgeoning);
			}
		}
		return map;
	}
}
