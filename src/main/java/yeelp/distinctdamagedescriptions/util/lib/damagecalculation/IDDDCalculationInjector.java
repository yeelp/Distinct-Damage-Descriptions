package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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
}
