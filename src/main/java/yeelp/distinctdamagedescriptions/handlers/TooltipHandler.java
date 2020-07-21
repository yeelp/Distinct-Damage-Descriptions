package yeelp.distinctdamagedescriptions.handlers;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.Tuple;
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
import yeelp.distinctdamagedescriptions.util.ComparableTriple;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.KeyHelper;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public class TooltipHandler extends Handler
{
	private static final DecimalFormat formatter = new DecimalFormat("##.##%");
	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY);
	private static final Style WHITE = new Style().setColor(TextFormatting.WHITE);
	private static final Style LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE);
	private static final Style YELLOW = new Style().setColor(TextFormatting.YELLOW);
	private static final Style AQUA = new Style().setColor(TextFormatting.AQUA);
	private static final Style DARK_RED = new Style().setColor(TextFormatting.DARK_RED);
	private static final ITextComponent slashTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.slashing").setStyle(WHITE);
	private static final ITextComponent pierceTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.piercing").setStyle(WHITE);
	private static final ITextComponent bludgeTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.bludgeoning").setStyle(WHITE);
	private static final ITextComponent shiftTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.shift").setStyle(YELLOW);
	private static final ITextComponent ctrlTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.ctrl").setStyle(YELLOW);
	private static final ITextComponent damageTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damage").setStyle(GRAY);
	private static final ITextComponent resistanceTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.resistance").setStyle(GRAY);
	private static final ITextComponent weaknessTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.weakness").setStyle(DARK_RED); 
	private static final ITextComponent effectivenessTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveness").setStyle(GRAY);
	private static final ITextComponent damageDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damagedistribution").setStyle(GRAY);
	private static final ITextComponent projDistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.projectiledistribution").setStyle(GRAY);
	private static final ITextComponent armorResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.armorresistances").setStyle(GRAY);
	private static final ITextComponent mobResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobresistances").setStyle(GRAY);
	private static final ITextComponent mobImmunityTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.immunities").setStyle(AQUA);
	private static final ITextComponent mobAdaptiveTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptivechance").setStyle(LIGHT_PURPLE);
	private static final ITextComponent mobAdaptiveAmountTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptiveAmount").setStyle(LIGHT_PURPLE);
	private static final ITextComponent[] damageTypeTooltips = {slashTooltip, pierceTooltip, bludgeTooltip};
	private static final String resistanceColor = TextFormatting.GRAY.toString();
	private static final String weaknessColor = TextFormatting.DARK_RED.toString();
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		IDamageDistribution damages = DDDAPI.accessor.getDamageDistribution(evt.getItemStack());
		IArmorDistribution armors = DDDAPI.accessor.getArmorResistances(evt.getItemStack());
		List<String> tooltips = evt.getToolTip();
		boolean advanced = evt.getFlags().isAdvanced();
		Item item = evt.getItemStack().getItem();
		ComparableTriple<Float, Float, Float> projDist = DistinctDamageDescriptions.getProjectileDamageTypesFromItemID(item.getRegistryName().toString());
		boolean shiftHeld = KeyHelper.isShiftHeld();
		boolean ctrlHeld = KeyHelper.isCtrlHeld();
		if(damages != null)
		{
			int index = 1;
			if(projDist != null)
			{
				if(ctrlHeld)
				{
					for(DamageType type : DamageType.values())
					{
						int i = type.ordinal();
						float percent = projDist.<Float>get(i);
						if(percent > 0)
						{
							tooltips.add(index, makeDamagePercentTooltip(percent, damageTypeTooltips[i]));
						}
					}
				}
				tooltips.add(index, projDistTooltip.getFormattedText() + (ctrlHeld ? "" : " "+ctrlTooltip.getFormattedText()));
			}
			if(shiftHeld)
			{
				float slashPercent = damages.getSlashingWeight();
				float piercePercent = damages.getPiercingWeight();
				float bludgePercent = damages.getBludgeoningWeight();
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
		if(item instanceof ItemMonsterPlacer)
		{
			ItemMonsterPlacer spawnegg = (ItemMonsterPlacer) item;
			MobResistanceCategories mobCats = DistinctDamageDescriptions.getMobResistances(ItemMonsterPlacer.getNamedIdFrom(evt.getItemStack()).toString());
			int index = tooltips.size();
			if(advanced)
			{
				index = tooltips.size()-2;
			}
			int startingIndex = index;
			if(ctrlHeld)
			{
				float[] resistsPercents = {mobCats.getSlashingResistance(), mobCats.getPiercingResistance(), mobCats.getBludgeoningResistance()};
				boolean[] immunities = {mobCats.getSlashingImmunity(), mobCats.getPiercingImmunity(), mobCats.getBludgeoningImmunity()};
				float adaptive = mobCats.adaptiveChance();
				float adaptiveAmount = mobCats.getAdaptiveAmount();
				Tuple<Float, Float> adaptiveInfo = new Tuple<Float, Float>(adaptive, adaptiveAmount);
				boolean hasImmunities = false;
				for(DamageType type : DamageType.values())
				{
					tooltips.add(index, makeMobResistTooltip(resistsPercents[type.ordinal()], damageTypeTooltips[type.ordinal()]));
					hasImmunities = hasImmunities || immunities[type.ordinal()];
				}
				if(hasImmunities)
				{
					index = tooltips.size();
					if(advanced)
					{
						index = tooltips.size()-2;
					}
					tooltips.add(index, makeMobImmunityTooltip(immunities));
				}
				if(adaptive > 0)
				{
					index = tooltips.size();
					if(advanced)
					{
						index = tooltips.size()-2;
					}
					Tuple<String, String> adaptiveTooltips = makeMobAdaptiveTooltip(adaptiveInfo);
					tooltips.add(index, adaptiveTooltips.getFirst());
					tooltips.add(index, adaptiveTooltips.getSecond());
				}
			}
			tooltips.add(startingIndex, mobResistTooltip.getFormattedText() + (ctrlHeld ? "" : " "+ctrlTooltip.getFormattedText()));
		}
		if(armors != null)
		{
			int index = 1;
			if(ctrlHeld)
			{
				float slashResist = armors.getSlashingWeight();
				float pierceResist = armors.getPiercingWeight();
				float bludgeResist = armors.getBludgeoningWeight();
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

	private static String makeArmorTooltip(float percent, ITextComponent tooltip)
	{
		return String.format("   %s %s %s", formatNum(percent).substring(1), tooltip.getFormattedText(), effectivenessTooltip.getFormattedText());
	}

	private static String makeDamagePercentTooltip(float percent, ITextComponent tooltip)
	{
		return String.format("   %s %s %s", formatNum(percent).substring(1), tooltip.getFormattedText(), damageTooltip.getFormattedText());
	}
	
	private static String makeMobResistTooltip(float percent, ITextComponent tooltip)
	{
		boolean isNegative = percent < 0;
		return String.format("   %s%s %s %s", isNegative ? weaknessColor : resistanceColor, formatNum(percent).substring(1), tooltip.getFormattedText(), isNegative ? weaknessTooltip.getFormattedText() : resistanceTooltip.getFormattedText());
	}
	
	private static String makeMobImmunityTooltip(boolean[] immunities)
	{
		String str = mobImmunityTooltip.getFormattedText()+" ";
		for(DamageType type : DamageType.values())
		{
			if(immunities[type.ordinal()])
			{
				str += damageTypeTooltips[type.ordinal()].getFormattedText() + ", ";
			}
		}
		str = str.trim();
		return str.substring(0, str.length()-1);
	}
	
	private static Tuple<String, String> makeMobAdaptiveTooltip(Tuple<Float, Float> adaptiveInfo)
	{
		String str1 = String.format("%s %s", mobAdaptiveTooltip.getFormattedText(), formatNum(adaptiveInfo.getFirst().floatValue()).substring(1));
		String str2 = String.format("   %s %s", mobAdaptiveAmountTooltip.getFormattedText(), formatNum(adaptiveInfo.getSecond().floatValue()).substring(1));
		return new Tuple<String, String>(str1, str2);
	}
	
	private static String formatNum(float num)
	{
		return String.format("%s%s", num < 0 ? "" : "+", formatter.format(num));
	}
}
