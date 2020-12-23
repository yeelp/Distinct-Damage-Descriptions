package yeelp.distinctdamagedescriptions.api.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorCategories;
import yeelp.distinctdamagedescriptions.util.ArmorDistributionProvider;
import yeelp.distinctdamagedescriptions.util.CreatureTypeProvider;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ICreatureType;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.MobResistancesProvider;
import yeelp.distinctdamagedescriptions.util.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.ShieldDistributionProvider;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator
{
	INSTANCE;
	
	private static final EntityEquipmentSlot[] armorSlots = {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS};
	private final Map<DamageSource, Tuple<Supplier<Boolean>, IDamageDistribution>> extraDists = new HashMap<DamageSource, Tuple<Supplier<Boolean>, IDamageDistribution>>();
	
	private DistinctDamageDescriptionsAPIImpl()
	{
		DDDAPI.accessor = this;
		DDDAPI.mutator = this;
		/* setting up extraDists:
		 * All we need to do is make a supplier that checks the corresponding config value 
		 * and associate that supplier with the DamageDistribution used (in a Tuple).
		 * When the DamageSource maps to the Tuple, we can get the value from the Supplier in the Tuple,
		 * returning the DamageDistribution on a pass, and null on a fail.
		 * 
		 * Using a Supplier to ensure the actual boolean value from the config is checked, 
		 * instead of a locally wrapped Boolean copy, which may not update if the config is changed.
		 * We want these config values not not require a restart (because it's not needed), so Supplier seems the best way to go.
		 */
		extraDists.put(DamageSource.ANVIL, new Tuple<Supplier<Boolean>, IDamageDistribution>(() -> ModConfig.dmg.extraDamage.enableFallDamage, DamageDistribution.BLUDGEONING_DISTRIBUTION));
		extraDists.put(DamageSource.CACTUS, new Tuple<Supplier<Boolean>, IDamageDistribution>(() -> ModConfig.dmg.extraDamage.enableCactusDamage, DamageDistribution.PIERCING_DISTRIBUTION));
		extraDists.put(DamageSource.FALL, new Tuple<Supplier<Boolean>, IDamageDistribution>(() -> ModConfig.dmg.extraDamage.enableFallDamage, DamageDistribution.BLUDGEONING_DISTRIBUTION));
		extraDists.put(DamageSource.FALLING_BLOCK, new Tuple<Supplier<Boolean>, IDamageDistribution>(() -> ModConfig.dmg.extraDamage.enableFallingBlockDamage, DamageDistribution.BLUDGEONING_DISTRIBUTION));
		extraDists.put(DamageSource.FLY_INTO_WALL, new Tuple<Supplier<Boolean>, IDamageDistribution>(() -> ModConfig.dmg.extraDamage.enableFlyIntoWallDamage, DamageDistribution.BLUDGEONING_DISTRIBUTION));
	}
	
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
		if(stack == null || !(stack.getItem() instanceof ItemArmor))
		{
			return null;
		}
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
	@Nullable
	public ShieldDistribution getShieldDistribution(ItemStack stack)
	{
		if(stack == null || !(stack.getItem() instanceof ItemShield))
		{
			return null;
		}
		return ShieldDistributionProvider.getShieldDistribution(stack);
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
	public Map<String, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity, boolean bootsOnly, boolean helmetOnly)
	{
		NonNullMap<String, Tuple<Float, Float>> map = new NonNullMap<String, Tuple<Float, Float>>(new Tuple<Float, Float>(0.0f, 0.0f));
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
				ArmorCategories cats = armorResists.distributeArmor(armor, toughness);
				for(Tuple<String, Float> t : cats.getNonZeroArmorValues())
				{
					map.compute(t.getFirst(), (s, v) -> new Tuple<Float, Float>(v.getFirst() + t.getSecond(), v.getSecond() + cats.getToughness(s)));
				}
			}
		}
		return map;
	}
	
	@Override
	@Nullable
	public Map<String, Float> classifyDamage(@Nonnull DamageSource src, float damage)
	{
		Map<String, Float> map = new NonNullMap<String, Float>(0.0f);
		boolean slyStrike = false;
		IDamageDistribution dist;
		if(src.getImmediateSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) src.getImmediateSource();
			ItemStack heldItem = attacker.getHeldItemMainhand();
			boolean hasEmptyHand = heldItem.isEmpty();
			if(!hasEmptyHand)
			{
				dist = getDamageDistribution(heldItem);
				slyStrike = EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, attacker) > 0;
			}
			else
			{
				dist = getDamageDistribution(attacker);
			}
		}
		else if(isValidProjectile(src))
		{
			IProjectile projectile = (IProjectile) src.getImmediateSource();
			dist = getDamageDistribution(projectile);
		}
		else
		{
			dist = getExtraDamageDistribution(src);
			if(dist == null)
			{
				Set<String> types = DDDRegistries.damageTypes.getCustomDamageContext(src);
				if(types.size() == 0)
				{
					return null;
				}
				else
				{
					NonNullMap<String, Float> custMap = new NonNullMap<String, Float>(0.0f);
					float weight = 1.0f/types.size();
					for(String s : types)
					{
						custMap.put(s, weight);
					}
					dist = new DamageDistribution(custMap);
				}
			}
		}
		map = dist.distributeDamage(damage);
		return map;
	}
	
	@Override
	public Map<String, Float> classifyResistances(Set<String> types, IMobResistances resists)
	{
		NonNullMap<String, Float> map = new NonNullMap<String, Float>(0.0f);
		for(String s : types)
		{
			map.put(s, resists.getResistance(s));
		}
		return map;
	}
	
	@Override
	public DamageSource getDamageContext(DamageSource src)
	{
		Entity entity = src.getImmediateSource();
		String type = src.getDamageType();
		Set<String> types = new HashSet<String>();
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase livingEntity = (EntityLivingBase) entity;
			ItemStack stack = livingEntity.getHeldItemMainhand();
			if(!stack.isEmpty())
			{
				types.addAll(getDamageDistribution(stack).getCategories());
			}
			else
			{
				types.addAll(getDamageDistribution(livingEntity).getCategories());
			}
		}
		else if(isValidProjectile(src))
		{
			types.addAll(getDamageDistribution((IProjectile) src.getImmediateSource()).getCategories());
		}
		else
		{
			IDamageDistribution dist = getExtraDamageDistribution(src);
			if(dist != null)
			{
				types.addAll(dist.getCategories());
			}
		}
		types.addAll(DDDRegistries.damageTypes.getCustomDamageContext(src));
		
		if(!ModConfig.dmg.useCustomDamageTypes)
		{
			types.removeIf(s -> s.startsWith("ddd_"));
		}
		if(types.size() == 0)
		{
			return src;
		}
		else
		{
			return new DDDDamageType(src, types.toArray(new String[] {}));
		}
	}
	
	@Override
	public boolean isPhysicalDamageOnly(DDDDamageType src)
	{
		for(String s : src.getExtendedTypes())
		{
			if(isPhysicalDamage(s))
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
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
	
	@Nullable
	private IDamageDistribution getExtraDamageDistribution(DamageSource src)
	{
		if(src.isExplosion() && ModConfig.dmg.extraDamage.enableExplosionDamage)
		{
			return DamageDistribution.BLUDGEONING_DISTRIBUTION;
		}
		else if(extraDists.containsKey(src))
		{
			Tuple<Supplier<Boolean>, IDamageDistribution> t = extraDists.get(src);
			return t.getFirst().get() ? t.getSecond() : null;
		}
		else
		{
			return null;
		}
	}
	
	/***********
	 * MUTATOR *
	 ***********/
	@Override
	public void setPlayerResistances(EntityPlayer player, Map<String, Float> newResists, Set<String> newImmunities, boolean adaptive, float adaptiveAmount)
	{
		IMobResistances mobResists = getMobResistances(player);
		for(Entry<String, Float> entry : newResists.entrySet())
		{
			mobResists.setResistance(entry.getKey(), entry.getValue());
		}
		mobResists.clearImmunities();
		for(String s : newImmunities)
		{
			mobResists.setImmunity(s, true);
		}
		mobResists.setAdaptiveResistance(adaptive);
		mobResists.setAdaptiveAmount(adaptiveAmount);
		CapabilityHandler.syncResistances(player);
	}
}
