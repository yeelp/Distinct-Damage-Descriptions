package yeelp.distinctdamagedescriptions.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorDistributionProvider;
import yeelp.distinctdamagedescriptions.util.CreatureTypeProvider;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ICreatureType;
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
	public IDamageDistribution getDamageDistribution(IProjectile projectile)
	{
		return DamageDistributionProvider.getDamageDistribution(projectile);
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
	public ICreatureType getMobCreatureType(EntityLivingBase entity)
	{
		return CreatureTypeProvider.getCreatureType(entity);
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
	public Map<DamageType, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity, boolean bootsOnly, boolean helmetOnly)
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
			else if(bootsOnly && slot != EntityEquipmentSlot.FEET)
			{
				continue;
			}
			else if(helmetOnly && slot != EntityEquipmentSlot.HEAD)
			{
				continue;
			}
			if(stack.getItem() instanceof ItemArmor)
			{
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
		}
		map.put(DamageType.SLASHING, new Tuple<Float, Float>(slashArmor, slashTough));
		map.put(DamageType.PIERCING, new Tuple<Float, Float>(pierceArmor, pierceTough));
		map.put(DamageType.BLUDGEONING, new Tuple<Float, Float>(bludgeArmor, bludgeTough));
		return map;
	}
	
	@Override
	@Nullable
	public Map<DamageType, Float> classifyDamage(@Nonnull IMobResistances resistances, @Nonnull DamageSource src, float damage)
	{
		HashMap<DamageType, Float> map = new HashMap<DamageType, Float>();
		boolean slyStrike = false;
		DamageCategories damageCat = null;
		if(src.getImmediateSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) src.getImmediateSource();
			ItemStack heldItem = attacker.getHeldItemMainhand();
			boolean hasEmptyHand = heldItem.isEmpty();
			if(!hasEmptyHand)
			{
				IDamageDistribution weaponDist = getDamageDistribution(heldItem);
				slyStrike = EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, attacker) > 0;
				damageCat = weaponDist.distributeDamage(damage);
			}
			else
			{
				IDamageDistribution mobDist = getDamageDistribution(attacker);
				damageCat = mobDist.distributeDamage(damage);
			}
		}
		else if(isValidProjectile(src))
		{
			IProjectile projectile = (IProjectile) src.getImmediateSource();
			IDamageDistribution dist = getDamageDistribution(projectile);
			damageCat = dist.distributeDamage(damage);
		}
		else
		{
			if(src.isExplosion() && ModConfig.dmg.extraDamage.enableExplosionDamage)
			{
				damageCat = DamageDistribution.BLUDGEONING_DISTRIBUTION.distributeDamage(damage);
			}
			else if(src == DamageSource.CACTUS && ModConfig.dmg.extraDamage.enableCactusDamage)
			{
				damageCat = DamageDistribution.PIERCING_DISTRIBUTION.distributeDamage(damage);
			}
			else if(src == DamageSource.ANVIL && ModConfig.dmg.extraDamage.enableAnvilDamage)
			{
				damageCat = DamageDistribution.BLUDGEONING_DISTRIBUTION.distributeDamage(damage);
			}
			else if(src == DamageSource.FALLING_BLOCK && ModConfig.dmg.extraDamage.enableFallingBlockDamage)
			{
				damageCat = DamageDistribution.BLUDGEONING_DISTRIBUTION.distributeDamage(damage);
			}
			else if(src == DamageSource.FALL && ModConfig.dmg.extraDamage.enableFallDamage)
			{
				damageCat = DamageDistribution.BLUDGEONING_DISTRIBUTION.distributeDamage(damage);
			}
			else if(src == DamageSource.FLY_INTO_WALL && ModConfig.dmg.extraDamage.enableFlyIntoWallDamage)
			{
				damageCat = DamageDistribution.BLUDGEONING_DISTRIBUTION.distributeDamage(damage);
			}
			else
			{
				return null;
			}
		}
		DistinctDamageDescriptions.debug(String.format("Damage Categories: %s", damageCat.toString()));
		float slashing = damageCat.getSlashingDamage();
		float piercing = damageCat.getPiercingDamage();
		float bludgeoning = damageCat.getBludgeoningDamage();
		if(slashing > 0)
		{
			map.put(DamageType.SLASHING, resistances.isSlashingImmune() && !slyStrike ? -1 : slashing);
		}
		if(piercing > 0)
		{
			map.put(DamageType.PIERCING, resistances.isPiercingImmune() && !slyStrike ? -1 : piercing);
		}
		if(bludgeoning > 0)
		{
			map.put(DamageType.BLUDGEONING, resistances.isBludgeoningImmune() && !slyStrike ? -1 : bludgeoning);
		}
		return map;
	}
	
	private static boolean isValidProjectile(DamageSource src)
	{
		Entity entityIn = src.getImmediateSource();
		if(entityIn instanceof IProjectile)
		{
			return DDDRegistries.projectileProperties.isProjectileRegistered(entityIn) || !src.isMagicDamage();
		}
		else
		{
			return false;
		}
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
		mobResists.setAdaptiveResistance(adaptive);
		CapabilityHandler.syncResistances(player);
	}
}
