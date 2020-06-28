package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.DamageCategories;

public class CapabilityHandler extends Handler
{
	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<ItemStack> evt)
	{
		if(evt.getObject().getItem().getRegistryName().toString().equals("minecraft:wooden_sword"))
		{
			evt.addCapability(new ResourceLocation(ModConsts.MODID, "DamageCategories"), new DamageCategories(0, 0, 4));
		}
		else
		{
			evt.addCapability(new ResourceLocation(ModConsts.MODID, "DamageCategories"), new DamageCategories(0, 0, 1));
		}
	}
}
