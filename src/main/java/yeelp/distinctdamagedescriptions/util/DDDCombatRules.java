package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

/**
 * A class containing all the calculations for damage.
 * @author Yeelp
 *
 */
public final class DDDCombatRules extends CombatRules
{
	private static final Map<Integer, CombatModifiers> modifiers = new HashMap<Integer, CombatModifiers>();
	private static class CombatModifiers
	{
		private boolean slyStrike = false, applyAnvilReductionCap = false, bootsOnly = false, helmetOnly = false;
		private float bruteForce = 0.0f;
		private ItemStack activeShield;
		public void reset()
		{
			slyStrike = false;
			applyAnvilReductionCap = false;
			bootsOnly = false;
			helmetOnly = false;
			activeShield = null;
			bruteForce = 0.0f;
		}
		
		public boolean shouldApplySlyStrike()
		{
			return slyStrike;
		}
		
		public boolean shouldApplyAnvilReductionCap()
		{
			return applyAnvilReductionCap;
		}
		
		public boolean isBootsOnly()
		{
			return bootsOnly;
		}
		
		public boolean isHelmetOnly()
		{
			return helmetOnly;
		}
		
		public ItemStack getActiveShield()
		{
			return activeShield;
		}
		
		public float getBruteForceAmount()
		{
			return bruteForce;
		}
		
		public void setSlyStrike(boolean status)
		{
			slyStrike = status;
		}
		
		public void setAnvilReductionCap(boolean status)
		{
			applyAnvilReductionCap = status;
		}
		
		public void setBootsOnly(boolean status)
		{
			bootsOnly = status;
		}
		
		public void setHelmetOnly(boolean status)
		{
			helmetOnly = status;
		}
		
		public void setActiveShield(ItemStack shield)
		{
			activeShield = shield;
		}
		public void setBruteForceAmount(float amount)
		{
			bruteForce = amount;
		}
	}
	
	public static class CombatResults
	{
		private final boolean immunityResisted, resisted, weakness, effectiveShield;
		private final Map<String, Float> dmgMap, resistMap;
		private final Map<String, Tuple<Float, Float>> armorMap;
		private final IMobResistances mobResists;
		
		CombatResults(boolean weakness, boolean resisted, boolean immunityResisted, boolean effectiveShield, Map<String, Float> dmgMap, Map<String, Float> resistMap, Map<String, Tuple<Float, Float>> armorMap, IMobResistances mobResists)
		{
			this.immunityResisted = immunityResisted;
			this.weakness = weakness;
			this.resisted = resisted;
			this.effectiveShield = effectiveShield;
			this.dmgMap = dmgMap;
			this.resistMap = resistMap;
			this.armorMap = armorMap;
			this.mobResists = mobResists;
		}
		
		public boolean wasImmunityTriggered()
		{
			return immunityResisted;
		}
		
		public boolean wasResistanceHit()
		{
			return resisted;
		}
		
		public boolean wasWeaknessHit()
		{
			return weakness;
		}
		
		public boolean wasShieldEffective()
		{
			return this.effectiveShield;
		}
		
		public Map<String, Float> getDamage()
		{
			return dmgMap;
		}
		
		public Map<String, Float> getResistances()
		{
			return resistMap;
		}
		
		public Map<String, Tuple<Float, Float>> getArmor()
		{
			return armorMap;
		}
		
		public IMobResistances getIMobResistances()
		{
			return mobResists;
		}
	}
	
	
	/**
	 * Set the relevant combat modifiers for this DamageSource
	 * @param src the DamageSource
	 * @param defender the EntityLivingBase being attacked
	 */
	public static void setModifiers(@Nonnull DamageSource src, @Nonnull EntityLivingBase defender)
	{
		Entity attacker = src.getImmediateSource();
		CombatModifiers mods = getModifiers(attacker, defender);
		mods.setBootsOnly(src == DamageSource.FALL);
		mods.setHelmetOnly(src == DamageSource.ANVIL || src == DamageSource.FALLING_BLOCK);
		mods.setAnvilReductionCap(src == DamageSource.ANVIL);
		if(attacker instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase) attacker;
			mods.setBruteForceAmount(0.1f*EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.bruteForce, entity));
			mods.setSlyStrike(EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, entity) > 0);
		}
		if(src instanceof DDDDamageType && canBlockDamage(attacker, defender, (DDDDamageType) src))
		{
			mods.setActiveShield(defender.getActiveItemStack());
		}
		//needed if getModifiers returned default value
		modifiers.put(getKey(attacker == null ? null : attacker.getUniqueID(), defender.getUniqueID()), mods);
	}
	
	/**
	 * Perform only DDD damage calculations and return a container with all the relevant results. Reductions via enchantments and natural armor are NOT done at this stage.
	 * @param attacker attacking Entity. May be null
	 * @param defender defending EntityLivingBase. Never null.
	 * @param dmgMap a map of damage inflicted by category.
	 * @param resistMap a map of damage resistances by category
	 * @param armors a map that maps a damage type to a tuple (a, t), where a is the armor points and t is the toughness points used to reduce damage of that type.
	 * @param mobResists the IMobResistances capability of the defending EntityLivingBase
	 * @return A CombatResults containing the results of the damage calculations.
	 */
	public static CombatResults computeNewDamage(@Nullable Entity attacker, @Nonnull EntityLivingBase defender, Map<String, Float> dmgMap, Map<String, Float> resistMap, Map<String, Tuple<Float, Float>> armors, IMobResistances mobResists)
	{
		NonNullMap<String, Float> absorb = new NonNullMap<String, Float>(0.0f);
		CombatModifiers mods = getModifiers(attacker, defender);
		final double unmoddedDmg = YMath.sum(dmgMap.values());
		dmgMap = computeShieldReductions(dmgMap, DDDAPI.accessor.getShieldDistribution(mods.getActiveShield()));
		double blockedDmg = MathHelper.clamp((unmoddedDmg - YMath.sum(dmgMap.values())), 0.0f, Float.MAX_VALUE);
		double ratio = blockedDmg/unmoddedDmg;
		boolean immunity = false, weakness = false, resist = false, effectiveShield = false;
		if(blockedDmg >= 0.01)
		{
			damageShield(attacker, defender, mods.getActiveShield(), (float) blockedDmg, (float) ratio);
			effectiveShield = true;
			if(Math.abs(unmoddedDmg - blockedDmg) <= 0.001)
			{
				defender.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 0.8f, 0.8f + defender.world.rand.nextFloat() * 0.4f);
				return new CombatResults(false, false, false, true, new NonNullMap<String, Float>(0.0f), resistMap, armors, mobResists);
			}
			else
			{
				//TODO play sound	
			}
		}
		for(Entry<String, Float> entry : dmgMap.entrySet())
		{
			String type = entry.getKey();
			float resistance = resistMap.get(type);
			if(mobResists.hasImmunity(type) && !mods.shouldApplySlyStrike())
			{
				immunity = true;
				dmgMap.put(type, 0.0f);
			}
			else
			{
				if(resistance > 0)
				{
					resistance = MathHelper.clamp(resistance - mods.getBruteForceAmount(), 0.0f, Float.MAX_VALUE);
					resistMap.put(type, resistance);
				}
				float damage = entry.getValue();
				float newDmg = applyDamageCalcs(damage, resistance, armors.get(type), mods.shouldApplyAnvilReductionCap());
				boolean isVeryClose = Math.abs(newDmg - damage) < 0.0000001D;
				if(!resist && newDmg < damage && !isVeryClose)
				{
					resist = true;
				}
				if(!weakness && newDmg > damage && !isVeryClose)
				{
					weakness = true;
				}
				absorb.put(type, damage - newDmg); 
				dmgMap.put(type, newDmg);
			}
		}
		if(damageArmor(defender, resistMap, mods.isBootsOnly(), mods.isHelmetOnly()))
		{
			//refresh armor values
			armors = DDDAPI.accessor.getArmorValuesForEntity(defender, mods.isBootsOnly(), mods.isHelmetOnly());
		}
		return new CombatResults(weakness, resist, immunity, effectiveShield, dmgMap, resistMap, armors, mobResists);
	}
	
	public static void wipeModifiers(@Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		getModifiers(attacker, defender).reset();
	}
	
	public static Map<String, Tuple<Float, Float>> getApplicableArmorValues(@Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		CombatModifiers mods = getModifiers(attacker, defender);
		return DDDAPI.accessor.getArmorValuesForEntity(defender, mods.isBootsOnly(), mods.isHelmetOnly());
	}
	
	private static CombatModifiers getModifiers(@Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		UUID id1 = attacker == null ? null : attacker.getUniqueID();
		UUID id2 = defender.getUniqueID();
		return modifiers.getOrDefault(getKey(id1, id2), new CombatModifiers());
	}
	
	/*
	 * Combine two UUID hashcodes into an int to index into a map.
	 * Because the key will be deleted at the end of calculations (which won't take very long)
	 * it's reasonable to assume the XOR won't give the same key for a different set of UUID's
	 * over the course of that key's lifetime.
	 */
	private static int getKey(UUID attackerID, UUID defenderID)
	{
		if(attackerID == null)
		{
			return defenderID.hashCode();
		}
		return attackerID.hashCode() ^ defenderID.hashCode();
	}
	
	private static Map<String, Float> computeShieldReductions(Map<String, Float> dmgMap, ShieldDistribution shieldDist)
	{
		if(shieldDist == null)
		{
			return dmgMap;
		}
		else
		{
			return shieldDist.block(dmgMap);
		}
	}
	
	/*
	 * Returns true if some armor piece was broken
	 */
	private static boolean damageArmor(@Nonnull EntityLivingBase defender, Map<String, Float> absorbedDamage, boolean bootsOnly, boolean helmetOnly)
	{
		boolean result = false;
		Map<EntityEquipmentSlot, IArmorDistribution> armorMap = DDDAPI.accessor.getArmorDistributionsForEntity(defender);
		for(ItemStack stack : defender.getArmorInventoryList())
		{
			Item item = stack.getItem();
			if(item instanceof ItemArmor)
			{
				ItemArmor armor = (ItemArmor) item;
				if(helmetOnly && armor.armorType != EntityEquipmentSlot.HEAD)
				{
					continue;
				}
				else if(bootsOnly && armor.armorType != EntityEquipmentSlot.FEET)
				{
					continue;
				}
				stack.damageItem(getArmorDamageAmount(absorbedDamage, armorMap.get(armor.armorType)), defender);
				if(stack.isEmpty())
				{
					result = true;
				}
				
			}
		}
		return result;
	}
	
	private static void damageShield(@Nullable Entity attacker, @Nonnull EntityLivingBase defender, ItemStack shield, float blockedDmg, float ratio)
	{
		if(!(shield.getItem() instanceof ItemShield))
		{
			return;
		}
		shield.damageItem(1 + (int)Math.floor(blockedDmg), defender);
		if(shield.isEmpty())
		{
			defender.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + defender.world.rand.nextFloat() * 0.4f);
		}
		if(attacker instanceof EntityLivingBase)
		{
			((EntityLivingBase) attacker).knockBack(defender, 0.4f*ratio + 0.1f, defender.posX - attacker.posX, defender.posZ - attacker.posZ);
		}
	}
	
	private static boolean canBlockDamage(Entity attacker, EntityLivingBase defender, DDDDamageType src)
	{
		if(defender.isActiveItemStackBlocking())
		{
			Vec3d srcVec = src.getDamageLocation();
			if(srcVec != null)
			{
				Vec3d lookVec = defender.getLook(1.0f);
				Vec3d diffVec = srcVec.subtractReverse(new Vec3d(defender.posX, defender.posY, defender.posZ)).normalize();
				diffVec = new Vec3d(diffVec.x, 0.0D, diffVec.z);
				if(diffVec.dotProduct(lookVec) < 0.0D)
				{
					ShieldDistribution dist = DDDAPI.accessor.getShieldDistribution(defender.getActiveItemStack());
					if(dist == null)
					{
						return false;
					}
					Set<String> damageTypes = new HashSet<String>(src.getExtendedTypes());
					if(YMath.setMinus(damageTypes, dist.getCategories()).size() < damageTypes.size()) //true if shield can block at least one damage type.
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static float applyDamageCalcs(float damage, float resistance, Tuple<Float, Float> armors, boolean applyAnvilReductionCap)
	{
		float armor = armors.getFirst(), toughness = armors.getSecond();
		float f = (float) MathHelper.clamp(damage*(1-Math.max(armor/5.0f, armor - damage/(6+toughness/4.0f))/25.0f), 0.0f, applyAnvilReductionCap ? 0.75*damage : Float.MAX_VALUE);
		return f*(1-resistance);
	}
	
	private static int getArmorDamageAmount(Map<String, Float> absorption, IArmorDistribution armorDist)
	{
		float sum = absorption.entrySet().stream().reduce(0.0f, (a, e) -> a + e.getValue()*armorDist.getWeight(e.getKey()), (u, v) -> u + v);
		return (int) MathHelper.clamp(sum, 0, Float.MAX_VALUE) + 1;
	}
}
