package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;

class ArmorClassifier implements IClassifier<ArmorMap> {

	@Override
	public Optional<ArmorMap> classify(CombatContext context) {
		ArmorMap aMap = new ArmorMap();
		for(EntityEquipmentSlot slot : context.getValidArmorSlots()) {
			ItemStack slottedStack = context.getDefender().getItemStackFromSlot(slot);
			Item slottedItem = slottedStack.getItem();
			if(slottedItem instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) slottedItem;
				DDDAPI.accessor.getArmorResistances(slottedStack).map((dist) -> dist.distributeArmor(armor.damageReduceAmount, armor.toughness)).ifPresent((m) -> m.forEach((k, v) -> aMap.merge(k, v, ArmorValues::merge)));
			}
		}
		return Optional.of(aMap);
	}
}
