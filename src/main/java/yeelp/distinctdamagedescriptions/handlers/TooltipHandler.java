package yeelp.distinctdamagedescriptions.handlers;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.mixin.MixinASMItemStack;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public class TooltipHandler extends Handler {
	private static final ResourceLocation ICONS = new ResourceLocation(ModConsts.MODID, "textures/tooltips/internaldamagetypes.png");
	private static final Map<String, ItemStack> CACHED_GENERATED_CAPABILITIES = Maps.newHashMap();
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt) {
		ItemStack stack = evt.getItemStack();
		if(evt.getToolTip().size() > 0 && !stack.isEmpty()) {
			ItemStack cachedStack = getCachedStackWithCapabilities(stack);
			evt.getToolTip().addAll(1, TooltipDistributor.getDistributor(cachedStack).getTooltip(cachedStack));			
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTooltipPost(RenderTooltipEvent.PostText evt) {
		if(ModConfig.client.useIcons && !evt.getStack().isEmpty()) {
			if(DDDHooks.fireShouldDrawIcons().getResult() == Result.DENY) {
				return;
			}
			Minecraft mc = Minecraft.getMinecraft();
			GL11.glPushMatrix();
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			mc.getTextureManager().bindTexture(ICONS);
			ItemStack cachedStack = getCachedStackWithCapabilities(evt.getStack());
			TooltipDistributor distributor = TooltipDistributor.getDistributor(cachedStack);
			distributor.getIcons(cachedStack, evt.getX(), evt.getY(), evt.getLines()).forEach((i) -> {
				if(i.getU() >= 0) {
					Gui.drawModalRectWithCustomSizedTexture(i.getX(), i.getY(), i.getU(), 0, 10, 10, 256, 256);					
				}
			});
			GL11.glPopMatrix();
		}
	}
	
	private static boolean shouldHaveCapabilities(String key) {
		return DDDConfigurations.armors.configured(key) || DDDConfigurations.items.configured(key) || DDDConfigurations.shields.configured(key);
	}
	
	private static ItemStack getCachedStackWithCapabilities(ItemStack stack) {
		MixinASMItemStack asmStack = (MixinASMItemStack) (Object) stack;
		if(asmStack.getCapNBT() == null) {
			return YResources.getRegistryStringWithMetadata(stack).map((s) -> {
				if(shouldHaveCapabilities(s)) {
					if(CACHED_GENERATED_CAPABILITIES.containsKey(s)) {
						return CACHED_GENERATED_CAPABILITIES.get(s);
					}
					asmStack.doForgeInit();
					CACHED_GENERATED_CAPABILITIES.put(s, stack);
				}
				return stack;
			}).orElse(stack);
		}
		return stack;
	}
}
