package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Iterator;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * Library of Armor utilities
 * @author Yeelp
 *
 */
public final class YArmor {

	public static ArmorValues getArmorFromStack(ItemStack stack, ItemArmor armor) {
		return getArmorFromStack(stack, armor.armorType);
	}
	
	public static ArmorValues getArmorFromStack(ItemStack stack, EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> mods = stack.getAttributeModifiers(slot);
		Queue<Double> values = Lists.newLinkedList();
		for(DDDAttributeModifierCollections.ArmorModifiers aMod : DDDAttributeModifierCollections.ArmorModifiers.values()) {
			values.add(mods.get(aMod.getAttribute().getName()).stream().mapToDouble(AttributeModifier::getAmount).sum());
		}
		Iterator<Double> it = values.iterator();
		return new ArmorValues(it.next().floatValue(), it.next().floatValue());
	}
}
