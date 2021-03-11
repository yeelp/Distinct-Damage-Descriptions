package yeelp.distinctdamagedescriptions.handlers;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.TooltipUtils;
import yeelp.distinctdamagedescriptions.util.lib.KeyHelper;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class TooltipHandler extends Handler
{
	private static final ResourceLocation ICONS = new ResourceLocation(ModConsts.MODID, "textures/tooltips/internaldamagetypes.png");
	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY);
	private static final Style YELLOW = new Style().setColor(TextFormatting.YELLOW);
	private static final ITextComponent shiftTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.shift").setStyle(YELLOW);
	private static final ITextComponent ctrlTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.ctrl").setStyle(YELLOW);
	private static final ITextComponent damageDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damagedistribution").setStyle(GRAY);
	private static final ITextComponent projDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.projectiledistribution").setStyle(GRAY);
	private static final ITextComponent armorResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.armorresistances").setStyle(GRAY);
	private static final ITextComponent shieldDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.shielddist").setStyle(GRAY);
	private static final ITextComponent mobResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobresistances").setStyle(GRAY);
	private static final ITextComponent notGenerated = new TextComponentTranslation("tooltips.distinctdamagedescriptions.notgenerated").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true));
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		ItemStack stack = evt.getItemStack();
		Item item = evt.getItemStack().getItem();
		List<String> tooltips = evt.getToolTip();
		boolean advanced = evt.getFlags().isAdvanced();
		IDamageDistribution damages = DDDAPI.accessor.getDamageDistribution(evt.getItemStack());
		IArmorDistribution armors = DDDAPI.accessor.getArmorResistances(evt.getItemStack());
		ShieldDistribution shield = DDDAPI.accessor.getShieldDistribution(evt.getItemStack());
		IDamageDistribution projDist = DDDConfigurations.projectiles.getFromItemID(YResources.getRegistryString(item));
		boolean shiftHeld = KeyHelper.isShiftHeld();
		boolean ctrlHeld = KeyHelper.isCtrlHeld();
		if(armors != null)
		{
			int index = 1;
			if(ctrlHeld)
			{
				tooltips.addAll(index, TooltipUtils.buildArmorDistTooltips(armors));
			}
			tooltips.add(index, armorResistTooltip.getFormattedText() + getCtrlText(ctrlHeld));
		}
		else if(shield != null)
		{
			int index = 1;
			if(ctrlHeld)
			{
				tooltips.addAll(index, TooltipUtils.buildShieldDistTooltips(shield));
			}
			tooltips.add(index, shieldDistTooltip.getFormattedText() + getCtrlText(ctrlHeld));
		}
		else if(item instanceof ItemMonsterPlacer)
		{
			ItemMonsterPlacer spawnegg = (ItemMonsterPlacer) item;
			ResourceLocation loc = ItemMonsterPlacer.getNamedIdFrom(stack);
			MobResistanceCategories mobCats = DDDConfigurations.mobResists.get(loc.toString());
			int index = 1;
			if(mobCats != null)
			{
				if(ctrlHeld)
				{
					tooltips.addAll(index, TooltipUtils.buildMobResistsTooltips(mobCats));
				}
				tooltips.add(index, mobResistTooltip.getFormattedText() + getCtrlText(ctrlHeld));
			}
			else
			{
				tooltips.add(index, notGenerated.getFormattedText());
			}
		}
		if(damages != null && shouldShowDist(stack))
		{
			int index = 1;
			if(projDist != null)
			{
				if(ctrlHeld)
				{
					tooltips.addAll(index, TooltipUtils.buildDamageDistTooltips(projDist));
				}
				tooltips.add(index, projDistTooltip.getFormattedText() + getCtrlText(ctrlHeld));
			}
			if(shiftHeld)
			{
				tooltips.addAll(index, TooltipUtils.buildDamageDistTooltips(damages));
			}
			tooltips.add(index, damageDistTooltip.getFormattedText() + getShiftText(shiftHeld));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTooltipPost(RenderTooltipEvent.PostText evt)
	{
		ItemStack stack = evt.getStack();
		if(ModConfig.client.useIcons)
		{
			Minecraft mc = Minecraft.getMinecraft();
			GL11.glPushMatrix();
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			mc.getTextureManager().bindTexture(ICONS);
			for(Tuple<Integer, Integer> t : TooltipUtils.getIconsToDraw(evt.getStack(), KeyHelper.isShiftHeld(), KeyHelper.isCtrlHeld()))
			{
				Gui.drawModalRectWithCustomSizedTexture(evt.getX() - 2, evt.getY() + t.getFirst(), t.getSecond(), 0, 10, 10, 256, 256);
			}	
			GL11.glPopMatrix();
		}
	}
	
	private static String getShiftText(boolean held)
	{
		return getWhenNotHeld(held, shiftTooltip);
	}
	
	private static String getCtrlText(boolean held)
	{
		return getWhenNotHeld(held, ctrlTooltip);
	}
	
	private static String getWhenNotHeld(boolean held, ITextComponent tooltip)
	{
		return held ? "" : " "+tooltip.getFormattedText();
	}
	
	private static boolean shouldShowDist(ItemStack stack)
	{
		return DDDConfigurations.items.configured(YResources.getRegistryString(stack)) || ModConfig.client.alwaysShowDamageDistTooltip;
	}
}
