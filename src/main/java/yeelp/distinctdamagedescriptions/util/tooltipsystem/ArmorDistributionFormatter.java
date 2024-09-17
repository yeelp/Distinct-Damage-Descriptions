package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;

/**
 * The singleton ArmorDistributionFormatter instance for formatting
 * {@link IArmorDistribution} instances.
 * 
 * @author Yeelp
 *
 */
public class ArmorDistributionFormatter extends AbstractCapabilityTooltipFormatter<IArmorDistribution, ItemStack> {

	private static ArmorDistributionFormatter instance;
	private static final ITextComponent UNCHANGED = getComponentWithWhiteColour("unchanged");

	protected ArmorDistributionFormatter() {
		this(DDDAPI.accessor::getArmorResistances, "armorresistances");
	}
	
	protected ArmorDistributionFormatter(Function<ItemStack, Optional<IArmorDistribution>> capExtractor, String typeTextKey ) {
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
		if(this.getNumberFormattingStrategy() == ArmorDistributionNumberFormat.PLAIN) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			return this.getArmorTooltip(cap, armor.damageReduceAmount, armor.toughness);
		}
		boolean relativeStrat = this.getNumberFormattingStrategy() == ArmorDistributionNumberFormat.RELATIVE;
		return Optional.of((relativeStrat ? DDDRegistries.damageTypes.getAll() : cap.getCategories()).stream().filter(Predicates.not(DDDDamageType::isHidden)).sorted().<List<String>>collect(LinkedList<String>::new, (l, d) -> {
			float weight = cap.getWeight(d);
			if(getFilterPredicate().test(weight)) {
				l.add(TooltipTypeFormatter.ARMOR.format(d, cap.getWeight(d), this));				
			}
		}, List<String>::addAll)).map((l) -> { 
			if(relativeStrat && l.isEmpty()) {
				return ImmutableList.of(UNCHANGED.getFormattedText()); 
			}
			return l;
		});
	}

	protected Optional<List<String>> getArmorTooltip(IArmorDistribution cap, float armor, float toughness) {
		ArmorMap aMap = cap.distributeArmor(armor, toughness);
		return Optional.of(aMap.entrySet().stream().filter((e) -> !e.getKey().isHidden() && e.getValue().compareTo(ArmorValues.ZERO) > 0).sorted(Comparator.comparing(Entry<DDDDamageType, ArmorValues>::getKey).thenComparing(Entry::getValue)).collect(LinkedList<String>::new, (l, e) -> l.add(TooltipTypeFormatter.ARMOR.formatArmorAndToughness(e.getKey(), e.getValue().getArmor(), e.getValue().getToughness(), this)), LinkedList<String>::addAll));
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
}