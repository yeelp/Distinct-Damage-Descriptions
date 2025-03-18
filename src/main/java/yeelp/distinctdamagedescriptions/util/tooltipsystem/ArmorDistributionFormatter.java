package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorParsingType;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;
import yeelp.distinctdamagedescriptions.util.lib.YArmor;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.IDDDTooltipInjector.IArmorTooltipInjector;

/**
 * The singleton ArmorDistributionFormatter instance for formatting
 * {@link IArmorDistribution} instances.
 * 
 * @author Yeelp
 *
 */
public class ArmorDistributionFormatter extends AbstractCapabilityTooltipFormatter<IArmorDistribution, ItemStack> {
	
	private static final Set<IArmorTooltipInjector> TOOLTIP_INJECTORS = Sets.newTreeSet();

	private static ArmorDistributionFormatter instance;
	private static final ITextComponent UNCHANGED_ALL_TYPES = getComponentWithWhiteColour("unchangedall");
	private static final ITextComponent UNCHANGED_ALL_OTHER_TYPES = getComponentWithWhiteColour("unchangedallothers");
	private static final ITextComponent ALL_TYPES = getComponentWithWhiteColour("alltypes");
	private static final ITextComponent ALL_OTHER_TYPES = getComponentWithWhiteColour("allothertypes");

	protected ArmorDistributionFormatter() {
		this(DDDAPI.accessor::getArmorResistances, "armorresistances");
	}
	
	protected ArmorDistributionFormatter(Function<ItemStack, Optional<IArmorDistribution>> capExtractor, String typeTextKey) {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, capExtractor, typeTextKey);
	}

	/**
	 * Get the singleton ArmorDistributionFormatter instance, creating a new
	 * instance if it doesn't exist yet.
	 * 
	 * @return The singleton instance, or a new instance if it doesn't exist yet.
	 */
	public static ArmorDistributionFormatter getInstance() {
		return instance == null ? instance = new ArmorDistributionFormatter() : instance;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, IArmorDistribution cap) {
		if(cap.getCategories().isEmpty()) {
			return Optional.of(ImmutableList.of(AbstractCapabilityTooltipFormatter.NONE_TEXT.getFormattedText()));
		}
		if(this.getNumberFormattingStrategy() == ArmorDistributionNumberFormat.PLAIN && stack.getItem() instanceof ItemArmor) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			ArmorValues av = YArmor.getArmorFromStack(stack, armor);
			return this.getArmorTooltip(stack, cap, av.getArmor(), av.getToughness());
		}
		boolean relativeStrat = this.getNumberFormattingStrategy() == ArmorDistributionNumberFormat.RELATIVE;
		return Optional.of((relativeStrat ? DDDRegistries.damageTypes.getAll() : cap.getCategories()).stream().filter(Predicates.not(DDDDamageType::isHidden)).sorted().<List<String>>collect(LinkedList<String>::new, (l, d) -> {
			float weight = cap.getWeight(d);
			if(getFilterPredicate().test(weight)) {
				l.add(TooltipTypeFormatter.ARMOR.format(d, cap.getWeight(d), this));				
			}
		}, List<String>::addAll)).map((l) -> { 
			if(ModConfig.resist.armorParseRule == ArmorParsingType.IMPLIED) {
				if(relativeStrat && ModConfig.resist.impliedArmorEffectiveness == 1.0f) {
					determineTextComponentOffListSize(UNCHANGED_ALL_TYPES, UNCHANGED_ALL_OTHER_TYPES, l).map(ITextComponent::getFormattedText).ifPresent(l::add);
				}
				else {
					determineTextComponentOffListSize(ALL_TYPES, ALL_OTHER_TYPES, l).ifPresent((comp) -> l.add(TooltipTypeFormatter.ARMOR.formatNoType(comp, (float) ModConfig.resist.impliedArmorEffectiveness, this)));
				}
			}
			return l;
		});
	}

	protected final Optional<List<String>> getArmorTooltip(ItemStack stack, IArmorDistribution cap, float armor, float toughness) {
		Iterator<IArmorTooltipInjector> it = TOOLTIP_INJECTORS.stream().filter((injector) -> injector.applies(stack)).iterator();
		ArmorValues av = new ArmorValues(armor, toughness);
		IArmorTooltipInjector injectorToGetFormatterFrom = null;
		while(it.hasNext()) {
			IArmorTooltipInjector injector = it.next();
			av = injector.alterArmorValues(stack, av.getArmor(), av.getToughness());
			if(injector.shouldUseFormatter(stack)) {
				injectorToGetFormatterFrom = injector;
			}
		}
		final ArmorValues original = new ArmorValues(av.getArmor(), av.getToughness());
		final TooltipTypeFormatter.Armor formatter = injectorToGetFormatterFrom == null ? TooltipTypeFormatter.ARMOR : injectorToGetFormatterFrom.getFormatterToUse();
		ArmorMap aMap = cap.distributeArmor(av.getArmor(), av.getToughness());
		ArmorValues baselineValue = new ArmorValues(av.getArmor(), av.getToughness()).mul(ModConfig.resist.armorParseRule == ArmorParsingType.IMPLIED ? (float) ModConfig.resist.impliedArmorEffectiveness : 0.0f);
		Set<DDDDamageType> armorTypes = cap.getCategories();
		return Optional.of(aMap.entrySet().stream().filter((e) -> armorTypes.contains(e.getKey()) && !e.getKey().isHidden() && e.getValue().compareTo(baselineValue) != 0).sorted(Comparator.comparing(Entry<DDDDamageType, ArmorValues>::getKey).thenComparing(Entry::getValue)).collect(LinkedList<String>::new, (l, e) -> l.add(formatter.formatArmorAndToughness(e.getKey(), e.getValue().getArmor(), e.getValue().getToughness(), original.getArmor(), original.getToughness(), this)), LinkedList<String>::addAll)).map((lst) -> {
			switch(ModConfig.resist.armorParseRule) {
				case IMPLIED:
					determineTextComponentOffListSize(ALL_TYPES, ALL_OTHER_TYPES, lst).ifPresent((comp) -> lst.add(formatter.formatArmorAndToughness(comp.getFormattedText(), baselineValue.getArmor(), baselineValue.getToughness(), original.getArmor(), original.getToughness(), this)));
					break;
				default:
					if(lst.isEmpty()) {
						lst.add(NONE_TEXT.getFormattedText());						
					}
					break;
			}
			return lst;
		});
	}
	
	private static final Optional<ITextComponent> determineTextComponentOffListSize(ITextComponent empty, ITextComponent notFull, List<String> lst) {
		ITextComponent comp = null;
		if(lst.isEmpty()) {
			comp = empty;
		}
		else if(lst.size() < DDDRegistries.damageTypes.getUsableTypeCount()) {
			comp = notFull;
		}
		return Optional.ofNullable(comp);
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.ARMOR;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return ModConfig.client.armorFormat;
	}

	@Override
	public DDDTooltipColourScheme getColourScheme() {
		return ModConfig.client.armorColourScheme;
	}
	
	private static Predicate<Float> getFilterPredicate() {
		return ModConfig.client.armorFormat;
	}
	
	static void registerTooltipInjector(IArmorTooltipInjector injector) {
		TOOLTIP_INJECTORS.add(injector);
	}
}