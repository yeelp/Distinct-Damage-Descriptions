package yeelp.distinctdamagedescriptions.integration.firstaid;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections.DDDAttributeModifier;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public class FirstAidEventHandler extends Handler {
	
	private static final Set<UUID> HAD_MODS_APPLIED = Sets.newHashSet();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SuppressWarnings("static-method")
	public void onFirstAidDamage(FirstAidLivingDamageEvent evt) {
		EntityPlayer player = evt.getEntityPlayer();
		if(player.world.isRemote || !HAD_MODS_APPLIED.remove(player.getUniqueID())) {
			return;
		}
		DistinctDamageDescriptions.debug("Running First Aid Damage Event to remove modifiers...");
		DDDAPI.accessor.getDDDCombatTracker(player).ifPresent((ct) -> ModConsts.ARMOR_SLOTS_ITERABLE.forEach((slot) -> {
			ItemStack stack = player.getItemStackFromSlot(slot);
			if(stack.isEmpty()) {
				return;
			}
			Optional.ofNullable(stack.getTagCompound()).map((tag) -> tag.getTagList(NBT.ATTRIBUTE_MODIFIERS_KEY, NBT.COMPOUND_TAG_ID)).ifPresent((lst) -> {
				DebugLib.outputFormattedDebug("FIRST AID COMPAT: NBTList Attribute Modifiers: %s", lst.toString());
				for(int i = 0; i < lst.tagCount(); i++) {
					NBTTagCompound compound = lst.getCompoundTagAt(i);
					String name = compound.getString(NBT.NAME_KEY);
					Optional<DDDAttributeModifier> mod = DDDAttributeModifierCollections.getModifierFromName(name);
					if(mod.isPresent()) {
						lst.removeTag(i--);
						mod.get().removeModifier(player);
					}
				}
				if(lst.tagCount() == 0) {
					stack.getTagCompound().removeTag(NBT.ATTRIBUTE_MODIFIERS_KEY);
				}
			});
		}));
	}
	
	static void markPlayerHasMods(EntityPlayer player) {
		HAD_MODS_APPLIED.add(player.getUniqueID());
	}
}
