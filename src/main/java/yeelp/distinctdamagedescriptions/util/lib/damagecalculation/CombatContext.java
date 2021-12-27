package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

final class CombatContext {

	private static final Set<DamageSource> HELMET_ONLY = ImmutableSet.of(DamageSource.ANVIL, DamageSource.FALLING_BLOCK);
	private static final Set<DamageSource> BOOTS_ONLY = ImmutableSet.of();
	private static final EntityEquipmentSlot[] armorSlots = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};

	private final DamageSource src;
	private final float amount;
	private final Entity attacker;
	private final EntityLivingBase defender;
	private Optional<ItemStack> shield;
	private Iterable<EntityEquipmentSlot> validArmorSlots;

	CombatContext(DamageSource src, float amount, Entity attacker, EntityLivingBase defender) {
		this.src = src;
		this.amount = amount;
		this.attacker = attacker;
		this.defender = defender;
		this.validArmorSlots = determineValidArmorSlots(this.src);
		this.shield = getBlockingShield(this.defender, this.src);
	}

	/**
	 * @return the src
	 */
	DamageSource getSource() {
		return this.src;
	}

	/**
	 * @return the amount
	 */
	float getAmount() {
		return this.amount;
	}

	/**
	 * @return the attacker
	 */
	Entity getImmediateAttacker() {
		return this.attacker;
	}

	Entity getTrueAttacker() {
		return this.src.getTrueSource();
	}

	EntityLivingBase getDefender() {
		return this.defender;
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
	Optional<ItemStack> getShield() {
		return this.shield;
	}

	private static Iterable<EntityEquipmentSlot> determineValidArmorSlots(DamageSource src) {
		ImmutableList.Builder<EntityEquipmentSlot> builder = ImmutableList.builder();
		if(src.isUnblockable()) {
			return builder.build();
		}
		if(HELMET_ONLY.contains(src)) {
			builder.add(EntityEquipmentSlot.HEAD);
		}
		else if(BOOTS_ONLY.contains(src)) {
			builder.add(EntityEquipmentSlot.FEET);
		}
		else {
			builder.add(armorSlots);
		}
		return builder.build();
	}

	private static Optional<ItemStack> getBlockingShield(EntityLivingBase defender, DamageSource src) {
		if(defender.isActiveItemStackBlocking()) {
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
}
