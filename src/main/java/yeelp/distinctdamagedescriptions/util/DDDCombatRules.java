package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

/**
 * A class containing all the calculations for damage.
 * 
 * @author Yeelp
 *
 */
public final class DDDCombatRules {
	private static final Map<Integer, CombatModifiers> modifiers = new HashMap<Integer, CombatModifiers>();
	private static Map<UUID, HitInfo> lastHit = new HashMap<UUID, HitInfo>();

	private static final class AbsorptionMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 1295880082452814495L;

		public AbsorptionMap() {
			super(0.0f);
		}
	}

	/**
	 * This container stores info about the last hit, used for getting death
	 * messages
	 * 
	 * @author Yeelp
	 *
	 */
	public static class HitInfo {
		private Optional<IDamageDistribution> dist;
		private DamageSource src;

		HitInfo(Optional<IDamageDistribution> dist, DamageSource newSrc) {
			this.dist = dist;
			this.src = newSrc;
		}

		public Optional<IDamageDistribution> getLastDist() {
			return this.dist;
		}

		public DamageSource getLastSource() {
			return this.src;
		}
	}

	/**
	 * This container stores all modifiers for that hit.
	 * 
	 * @author Yeelp
	 *
	 */
	private static class CombatModifiers {
		private boolean slyStrike = false, applyAnvilReductionCap = false, bootsOnly = false, helmetOnly = false,
				bypassArmor = false;
		private float bruteForce = 0.0f, hungerDamage = 0.0f;
		private ItemStack activeShield;

		public CombatModifiers() {
		}

		public void reset() {
			this.slyStrike = false;
			this.applyAnvilReductionCap = false;
			this.bootsOnly = false;
			this.helmetOnly = false;
			this.bypassArmor = false;
			this.activeShield = null;
			this.bruteForce = 0.0f;
			this.hungerDamage = 0.0f;
		}

		public boolean shouldApplySlyStrike() {
			return this.slyStrike;
		}

		public boolean shouldApplyAnvilReductionCap() {
			return this.applyAnvilReductionCap;
		}

		public boolean isBootsOnly() {
			return this.bootsOnly;
		}

		public boolean isHelmetOnly() {
			return this.helmetOnly;
		}

		public ItemStack getActiveShield() {
			return this.activeShield;
		}

		public float getBruteForceAmount() {
			return this.bruteForce;
		}

		public boolean shouldBypassArmor() {
			return this.bypassArmor;
		}

		@SuppressWarnings("unused")
		public float getHungerDamage() {
			return this.hungerDamage;
		}

		public void setSlyStrike(boolean status) {
			this.slyStrike = status;
		}

		public void setAnvilReductionCap(boolean status) {
			this.applyAnvilReductionCap = status;
		}

		public void setBootsOnly(boolean status) {
			this.bootsOnly = status;
		}

		public void setHelmetOnly(boolean status) {
			this.helmetOnly = status;
		}

		public void setActiveShield(ItemStack shield) {
			this.activeShield = shield;
		}

		public void setBruteForceAmount(float amount) {
			this.bruteForce = amount;
		}

		public void setBypassArmor(boolean bypassesArmor) {
			this.bypassArmor = bypassesArmor;
		}

		public void setHungerDamage(float amount) {
			this.hungerDamage = amount;
		}

		public Iterable<EntityEquipmentSlot> getApplicableArmorSlots() {
			HashSet<EntityEquipmentSlot> set = new HashSet<EntityEquipmentSlot>(4);
			if(this.bootsOnly) {
				set.add(EntityEquipmentSlot.FEET);
			}
			if(this.helmetOnly) {
				set.add(EntityEquipmentSlot.HEAD);
			}
			else if(!(this.bootsOnly || this.helmetOnly)) {
				set.add(EntityEquipmentSlot.FEET);
				set.add(EntityEquipmentSlot.HEAD);
				set.add(EntityEquipmentSlot.LEGS);
				set.add(EntityEquipmentSlot.CHEST);
			}
			return set;
		}
	}

	/**
	 * This container stores all the results after damage calculations
	 * 
	 * @author Yeelp
	 *
	 */
	public static class CombatResults {
		private final boolean immunityResisted, resisted, weakness, effectiveShield;
		private final DamageMap dmgMap;
		private final ResistMap resistMap;
		private final ArmorMap armorMap;
		private final IMobResistances mobResists;

		CombatResults(boolean weakness, boolean resisted, boolean immunityResisted, boolean effectiveShield, DamageMap dmgMap, ResistMap resistMap, ArmorMap armorMap, IMobResistances mobResists) {
			this.immunityResisted = immunityResisted;
			this.weakness = weakness;
			this.resisted = resisted;
			this.effectiveShield = effectiveShield;
			this.dmgMap = dmgMap;
			this.resistMap = resistMap;
			this.armorMap = armorMap;
			this.mobResists = mobResists;
		}

		public boolean wasImmunityTriggered() {
			return this.immunityResisted;
		}

		public boolean wasResistanceHit() {
			return this.resisted;
		}

		public boolean wasWeaknessHit() {
			return this.weakness;
		}

		public boolean wasShieldEffective() {
			return this.effectiveShield;
		}

		public DamageMap getDamage() {
			return this.dmgMap;
		}

		public ResistMap getResistances() {
			return this.resistMap;
		}

		public ArmorMap getArmor() {
			return this.armorMap;
		}

		public IMobResistances getIMobResistances() {
			return this.mobResists;
		}
	}

	/**
	 * Set the relevant combat modifiers for this DamageSource
	 * 
	 * @param src      the DamageSource
	 * @param defender the EntityLivingBase being attacked
	 */

	public static void setModifiers(@Nonnull DamageSource src, @Nonnull EntityLivingBase defender) {
		Entity attacker = src.getImmediateSource();
		CombatModifiers mods = getModifiers(attacker, defender);
		mods.setBootsOnly(src == DamageSource.FALL);
		mods.setHelmetOnly(src == DamageSource.ANVIL || src == DamageSource.FALLING_BLOCK);
		mods.setAnvilReductionCap(src == DamageSource.ANVIL);
		mods.setBypassArmor(src.isUnblockable());
		mods.setHungerDamage(src.getHungerDamage());
		if(attacker instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) attacker;
			mods.setBruteForceAmount(0.1f * EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.bruteForce, entity));
			mods.setSlyStrike(EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, entity) > 0);
		}
		if(src instanceof DDDDamageSource && canBlockDamage(defender, (DDDDamageSource) src)) {
			mods.setActiveShield(defender.getActiveItemStack());
		}
		// needed if getModifiers returned default value
		modifiers.put(getKey(attacker == null ? null : attacker.getUniqueID(), defender.getUniqueID()), mods);
	}

	public static HitInfo setLastHit(@Nonnull DamageSource src, @Nonnull EntityLivingBase defender) {
		Optional<IDamageDistribution> oDist = DDDAPI.accessor.classifyDamage(src, defender);
		DamageSource newSrc = oDist.isPresent() ? new DDDDamageSource(src, oDist.get().getCategories()) : src;
		HitInfo info = new HitInfo(oDist, newSrc);
		lastHit.put(defender.getUniqueID(), info);
		return info;
	}

	/**
	 * Perform only DDD damage calculations and return a container with all the
	 * relevant results. Reductions via enchantments and natural armor are NOT done
	 * at this stage.
	 * 
	 * @param attacker   attacking Entity. May be null
	 * @param defender   defending EntityLivingBase. Never null.
	 * @param dmgMap     a map of damage inflicted by category.
	 * @param resistMap  a map of damage resistances by category
	 * @param armors     a map that maps a damage type to a tuple (a, t), where a is
	 *                   the armor points and t is the toughness points used to
	 *                   reduce damage of that type.
	 * @param mobResists the IMobResistances capability of the defending
	 *                   EntityLivingBase
	 * @return A CombatResults containing the results of the damage calculations.
	 */

	public static CombatResults computeNewDamage(@Nullable Entity attacker, @Nonnull EntityLivingBase defender, DamageMap dmgMap, ResistMap resistMap, ArmorMap armors, IMobResistances mobResists) {
		AbsorptionMap absorb = new AbsorptionMap();
		CombatModifiers mods = getModifiers(attacker, defender);
		final double unmoddedDmg = YMath.sum(dmgMap.values());
		dmgMap = computeShieldReductions(dmgMap, DDDAPI.accessor.getShieldDistribution(mods.getActiveShield()));
		double blockedDmg = MathHelper.clamp((unmoddedDmg - YMath.sum(dmgMap.values())), 0.0f, Float.MAX_VALUE);
		double ratio = blockedDmg / unmoddedDmg;
		boolean immunity = false, weakness = false, resist = false, effectiveShield = false;
		if(blockedDmg >= 0.01) {
			boolean broken = damageShield(attacker, defender, mods.getActiveShield(), (float) blockedDmg, (float) ratio);
			effectiveShield = true;
			if(defender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) defender;
				Random rand = defender.world.rand;
				if(Math.abs(unmoddedDmg - blockedDmg) <= 0.001) {
					if(!broken) {
						player.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.8f + rand.nextFloat() * 0.4f);
						DDDSounds.playSound(player, DDDSounds.IMMUNITY_HIT, 1.0f, 0.8f + rand.nextFloat() * 0.4f);
					}
					return new CombatResults(false, false, false, true, new DamageMap(), resistMap, armors, mobResists);
				}
				else if(!broken) {
					DDDSounds.playSound(player, DDDSounds.HIGH_RESIST_HIT, 1.0f, 0.8f + rand.nextFloat() * 0.4f);
				}
			}
		}
		for(Entry<DDDDamageType, Float> entry : dmgMap.entrySet()) {
			DDDDamageType type = entry.getKey();
			float resistance = resistMap.get(type);
			if(mobResists.hasImmunity(type) && !mods.shouldApplySlyStrike()) {
				immunity = true;
				dmgMap.put(type, 0.0f);
			}
			else {
				if(resistance > 0) {
					resistance = MathHelper.clamp(resistance - mods.getBruteForceAmount(), 0.0f, Float.MAX_VALUE);
					resistMap.put(type, resistance);
				}
				float damage = entry.getValue();
				float newDmg = damage;
				if(!mods.shouldBypassArmor()) {
					newDmg = applyDamageCalcs(damage, armors.get(type), mods.shouldApplyAnvilReductionCap());
				}
				absorb.put(type, damage - newDmg);
				newDmg *= (1 - resistance);
				dmgMap.put(type, newDmg);

				boolean isVeryClose = Math.abs(newDmg - damage) < 0.0000001D;
				if(!resist && newDmg < damage && !isVeryClose) {
					resist = true;
				}
				if(!weakness && newDmg > damage && !isVeryClose) {
					weakness = true;
				}
			}
		}
		if(damageArmor(defender, absorb, mods.isBootsOnly(), mods.isHelmetOnly())) {
			// refresh armor values
			armors = DDDAPI.accessor.getArmorValuesForEntity(defender, mods.getApplicableArmorSlots());
		}
		return new CombatResults(weakness, resist, immunity, effectiveShield, dmgMap, resistMap, armors, mobResists);
	}

	public static void wipeModifiers(@Nullable Entity attacker, @Nonnull EntityLivingBase defender) {
		getModifiers(attacker, defender).reset();
	}

	public static ArmorMap getApplicableArmorValues(@Nullable Entity attacker, @Nonnull EntityLivingBase defender) {
		CombatModifiers mods = getModifiers(attacker, defender);
		return DDDAPI.accessor.getArmorValuesForEntity(defender, mods.getApplicableArmorSlots());
	}

	public static HitInfo getLastHit(UUID uuid) {
		return lastHit.get(uuid);
	}

	private static CombatModifiers getModifiers(@Nullable Entity attacker, @Nonnull EntityLivingBase defender) {
		UUID id1 = attacker == null ? null : attacker.getUniqueID();
		UUID id2 = defender.getUniqueID();
		return modifiers.getOrDefault(getKey(id1, id2), new CombatModifiers());
	}

	/*
	 * Combine two UUID hashcodes into an int to index into a map. Because the key
	 * will be deleted at the end of calculations (which won't take very long) it's
	 * reasonable to assume the XOR won't give the same key for a different set of
	 * UUID's over the course of that key's lifetime.
	 */
	private static int getKey(UUID attackerID, UUID defenderID) {
		if(attackerID == null) {
			return defenderID.hashCode();
		}
		return attackerID.hashCode() ^ defenderID.hashCode();
	}

	private static DamageMap computeShieldReductions(DamageMap dmgMap, ShieldDistribution shieldDist) {
		if(shieldDist == null) {
			return dmgMap;
		}
		return shieldDist.block(dmgMap);
	}

	/*
	 * Returns true if some armor piece was broken
	 */
	private static boolean damageArmor(@Nonnull EntityLivingBase defender, AbsorptionMap absorbedDamage, boolean bootsOnly, boolean helmetOnly) {
		boolean result = false;
		Map<EntityEquipmentSlot, IArmorDistribution> armorMap = DDDAPI.accessor.getArmorDistributionsForEntity(defender);
		for(ItemStack stack : defender.getArmorInventoryList()) {
			Item item = stack.getItem();
			if(item instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) item;
				if(helmetOnly && armor.armorType != EntityEquipmentSlot.HEAD) {
					continue;
				}
				else if(bootsOnly && armor.armorType != EntityEquipmentSlot.FEET) {
					continue;
				}
				stack.damageItem(getArmorDamageAmount(absorbedDamage, armorMap.get(armor.armorType)), defender);
				if(stack.isEmpty()) {
					result = true;
				}

			}
		}
		return result;
	}

	private static boolean damageShield(@Nullable Entity attacker, @Nonnull EntityLivingBase defender, ItemStack shield, float blockedDmg, float ratio) {
		boolean broken = false;
		if(!(shield.getItem() instanceof ItemShield)) {
			return broken;
		}
		shield.damageItem(1 + (int) Math.floor(blockedDmg), defender);
		if(shield.isEmpty()) {
			defender.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + defender.world.rand.nextFloat() * 0.4f);
			broken = true;
		}
		if(attacker instanceof EntityLivingBase) {
			((EntityLivingBase) attacker).knockBack(defender, 0.4f * ratio + 0.1f, defender.posX - attacker.posX, defender.posZ - attacker.posZ);
		}
		return broken;
	}

	private static boolean canBlockDamage(EntityLivingBase defender, DDDDamageSource src) {
		if(defender.isActiveItemStackBlocking()) {
			Vec3d srcVec = src.getDamageLocation();
			if(srcVec != null) {
				Vec3d lookVec = defender.getLook(1.0f);
				Vec3d diffVec = srcVec.subtractReverse(new Vec3d(defender.posX, defender.posY, defender.posZ)).normalize();
				diffVec = new Vec3d(diffVec.x, 0.0D, diffVec.z);
				if(diffVec.dotProduct(lookVec) < 0.0D) {
					ShieldDistribution dist = DDDAPI.accessor.getShieldDistribution(defender.getActiveItemStack());
					if(dist == null) {
						return false;
					}
					Set<DDDDamageType> damageTypes = new HashSet<DDDDamageType>(src.getExtendedTypes());
					// true if shield can block at least one damage type
					if(YMath.setMinus(damageTypes, dist.getCategories()).size() < damageTypes.size()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static float applyDamageCalcs(float damage, ArmorValues armors, boolean applyAnvilReductionCap) {
		float armor = armors.getArmor(), toughness = armors.getToughness();
		return (float) MathHelper.clamp(damage * (1 - Math.max(armor / 5.0f, armor - damage / (6 + toughness / 4.0f)) / 25.0f), 0.0f, applyAnvilReductionCap ? 0.75 * damage : Float.MAX_VALUE);
	}

	private static int getArmorDamageAmount(AbsorptionMap absorption, IArmorDistribution armorDist) {
		float sum = absorption.entrySet().stream().reduce(0.0f, (a, e) -> a + e.getValue() * armorDist.getWeight(e.getKey()), (u, v) -> u + v);
		if(sum == 0) {
			return 0;
		}
		return (int) MathHelper.clamp(sum, 0, Float.MAX_VALUE) + 1;
	}
}
