package yeelp.distinctdamagedescriptions.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.util.ArmorDistributionProvider;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
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
	public IArmorDistribution getArmorResistances(ItemStack stack)
	{
		return ArmorDistributionProvider.getArmorResistances(stack);
	}

	@Override
	public IMobResistances getMobResistances(EntityLivingBase entity)
	{
		return MobResistancesProvider.getMobResistances(entity);
	}
	
	@Override
	public Map<EntityEquipmentSlot, IArmorDistribution> getArmorDistributionsForEntity(EntityLivingBase entity)
	{
		HashMap<EntityEquipmentSlot, IArmorDistribution> map = new HashMap<EntityEquipmentSlot, IArmorDistribution>();
		for(EntityEquipmentSlot slot : armorSlots)
		{
			map.put(slot, DDDAPI.accessor.getArmorResistances(entity.getItemStackFromSlot(slot)));
		}
		return map;
	}
	
	@Override
	public Map<DamageType, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity)
	{
		HashMap<DamageType, Tuple<Float, Float>> map = new HashMap<DamageType, Tuple<Float, Float>>();
		float slashArmor = 0, pierceArmor = 0, bludgeArmor = 0, slashTough = 0, pierceTough = 0, bludgeTough = 0;
		for(EntityEquipmentSlot slot : armorSlots)
		{
			ItemStack stack = entity.getItemStackFromSlot(slot);
			if(stack.isEmpty())
			{
				continue;
			}
			ItemArmor armorItem = (ItemArmor) stack.getItem();
			IArmorDistribution armorResists = getArmorResistances(stack);
			float armor = armorItem.damageReduceAmount;
			float toughness = armorItem.toughness;
			float slashWeight = armorResists.getSlashingWeight();
			float pierceWeight = armorResists.getPiercingWeight();
			float bludgeWeight = armorResists.getBludgeoningWeight();
			slashArmor += armor*slashWeight;
			pierceArmor += armor*pierceWeight;
			bludgeArmor += armor*bludgeWeight;
			slashTough += toughness*slashWeight;
			pierceTough += toughness*pierceWeight;
			bludgeTough += toughness*bludgeWeight;
		}
		map.put(DamageType.SLASHING, new Tuple<Float, Float>(slashArmor, slashTough));
		map.put(DamageType.PIERCING, new Tuple<Float, Float>(pierceArmor, pierceTough));
		map.put(DamageType.BLUDGEONING, new Tuple<Float, Float>(bludgeArmor, bludgeTough));
		return map;
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
			if(!resistances.isSlashingImmune() && slashing > 0)
			{
				map.put(DamageType.SLASHING, slashing);
			}
			if(!resistances.isPiercingImmune() && piercing > 0)
			{
				map.put(DamageType.PIERCING, piercing);
			}
			if(!resistances.isBludgeoningImmune() && bludgeoning > 0)
			{
				map.put(DamageType.BLUDGEONING, bludgeoning);
			}
		}
		return map;
	}
	
	/***********
	 * MUTATOR *
	 ***********/
	@Override
	public void setPlayerResistances(EntityPlayer player, float slash, float pierce, float bludge, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, boolean adaptive)
	{
		IMobResistances mobResists = getMobResistances(player);
		mobResists.setSlashingResistance(slash);
		mobResists.setPiercingResistance(pierce);
		mobResists.setBludgeoningResistance(bludge);
		mobResists.setSlashingImmunity(slashImmune);
		mobResists.setPiercingImmunity(pierceImmune);
		mobResists.setBludgeoningImmunity(bludgeImmune);
		mobResists.setAdaptiveImmunity(adaptive);
		CapabilityHandler.syncResistances(player);
	}
}
