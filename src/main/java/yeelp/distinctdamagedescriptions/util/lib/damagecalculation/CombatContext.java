package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IValidArmorSlotInjector;

public final class CombatContext {

	private static final Set<IValidArmorSlotInjector> ARMOR_SLOT_INJECTOR = Sets.newTreeSet();
	private static final EntityEquipmentSlot[] armorSlots = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};
	private static final Set<EntityEquipmentSlot> ARMOR_SLOTS_SET;

	private final DamageSource src;
	private final float amount;
	private final Entity attacker;
	private final EntityLivingBase defender;
	private final int hitTime;
	private Optional<ItemStack> shield;
	private Iterable<EntityEquipmentSlot> validArmorSlots;
	
	static {
		registerInjector(new DDDValidArmorInjector());
		ARMOR_SLOTS_SET = EnumSet.noneOf(EntityEquipmentSlot.class);
		Arrays.stream(armorSlots).forEach(ARMOR_SLOTS_SET::add);
	}

	public CombatContext(DamageSource src, float amount, Entity attacker, EntityLivingBase defender) {
		this.src = src;
		this.amount = amount;
		this.attacker = attacker;
		this.defender = defender;
		this.validArmorSlots = determineValidArmorSlots(defender, this.src);
		this.shield = getBlockingShield(this.defender, this.src);
		this.hitTime = defender.ticksExisted;
	}

	/**
	 * @return the src
	 */
	public DamageSource getSource() {
		return this.src;
	}

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return this.amount;
	}

	/**
	 * @return the attacker
	 */
	public Entity getImmediateAttacker() {
		return this.attacker;
	}

	public Entity getTrueAttacker() {
		return this.src.getTrueSource();
	}

	public EntityLivingBase getDefender() {
		return this.defender;
	}
	
	int getLastHitTime() {
		return this.hitTime;
	}

	/**
	 * @return the validArmorSlots
	 */
	Iterable<EntityEquipmentSlot> getValidArmorSlots() {
		return this.validArmorSlots;
	}

	/**
	 * @return the shield
	 */
	public Optional<ItemStack> getShield() {
		return this.shield;
	}

	private static Iterable<EntityEquipmentSlot> determineValidArmorSlots(EntityLivingBase defender, DamageSource src) {
		Set<EntityEquipmentSlot> start = EnumSet.copyOf(ARMOR_SLOTS_SET);
		ARMOR_SLOT_INJECTOR.forEach((v) -> v.accept(defender, src, start));
		DistinctDamageDescriptions.debug("Valid armor slots: " + DebugLib.iterableToString(start));
		return start;
	}

	private static Optional<ItemStack> getBlockingShield(EntityLivingBase defender, DamageSource src) {
		if(defender.isActiveItemStackBlocking() && !src.isUnblockable()) {
			Vec3d srcVec = src.getDamageLocation();
			if(srcVec != null) {
				Vec3d look = defender.getLook(1.0f),
						diff = srcVec.subtractReverse(new Vec3d(defender.posX, defender.posY, defender.posZ)).normalize();
				if(new Vec3d(diff.x, 0, diff.z).dotProduct(look) < 0.0) {
					return Optional.of(defender.getActiveItemStack());
				}
			}
		}
		return Optional.empty();
	}
	
	static void registerInjector(IValidArmorSlotInjector injector) {
		ARMOR_SLOT_INJECTOR.add(injector);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CombatContext) {
			CombatContext otherCtx = (CombatContext) obj;
			return this.src.damageType.equals(otherCtx.src.damageType);			
		}
		return false;
	}
	
	public boolean contextMatches(DamageSource src, @Nullable Entity attacker) {
		Entity trueAttacker = src.getTrueSource();
		return src.damageType.equals(this.src.damageType) 
				&& (trueAttacker == null || trueAttacker.getUniqueID().equals(this.getTrueAttacker().getUniqueID()) 
				&& (attacker == null || attacker.getUniqueID().equals(this.getImmediateAttacker().getUniqueID())));
	}
	
	private static final class DDDValidArmorInjector implements IValidArmorSlotInjector {
		private static final Set<DamageSource> HELMET_ONLY = ImmutableSet.of(DamageSource.ANVIL, DamageSource.FALLING_BLOCK);
		private static final Set<DamageSource> BOOTS_ONLY = ImmutableSet.of(DamageSource.HOT_FLOOR);
		private static final Collection<EntityEquipmentSlot> HEAD_ONLY_SET = EnumSet.of(EntityEquipmentSlot.HEAD);
		private static final Collection<EntityEquipmentSlot> FEET_ONLY_SET = EnumSet.of(EntityEquipmentSlot.FEET);
		
		public DDDValidArmorInjector() {
			//no-op
		}
		
		@Override
		public void accept(EntityLivingBase defender, DamageSource t, Set<EntityEquipmentSlot> u) {
			if(HELMET_ONLY.contains(t)) {
				u.retainAll(HEAD_ONLY_SET);
			}
			else if (BOOTS_ONLY.contains(t)) {
				u.retainAll(FEET_ONLY_SET);
			}
		}
		
		@Override
		public int priority() {
			return Integer.MAX_VALUE;
		}
		
	}
}
