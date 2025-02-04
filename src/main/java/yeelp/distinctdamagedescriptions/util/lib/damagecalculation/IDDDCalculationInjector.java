package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.util.IPriority;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;

public interface IDDDCalculationInjector extends IPriority {
	
	@FunctionalInterface
	public interface IArmorValuesInjector extends IDDDCalculationInjector, BiFunction<ItemStack, EntityEquipmentSlot, ArmorValues> {
		//empty
	}
	
	@FunctionalInterface
	public interface IValidArmorSlotInjector extends IDDDCalculationInjector {
		void accept(EntityLivingBase defender, DamageSource src, Set<EntityEquipmentSlot> currSlots);
	}
	
	@FunctionalInterface
	public interface IArmorModifierInjector extends IDDDCalculationInjector {
		/**
		 * Modify the armor values that will be applied
		 * @param willModsBeApplied if the mods will actually be applied
		 * @param deltaArmor the current delta armor values
		 * @return True if the armor mods should still be applied, false if not.
		 */
		boolean modify(boolean willModsBeApplied, EntityLivingBase defender, Map<EntityEquipmentSlot, ArmorValues> deltaArmor);
		
		/**
		 * Should this injector fire even if the armor values are not going to be applied?
		 * @return True if yes, false if no.
		 */
		default boolean shouldFireIfNotBeingApplied() {
			return false;
		}
	}
	
	@FunctionalInterface
	public interface ICancelCalculationInjector extends IDDDCalculationInjector {
		enum Phase {
			ATTACK,
			HURT,
			DAMAGE
		}
		
		static Phase determinePhase(@SuppressWarnings("unused") LivingAttackEvent evt) {
			return Phase.ATTACK;
		}
		
		static Phase determinePhase(@SuppressWarnings("unused") LivingHurtEvent evt) {
			return Phase.HURT;
		}
		
		static Phase determinePhase(@SuppressWarnings("unused") LivingDamageEvent evt) {
			return Phase.DAMAGE;
		}
		
		/**
		 * Should the calculation for this phase be canceled? If canceled in this phase, it will still run in other phases, unless canceled there as well.
		 * @param currentlyCanceled If the calculation is currently being canceled.
		 * @param phase the phase of calculation we're in. ATTACK, HURT, DAMAGE
		 * @param defender the defending entity
		 * @param src the damage source
		 * @param amount the amount
		 * @return True if the calculation should be canceled, false otherwise.
		 */
		boolean shouldCancel(boolean currentlyCanceled, Phase phase, EntityLivingBase defender, DamageSource src, float amount);
	}
}
