package yeelp.distinctdamagedescriptions.handlers;

import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.BLUDGEONING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.PIERCING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.SLASHING;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.ResourceLocation;
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
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
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
	private static final ITextComponent mobAdaptiveAmountTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptiveamount").setStyle(LIGHT_PURPLE);
	private static final ITextComponent notGenerated = new TextComponentTranslation("tooltips.distinctdamagedescriptions.notgenerated").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true));
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
		Map<String, Float> projDist = DDDRegistries.projectileProperties.getProjectileDamageTypesFromItemID(item.getRegistryName().toString());
		boolean shiftHeld = KeyHelper.isShiftHeld();
		boolean ctrlHeld = KeyHelper.isCtrlHeld();
		if(damages != null && (DDDRegistries.itemProperties.doesItemHaveCustomDamageDistribution(evt.getItemStack().getItem().getRegistryName().toString()) || ModConfig.client.alwaysShowDamageDistTooltip))
		{
			int index = 1;
			if(projDist != null)
			{
				if(ctrlHeld)
				{
					for(String type : projDist.keySet())
					{
						float percent = projDist.get(type);
						int i = type.equals(SLASHING) ? 0 :
							    type.equals(PIERCING) ? 1 :
							    type.equals(BLUDGEONING) ? 2 : -1;
						if(percent > 0 && i != -1)
						{
							tooltips.add(index, makeDamagePercentTooltip(percent, damageTypeTooltips[i]));
						}
					}
				}
				tooltips.add(index, projDistTooltip.getFormattedText() + (ctrlHeld ? "" : " "+ctrlTooltip.getFormattedText()));
			}
			if(shiftHeld)
			{
				float slashPercent = damages.getWeight(SLASHING);
				float piercePercent = damages.getWeight(PIERCING);
				float bludgePercent = damages.getWeight(BLUDGEONING);
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
			ResourceLocation loc = ItemMonsterPlacer.getNamedIdFrom(evt.getItemStack());
			Optional<MobResistanceCategories> oMobCats = DDDRegistries.mobResists.getResistancesForMob(loc.toString());
			MobResistanceCategories mobCats;
			int index = tooltips.size();
			if(advanced)
			{
				index = tooltips.size()-2;
			}
			int startingIndex = index;
			if(oMobCats.isPresent())
			{
				mobCats = oMobCats.get();
				if(ctrlHeld)
				{
					float[] resistsPercents = {mobCats.getResistance(SLASHING), mobCats.getResistance(PIERCING), mobCats.getResistance(BLUDGEONING)};
					boolean[] immunities = {mobCats.hasImmunity(SLASHING), mobCats.hasImmunity(PIERCING), mobCats.hasImmunity(BLUDGEONING)};
					float adaptive = mobCats.adaptiveChance();
					float adaptiveAmount = mobCats.getAdaptiveAmount();
					Tuple<Float, Float> adaptiveInfo = new Tuple<Float, Float>(adaptive, adaptiveAmount);
					boolean hasImmunities = false;
					for(int i = 0; i < 3; i++)
					{
						tooltips.add(index, makeMobResistTooltip(resistsPercents[i], damageTypeTooltips[i]));
						hasImmunities = hasImmunities || immunities[i];
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
						tooltips.add(index, adaptiveTooltips.getSecond());
						tooltips.add(index, adaptiveTooltips.getFirst());
					}
				}
				tooltips.add(startingIndex, mobResistTooltip.getFormattedText() + (ctrlHeld ? "" : " "+ctrlTooltip.getFormattedText()));
			}
			else
			{
				tooltips.add(index, notGenerated.getFormattedText());
			}
		}
		if(armors != null)
		{
			int index = 1;
			if(ctrlHeld)
			{
				float slashResist = armors.getWeight(SLASHING);
				float pierceResist = armors.getWeight(PIERCING);
				float bludgeResist = armors.getWeight(BLUDGEONING);
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
		for(int i = 0; i < 3; i++)
		{
			if(immunities[i])
			{
				str += damageTypeTooltips[i].getFormattedText() + ", ";
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
