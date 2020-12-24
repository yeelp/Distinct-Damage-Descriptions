package yeelp.distinctdamagedescriptions.util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.*;

public final class TooltipUtils
{
	private static final DecimalFormat formatter = new DecimalFormat("##.##%");
	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY);
	private static final Style WHITE = new Style().setColor(TextFormatting.WHITE);
	private static final Style LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE);
	private static final Style AQUA = new Style().setColor(TextFormatting.AQUA);
	private static final Style DARK_RED = new Style().setColor(TextFormatting.DARK_RED);
	private static final ITextComponent slashTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.slashing").setStyle(WHITE);
	private static final ITextComponent pierceTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.piercing").setStyle(WHITE);
	private static final ITextComponent bludgeTooltip = new TextComponentTranslation("damagetypes.distinctdamagedescriptions.bludgeoning").setStyle(WHITE);
	private static final ITextComponent damageTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damage").setStyle(GRAY);
	private static final ITextComponent resistanceTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.resistance").setStyle(GRAY);
	private static final ITextComponent weaknessTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.weakness").setStyle(DARK_RED); 
	private static final ITextComponent effectivenessTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveness").setStyle(GRAY);
	private static final ITextComponent mobImmunityTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.immunities").setStyle(AQUA);
	private static final ITextComponent mobAdaptiveTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptivechance").setStyle(LIGHT_PURPLE);
	private static final ITextComponent mobAdaptiveAmountTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptiveamount").setStyle(LIGHT_PURPLE);
	private static final ITextComponent noResists = new TextComponentTranslation("tooltips.distinctdamagedescriptions.noresists").setStyle(WHITE);
	private static final Map<String, ITextComponent> damageTypeTooltips = new HashMap<String, ITextComponent>();
	private static final String grayColour = TextFormatting.GRAY.toString();
	private static final String redColour = TextFormatting.DARK_RED.toString();
	private static final Queue<Integer> imageIndices = new LinkedList<Integer>();
	
	static
	{
		damageTypeTooltips.put(SLASHING, slashTooltip);
		damageTypeTooltips.put(PIERCING, pierceTooltip);
		damageTypeTooltips.put(BLUDGEONING, bludgeTooltip);
	}
	
	public static final List<String> buildDamageDistTooltips(IDamageDistribution dist)
	{
		return buildDistTooltip(dist, grayColour, damageTooltip.getFormattedText());
	}
	
	public static final List<String> buildDamageDistTooltips(Map<String, Float> dist)
	{
		return buildDistTooltip(dist, grayColour, damageTooltip.getFormattedText());
	}
	
	public static final List<String> buildArmorDistTooltips(IArmorDistribution dist)
	{
		return buildDistTooltip(dist, grayColour, effectivenessTooltip.getFormattedText());
	}
	
	public static final List<String> buildShieldDistTooltips(ShieldDistribution dist)
	{
		return buildDistTooltip(dist, grayColour, effectivenessTooltip.getFormattedText());
	}
	
	public static final List<String> buildMobResistsTooltips(MobResistanceCategories resists)
	{
		List<String> lst = new LinkedList<String>();
		for(Entry<String, Float> entry : resists.getResistanceMap().entrySet())
		{
			lst.add(makeOneMobResistString(entry.getValue(), getDamageName(entry.getKey())));
		}
		if(lst.isEmpty())
		{
			lst.add(noResists.getFormattedText());
		}
		String immunities = makeMobImmunityTooltip(resists.getImmunities());
		if(immunities != null)
		{
			lst.add(immunities);
		}
		lst.addAll(makeMobAdaptiveTooltip(resists.adaptiveChance(), resists.getAdaptiveAmount()));
		return lst;
	}
	
	private static String makeMobImmunityTooltip(Set<String> immunities)
	{
		if(immunities.isEmpty())
		{
			return null;
		}
		String str = mobImmunityTooltip.getFormattedText()+" ";
		String[] strings = new String[immunities.size()];
		int index = 0;
		for(String s : immunities)
		{
			strings[index++] = getDamageName(s);
		}
		return str + YLib.joinNiceString(true, ",", strings);
	}
	
	private static List<String> makeMobAdaptiveTooltip(float adaptiveChance, float adaptiveAmount)
	{
		if(adaptiveChance == 0 && adaptiveAmount == 0)
		{
			return Collections.emptyList();
		}
		String str1 = String.format("%s %s", mobAdaptiveTooltip.getFormattedText(), formatNum(adaptiveChance));
		String str2 = String.format("   %s %s", mobAdaptiveAmountTooltip.getFormattedText(), formatNum(adaptiveAmount));
		return Lists.newArrayList(str1, str2);
	}
	
	private static List<String> buildDistTooltip(IDistribution dist, String prefix, String suffix)
	{
		List<String> lst = new LinkedList<String>();
		for(String s : dist.getCategories())
		{
			float percent = dist.getWeight(s);
			lst.add(makeOneDistString(percent, getDamageName(s), prefix, suffix));
		}
		return lst;
	}
	
	private static List<String> buildDistTooltip(Map<String, Float> dist, String prefix, String suffix)
	{
		List<String> lst = new LinkedList<String>();
		for(String s : dist.keySet())
		{
			float percent = dist.get(s);
			lst.add(makeOneDistString(percent, getDamageName(s), prefix, suffix));
		}
		return lst;
	}
	
	private static String makeOneDistString(float percent, String damageName, String prefix, String suffix)
	{
		return String.format("   %s%s %s %s", prefix, formatNum(percent), damageName, suffix);
	}
	
	private static String makeOneMobResistString(float percent, String damageType)
	{
		boolean isNegative = percent < 0;
		return String.format("   %s%s %s %s", isNegative ? redColour : grayColour, formatNum(percent), damageType, isNegative ? weaknessTooltip.getFormattedText() : resistanceTooltip.getFormattedText());
	}
	
	private static String formatNum(float num)
	{
		return String.format("%s%s", num < 0 ? "" : "+", formatter.format(num)).substring(1);
	}
	
	private static String getDamageName(String s)
	{
		return DDDAPI.accessor.isPhysicalDamage(s) ? damageTypeTooltips.get(s).getFormattedText() : DDDRegistries.damageTypes.getDisplayName(s);
	}
}
