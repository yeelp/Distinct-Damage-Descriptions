package yeelp.distinctdamagedescriptions.util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class TooltipUtils {
	private static final int ICON_HEIGHT = 10;
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

	public static class TooltipString implements Iterable<Tuple<Integer, TooltipComp>> {
		private final List<TooltipComp> comps;
		private final int xStart, y;

		TooltipString(int x, int y) {
			this.xStart = x;
			this.y = y;
			comps = new LinkedList<TooltipComp>();
		}

		public int getY() {
			return this.y;
		}

		public void add(TooltipComp comp) {
			comps.add(comp);
		}

		@Override
		public Iterator<Tuple<Integer, TooltipComp>> iterator() {
			return new TooltipIterator(this);
		}

		private class TooltipIterator implements Iterator<Tuple<Integer, TooltipComp>> {
			private Iterator<TooltipComp> it;
			private int x;

			private TooltipIterator(TooltipString s) {
				this.x = xStart;
				this.it = comps.iterator();
			}

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Tuple<Integer, TooltipComp> next() {
				TooltipComp comp = it.next();
				Tuple<Integer, TooltipComp> t = new Tuple<Integer, TooltipComp>(x, comp);
				x += Minecraft.getMinecraft().fontRenderer.getStringWidth(comp.text);
				return t;
			}

		}
	}

	public static class TooltipComp {
		private String text;
		private int colour;

		TooltipComp(String s, int colour) {
			this.text = s;
			this.colour = colour;
		}

		public String getText() {
			return text;
		}

		public int getColour() {
			return colour;
		}
	}

	static {
		damageTypeTooltips.put(DDDBuiltInDamageType.SLASHING.getTypeName(), slashTooltip);
		damageTypeTooltips.put(DDDBuiltInDamageType.PIERCING.getTypeName(), pierceTooltip);
		damageTypeTooltips.put(DDDBuiltInDamageType.BLUDGEONING.getTypeName(), bludgeTooltip);
	}

	public static final List<String> buildDamageDistTooltips(IDamageDistribution dist) {
		return buildDistTooltip(dist, grayColour, damageTooltip.getFormattedText());
	}

	public static final List<String> buildDamageDistTooltips(DDDBaseMap<Float> dist) {
		return buildDistTooltip(dist, grayColour, damageTooltip.getFormattedText());
	}

	public static final List<String> buildArmorDistTooltips(IArmorDistribution dist) {
		return buildDistTooltip(dist, grayColour, effectivenessTooltip.getFormattedText());
	}

	public static final List<String> buildShieldDistTooltips(ShieldDistribution dist) {
		return buildDistTooltip(dist, grayColour, effectivenessTooltip.getFormattedText());
	}

	public static final List<String> buildMobResistsTooltips(MobResistanceCategories resists) {
		List<String> lst = new LinkedList<String>();
		for(Entry<DDDDamageType, Float> entry : resists.getResistanceMap().entrySet()) {
			lst.add(makeOneMobResistString(entry.getValue(), getDamageName(entry.getKey())));
		}
		if(lst.isEmpty()) {
			lst.add(noResists.getFormattedText());
		}
		String immunities = makeMobImmunityTooltip(resists.getImmunities());
		if(immunities != null) {
			lst.add(immunities);
		}
		lst.addAll(makeMobAdaptiveTooltip(resists.adaptiveChance(), resists.getAdaptiveAmount()));
		return lst;
	}

	private static String makeMobImmunityTooltip(Set<DDDDamageType> immunities) {
		if(immunities.isEmpty()) {
			return null;
		}
		String str = mobImmunityTooltip.getFormattedText() + " ";
		String[] strings = new String[immunities.size()];
		int index = 0;
		for(DDDDamageType type : immunities) {
			strings[index++] = getDamageName(type, false);
		}
		return str + YLib.joinNiceString(true, ",", strings);
	}

	private static List<String> makeMobAdaptiveTooltip(float adaptiveChance, float adaptiveAmount) {
		if(adaptiveChance == 0 && adaptiveAmount == 0) {
			return Collections.emptyList();
		}
		String str1 = String.format("%s %s", mobAdaptiveTooltip.getFormattedText(), formatNum(adaptiveChance));
		String str2 = String.format("   %s %s", mobAdaptiveAmountTooltip.getFormattedText(), formatNum(adaptiveAmount));
		return Lists.newArrayList(str1, str2);
	}

	private static List<String> buildDistTooltip(IDistribution dist, String prefix, String suffix) {
		List<String> lst = new LinkedList<String>();
		for(DDDDamageType type : dist.getCategories()) {
			float percent = dist.getWeight(type);
			String name = getDamageName(type);
			lst.add(makeOneTooltipString(percent, name, prefix, name.isEmpty() ? "" : suffix));
		}
		return lst;
	}

	private static List<String> buildDistTooltip(DDDBaseMap<Float> dist, String prefix, String suffix) {
		List<String> lst = new LinkedList<String>();
		for(DDDDamageType type : dist.keySet()) {
			float percent = dist.get(type);
			String name = getDamageName(type);
			lst.add(makeOneTooltipString(percent, name, prefix, name.isEmpty() ? "" : suffix));
		}
		return lst;
	}

	private static String makeOneTooltipString(float percent, String damageName, String prefix, String suffix) {
		return String.format("   %s%s %s %s", prefix, formatNum(percent), damageName, suffix);
	}

	private static String makeOneMobResistString(float percent, String damageType) {
		boolean isNegative = percent < 0;
		return makeOneTooltipString(percent, damageType, isNegative ? redColour : grayColour, isNegative ? weaknessTooltip.getFormattedText() : resistanceTooltip.getFormattedText());
	}

	private static String formatNum(float num) {
		return String.format("%s%s", num < 0 ? "" : "+", formatter.format(num)).substring(1);
	}

	private static String getDamageName(DDDDamageType type, boolean usingIcons) {
		if(DDDAPI.accessor.isPhysicalDamage(type)) {
			if(usingIcons) {
				return "";
			}
			else {
				return damageTypeTooltips.get(type.getTypeName()).getFormattedText();
			}
		}
		return type.getFormattedDisplayName();
	}

	private static String getDamageName(DDDDamageType type) {
		return getDamageName(type, ModConfig.client.useIcons);
	}

	private static int getIndex(DDDDamageType type) {
		int index = 0;
		for(DDDDamageType damageType : DDDBuiltInDamageType.PHYSICAL_TYPES) {
			if(damageType.getTypeName().equals(type.getTypeName())) {
				break;
			}
			index++;
		}
		return ICON_HEIGHT * index;
	}

	/**
	 * Get the icon y locations and type
	 * 
	 * @param stack     stack to check
	 * @param shiftHeld
	 * @param ctrlHeld
	 * @return an iterable of y positions and type (index)
	 */
	public static Iterable<Tuple<Integer, Integer>> getIconsToDraw(ItemStack stack, boolean shiftHeld, boolean ctrlHeld) {
		LinkedList<Tuple<Integer, Integer>> lst = new LinkedList<Tuple<Integer, Integer>>();
		int currY = 21;
		if(stack.isEmpty()) {
			return lst;
		}
		Item item = stack.getItem();
		boolean offsetFlag = DDDConfigurations.items.configured(YResources.getRegistryString(stack)) || ModConfig.client.alwaysShowDamageDistTooltip;
		if(shiftHeld && offsetFlag) {
			IDamageDistribution dist = DDDAPI.accessor.getDamageDistribution(stack);
			currY = addIcons(currY, lst, dist.getCategories());
		}
		currY += offsetFlag ? 11 : 0;
		if(ctrlHeld) {
			IDamageDistribution projDist = DDDConfigurations.projectiles.getFromItemID(YResources.getRegistryString(item));
			IArmorDistribution armorDist = DDDAPI.accessor.getArmorResistances(stack);
			ShieldDistribution shieldDist = DDDAPI.accessor.getShieldDistribution(stack);
			if(projDist != null) {
				addIcons(currY, lst, projDist.getCategories());
			}
			else if(armorDist != null) {
				addIcons(currY, lst, armorDist.getCategories());
			}
			else if(shieldDist != null) {
				addIcons(currY, lst, shieldDist.getCategories());
			}
			else if(item instanceof ItemMonsterPlacer) {
				ItemMonsterPlacer spawnegg = (ItemMonsterPlacer) item;
				ResourceLocation loc = ItemMonsterPlacer.getNamedIdFrom(stack);
				MobResistanceCategories mobCats = DDDConfigurations.mobResists.get(loc.toString());
				if(mobCats != null) {
					addIcons(currY, lst, mobCats.getResistanceMap().keySet());
				}
			}
		}
		return lst;
	}

	public static Iterable<TooltipString> getTooltipStrings(ItemStack stack, boolean ctrlHeld, boolean shiftHeld, int x) {
		LinkedList<TooltipString> strings = new LinkedList<TooltipString>();
		// TODO finish
		return strings;
	}

	private static int addIcons(int currY, LinkedList<Tuple<Integer, Integer>> lst, Iterable<DDDDamageType> types) {
		for(DDDDamageType type : types) {
			if(DDDAPI.accessor.isPhysicalDamage(type)) {
				lst.add(new Tuple<Integer, Integer>(currY, getIndex(type)));
			}
			currY += ICON_HEIGHT;
		}
		return currY;
	}

	private static int addComponents(int x, int currY, LinkedList<TooltipString> lst, IDistribution dist, Function<Float, String> suffix) {
		for(DDDDamageType type : dist.getCategories()) {
			float weight = dist.getWeight(type);
			lst.add(buildTooltipString(x, currY, type, formatNum(weight), suffix.apply(weight), weight < 0 ? 0xaa0000 : 0x555555));
			currY += ICON_HEIGHT;
		}
		return currY;
	}

	private static TooltipString buildTooltipString(int x, int y, DDDDamageType type, String prefix, String suffix, int colour) {
		TooltipString s = new TooltipString(x, y);
		s.add(new TooltipComp(prefix, colour));
		s.add(new TooltipComp(type.getDisplayName(), type.getColour()));
		s.add(new TooltipComp(suffix, colour));
		return s;
	}
}
