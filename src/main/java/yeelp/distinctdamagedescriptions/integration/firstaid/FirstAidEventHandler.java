package yeelp.distinctdamagedescriptions.integration.firstaid;

import java.util.Optional;

import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public class FirstAidEventHandler extends Handler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SuppressWarnings("static-method")
	public void onFirstAidDamage(FirstAidLivingDamageEvent evt) {
		EntityPlayer player = evt.getEntityPlayer();
		if(player.world.isRemote) {
			return;
		}
		DistinctDamageDescriptions.debug("Running First Aid Damage Event to remove modifiers...");
		DDDAPI.accessor.getDDDCombatTracker(player).get().getNewDeltaArmorValues().ifPresent((vals) -> vals.forEach((slot, armorValues) -> {
			ItemStack stack = player.getItemStackFromSlot(slot);
			if(stack.isEmpty()) {
				return;
			}
			Optional.ofNullable(stack.getTagCompound()).flatMap((tag) -> Optional.ofNullable(tag.getTagList(NBT.ATTRIBUTE_MODIFIERS_KEY, NBT.COMPOUND_TAG_ID))).ifPresent((lst) -> {
				DebugLib.outputFormattedDebug("FIRST AID COMPAT: NBTList Attribute Modifiers: %s", lst.toString());
				for(int i = 0; i < lst.tagCount(); i++) {
					NBTTagCompound compound = lst.getCompoundTagAt(i);
					String name = compound.getString(NBT.NAME_KEY);
					if(DDDAttributeModifierCollections.getModifierRecordFromName(name).isPresent()) {
						lst.removeTag(i--);
					}
				}
				if(lst.tagCount() == 0) {
					stack.getTagCompound().removeTag(NBT.ATTRIBUTE_MODIFIERS_KEY);
				}
			});
		}));
	}
}
