package yeelp.distinctdamagedescriptions.handlers;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorResistances;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.KeyHelper;

public class TooltipHandler extends Handler
{
	private static ITextComponent slashTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.slashing").setStyle(new Style().setColor(TextFormatting.WHITE));
	private static ITextComponent pierceTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.piercing").setStyle(new Style().setColor(TextFormatting.WHITE));
	private static ITextComponent bludgeTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.bludgeoning").setStyle(new Style().setColor(TextFormatting.WHITE));
	private static ITextComponent shiftTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.shift").setStyle(new Style().setColor(TextFormatting.YELLOW));
	private static ITextComponent ctrlTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.ctrl").setStyle(new Style().setColor(TextFormatting.YELLOW));
	private static ITextComponent damageTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damage").setStyle(new Style().setColor(TextFormatting.GRAY));
	private static ITextComponent resistanceTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.resistance").setStyle(new Style().setColor(TextFormatting.GRAY));
	private static ITextComponent weaknessTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.weakness").setStyle(new Style().setColor(TextFormatting.DARK_RED)); 
	private static ITextComponent damageDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damagedistribution").setStyle(new Style().setColor(TextFormatting.GRAY));
	private static ITextComponent armorResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.armorresistances").setStyle(new Style().setColor(TextFormatting.GRAY));
	
	private static final String resistanceColor = TextFormatting.GRAY.toString();
	private static final String weaknessColor = TextFormatting.DARK_RED.toString();
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		IDamageDistribution damages = DDDAPI.accessor.getDamageDistribution(evt.getItemStack());
		IArmorResistances armors = DDDAPI.accessor.getArmorResistances(evt.getItemStack());
		List<String> tooltips = evt.getToolTip();
		if(damages != null)
		{
			int index = tooltips.size()-1;
			if(index <= -1)
			{
				index = tooltips.size();
			}
			boolean shiftHeld = KeyHelper.isShiftHeld();
			if(shiftHeld)
			{
				float slashPercent = damages.getSlashingWeight()*100;
				float piercePercent = damages.getPiercingWeight()*100;
				float bludgePercent = damages.getBludgeoningWeight()*100;
				if(slashPercent > 0)
				{
					tooltips.add(index, makeDamagePercentTooltip(slashPercent, slashTooltip));
				}
				if(piercePercent > 0)
				{
					tooltips.add(index, makeDamagePercentTooltip(piercePercent, pierceTooltip));
				}
				if(bludgePercent > 0)
				{
					tooltips.add(index, makeDamagePercentTooltip(bludgePercent, bludgeTooltip));
				}
			}
			tooltips.add(index, damageDistTooltip.getFormattedText() + (shiftHeld ? "" : " "+shiftTooltip.getFormattedText()));
		}
		if(armors != null)
		{
			int index = tooltips.indexOf(evt.getItemStack().getItem().getRegistryName().toString()) - 1;
			if(index <= -1)
			{
				index = tooltips.size();
			}
			boolean ctrlHeld = KeyHelper.isCtrlHeld();
			if(KeyHelper.isCtrlHeld())
			{
				float slashResist = armors.getSlashingResistance()*100;
				float pierceResist = armors.getPiercingResistance()*100;
				float bludgeResist = armors.getBludgeoningResistance()*100;
				if(slashResist != 0)
				{
					tooltips.add(index, makeArmorTooltip(slashResist, slashTooltip));
				}
				if(pierceResist != 0)
				{
					tooltips.add(index, makeArmorTooltip(pierceResist, pierceTooltip));
				}
				if(bludgeResist != 0)
				{
					tooltips.add(index, makeArmorTooltip(bludgeResist, bludgeTooltip));
				}
			}
			tooltips.add(index, armorResistTooltip.getFormattedText() + (ctrlHeld ? "" : " "+ctrlTooltip.getFormattedText()));
		}
	}

	private String makeArmorTooltip(float percent, ITextComponent tooltip)
	{
		return String.format("   %s%+.2f%% %s %s", percent < 0 ? weaknessColor : resistanceColor, percent, tooltip.getFormattedText(), (percent < 0 ? weaknessTooltip.getFormattedText() : resistanceTooltip.getFormattedText()));
	}

	private String makeDamagePercentTooltip(float percent, ITextComponent tooltip)
	{
		return String.format("   %.2f%% %s %s", percent, tooltip.getFormattedText(), damageTooltip.getFormattedText());
	}
}
