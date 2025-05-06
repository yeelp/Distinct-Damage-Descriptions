package yeelp.distinctdamagedescriptions.integration.hwyla.client;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla.HwylaConsts;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.LayeredTranslator;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;
import yeelp.distinctdamagedescriptions.util.lib.YArmor;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.IDDDTooltipInjector.IArmorTooltipInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;

public class HwylaMobResistanceFormatter extends HwylaTooltipFormatter<IMobResistances> {

	private static HwylaMobResistanceFormatter instance;
	private static final LayeredTranslator TRANSLATOR = Translations.INSTANCE.getLayeredTranslator(TooltipConsts.TOOLTIPS_ROOT, HwylaConsts.HWYLA);
	private static final Predicate<DDDDamageType> SHOULD_TYPE_APPEAR = Predicates.not(Predicates.or(DDDDamageType::isHidden, DDDDamageType::isInternalType));
	private static final Set<IArmorTooltipInjector> ARMOR_TOOLTIP_INJECTORS = Sets.newTreeSet();
	
	private static final Style WHITE = new Style().setColor(TextFormatting.WHITE),
			AQUA = new Style().setColor(TextFormatting.AQUA),
			LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE);

	private final ITextComponent noResists = AbstractCapabilityTooltipFormatter.getComponentWithStyle(TooltipConsts.NO_RESISTS, WHITE),
			immunityPrefix = TRANSLATOR.getComponent(TooltipConsts.IMMUNITIES).setStyle(AQUA),
			adaptabilityAmountPrefix = AbstractCapabilityTooltipFormatter.getComponentWithStyle(TooltipConsts.ADAPTABILITY_AMOUNT, LIGHT_PURPLE),
			allTypes = TRANSLATOR.getComponent(HwylaConsts.ALL_TYPES).setStyle(WHITE),
			allOtherTypes = TRANSLATOR.getComponent(HwylaConsts.ALL_OTHER_TYPES).setStyle(WHITE),
			adaptedTo = TRANSLATOR.getComponent(HwylaConsts.ADAPTED_TO).setStyle(LIGHT_PURPLE),
			resistanceSuffix = AbstractCapabilityTooltipFormatter.getComponentWithGrayColour(TooltipConsts.RESISTANCE),
			maxPrefix = TRANSLATOR.getComponent(HwylaConsts.MAX).setStyle(new Style().setColor(TextFormatting.GRAY));
	
	private NBTTagCompound nbtFromServer = null;

	private HwylaMobResistanceFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getMobResistances, "hwyla.mobresistances");
	}
	
	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static HwylaMobResistanceFormatter getInstance() {
		return instance == null ? instance = new HwylaMobResistanceFormatter() : instance;
	}
	
	List<String> format(EntityLivingBase t, NBTTagCompound nbtFromServer) {
		this.nbtFromServer = nbtFromServer;
		return this.format(t);
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(EntityLivingBase t, IMobResistances cap) {
		if(t == null || cap == null) {
			return Optional.empty();
		}
		IMobResistances updatedCap = this.nbtFromServer == null ? cap : this.getResistances();
		List<String> result = new LinkedList<String>();
		Set<DDDDamageType> immunities = Sets.newHashSet();
		Set<DDDDamageType> armorTypesToFormat = Sets.newHashSet();
		ArmorMap aMap = DDDMaps.newArmorMap();
		ArmorValues runningTotal = new ArmorValues();
		ModConsts.ARMOR_SLOTS_ITERABLE.forEach((slot) -> {
			ItemStack stack = t.getItemStackFromSlot(slot);
			if(stack.isEmpty()) {
				return;
			}
			DDDAPI.accessor.getArmorResistances(stack).ifPresent((armorDist) -> {
				ArmorValues av = YArmor.getArmorFromStack(stack, slot);
				Iterator<IArmorTooltipInjector> it = ARMOR_TOOLTIP_INJECTORS.stream().filter((injector) -> injector.applies(stack)).iterator();
				while(it.hasNext()) {
					av = it.next().alterArmorValues(stack, av.getArmor(), av.getToughness());
				}
				runningTotal.add(av);
				determineArmorToFormat(armorTypesToFormat, armorDist);
				armorDist.distributeArmor(av.getArmor(), av.getToughness()).forEach((type, armorValues) -> aMap.compute(type, (k, v) -> ArmorValues.merge(v, armorValues)));
			});
		});
		ResistMap rMap = updatedCap.getAllResistancesCopy();
		boolean shouldAddAllOtherTypesLine = false;
		ArmorValues armorValue = ArmorValues.ZERO;
		float naturalArmor = Math.max((float) t.getAttributeMap().getAttributeInstance(DDDAttributeModifierCollections.ArmorModifiers.ARMOR.getAttribute()).getAttributeValue() - runningTotal.getArmor(), 0);
		for(Iterator<DDDDamageType> it = DDDRegistries.damageTypes.getAll().stream().sorted(Comparator.<DDDDamageType>comparingDouble((d) -> rMap.get(d)).thenComparing(Comparator.naturalOrder())).filter(SHOULD_TYPE_APPEAR).iterator(); it.hasNext();) {
			DDDDamageType type = it.next();
			if(updatedCap.hasImmunity(type)) {
				immunities.add(type);
			}
			if(shouldFormatResistance(type, rMap, armorTypesToFormat)) {
				String s = TooltipTypeFormatter.MOB_RESISTS.format(type, rMap.get(type), this);				
				if(aMap.get(type).compareTo(ArmorValues.ZERO) != 0 || naturalArmor != 0) {
					result.add(s.concat(", ").concat(this.formatWithArmor(rMap.get(type), aMap.get(type).getArmor() + naturalArmor)));
				}
				else {
					result.add(s);
				}
			}
			//this is for implied armor effectiveness
			else if(!shouldAddAllOtherTypesLine && aMap.get(type).compareTo(ArmorValues.ZERO) != 0) {
				shouldAddAllOtherTypesLine = true;
				armorValue = aMap.get(type);
			}
		}
		Optional<ITextComponent> comp = Optional.empty();
		if(result.isEmpty()) {
			if(shouldAddAllOtherTypesLine) {
				comp = Optional.of(this.allTypes);
			}
			else {
				result.add(this.noResists.getFormattedText());
			}
		}
		else if(shouldAddAllOtherTypesLine) {
			comp = Optional.of(this.allOtherTypes);
		}
		float armor = armorValue.getArmor();
		comp.map(ITextComponent::getFormattedText).ifPresent((c) -> result.add(String.format("   %s %s0%%%s %s, %s", c, this.getColourScheme().getFormattingBasedOnValue(0, 0), TextFormatting.RESET.toString(), this.resistanceSuffix.getFormattedText(), this.formatWithArmor(0, armor))));
		makeImmunityString(immunities).ifPresent(result::add);
		if(updatedCap.hasAdaptiveResistance()) {
			result.add(String.format("   %s %s", this.adaptabilityAmountPrefix.getFormattedText(), this.getNumberFormattingStrategy().format(updatedCap.getAdaptiveAmount())));
			Set<DDDDamageType> adaptedTypes = DDDRegistries.damageTypes.getAll().stream().filter(updatedCap::isAdaptiveTo).collect(Collectors.toSet());
			if(!adaptedTypes.isEmpty()) {
				result.add(String.format("   %s %s", this.adaptedTo.getFormattedText(), YLib.joinNiceString(true, ",", adaptedTypes.stream().sorted().map(DDDDamageFormatter.COLOURED::format).toArray(String[]::new))));
			}
		}
		return Optional.of(result);

	}

	private Optional<String> makeImmunityString(Set<DDDDamageType> immunities) {
		if(immunities.isEmpty()) {
			return Optional.empty();
		}
		String str = this.immunityPrefix.getFormattedText() + " ";
		String[] strings = new String[immunities.size()];
		strings = immunities.stream().sorted().map(DDDDamageFormatter.COLOURED::format).collect(Collectors.toList()).toArray(strings);
		return Optional.of(str + YLib.joinNiceString(true, ",", strings));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_RESISTANCES;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}
	
	@Override
	public DDDTooltipColourScheme getColourScheme() {
		return ModConfig.client.mobResistColourScheme;
	}
	
	private static boolean shouldFormatResistance(DDDDamageType type, ResistMap resists, Set<DDDDamageType> hasArmorToFormat) {
		return resists.get(type) != 0 || hasArmorToFormat.contains(type);
	}
	
	private static void determineArmorToFormat(Set<DDDDamageType> currentTypesToFormat, IArmorDistribution armor) {
		armor.getCategories().forEach((type) -> {
			if(!ModConfig.resist.armorParseRule.isDefaultValue(armor.getWeight(type))) {
				currentTypesToFormat.add(type);
			}
		});
	}
	
	private static float getCombinedArmorEffectivenessRating(float resistance, float armor) {
		return 1 - (1 - resistance) * (1 - armor/100.0f);
	}
	
	private String formatWithArmor(float resistance, float armor) {
		return String.format("   %s %s%s%s %s", this.maxPrefix.getFormattedText(), ModConfig.client.armorColourScheme.getFormattingBasedOnValue(resistance, 0.0f), this.getNumberFormattingStrategy().format(getCombinedArmorEffectivenessRating(resistance, armor)), TextFormatting.GRAY, TRANSLATOR.translate(HwylaConsts.WITH_ARMOR));
	}
	
	private IMobResistances getResistances() {
		IMobResistances r = new MobResistances();
		r.deserializeNBT(this.nbtFromServer);
		return r;
	}
	
	static void registerArmorTooltipInjector(IArmorTooltipInjector injector) {
		ARMOR_TOOLTIP_INJECTORS.add(injector);
	}
}
