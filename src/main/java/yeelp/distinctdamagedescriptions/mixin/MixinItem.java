package yeelp.distinctdamagedescriptions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

@Mixin(Item.class)
public abstract class MixinItem extends IForgeRegistryEntry.Impl<Item> {
	
	@SuppressWarnings("static-method")
	@ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"), remap = false)
	private Multimap<String, AttributeModifier> getAttributeModifiers(Multimap<String, AttributeModifier> original, EntityEquipmentSlot slot, ItemStack stack) {
		if(stack.isEmpty()) {
			return original;
		}
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("AttributeModifiers", NBT.LIST_TAG_ID)) {
			return original;
		}
		Multimap<String, AttributeModifier> mods = stack.getAttributeModifiers(slot);
		for(DDDAttributeModifierCollections.ArmorModifiers armorMod : DDDAttributeModifierCollections.ArmorModifiers.values()) {
			String attributeName = armorMod.getAttribute().getName();
			DebugLib.outputFormattedDebug("Armor attribute name: %s", attributeName);
			for(AttributeModifier modifier : mods.get(attributeName)) {
				if(modifier.getID().equals(armorMod.getUUID())) {
					if(!original.containsValue(modifier)) {
						DebugLib.outputFormattedDebug("Adding attribute modifier via Mixin: %s", modifier.toString());
						original.put(attributeName, modifier);						
					}
				}
			}
		}
		return original;
	}
}
