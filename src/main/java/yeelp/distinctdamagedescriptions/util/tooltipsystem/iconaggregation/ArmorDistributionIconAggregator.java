package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorParsingType;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionNumberFormat;

/**
 * An icon aggregator for IArmorDistributions
 * 
 * @author Yeelp
 *
 */
public class ArmorDistributionIconAggregator extends DistributionIconAggregator<IArmorDistribution> {

	private static ArmorDistributionIconAggregator instance;
	
	private enum IconAggregatingType implements Predicate<Float> {
		RELATIVE(ArmorDistributionNumberFormat.RELATIVE) {
			@Override
			public boolean test(Float t) {
				return ArmorDistributionNumberFormat.RELATIVE.test(t);
			}

			@Override
			boolean hasEdgeCases() {
				return true;
			}

			@Override
			Optional<Stream<DDDDamageType>> handleEdgeCase(IArmorDistribution dist) {
				return Optional.ofNullable(dist.getCategories().isEmpty() ? Stream.empty() : null);
			}

			@Override
			Collection<DDDDamageType> getStartingTypesToAggregate(ItemStack stack, IArmorDistribution dist) {
				return DDDRegistries.damageTypes.getAll();
			}
		},
		PERCENT(ArmorDistributionNumberFormat.PERCENT) {
			@Override
			public boolean test(Float t) {
				return ArmorDistributionNumberFormat.PERCENT.test(t);
			}

			@Override
			boolean hasEdgeCases() {
				return false;
			}

			@Override
			Optional<Stream<DDDDamageType>> handleEdgeCase(IArmorDistribution dist) {
				return Optional.empty();
			}

			@Override
			Collection<DDDDamageType> getStartingTypesToAggregate(ItemStack stack, IArmorDistribution dist) {
				return dist.getCategories();
			}
		},
		PLAIN(ArmorDistributionNumberFormat.PLAIN) {
			@Override
			public boolean test(Float t) {
				return true;
			}
			
			@Override
			boolean hasEdgeCases() {
				return false;
			}
			
			@Override
			Optional<Stream<DDDDamageType>> handleEdgeCase(IArmorDistribution dist) {
				return Optional.empty();
			}
			
			@Override
			Collection<DDDDamageType> getStartingTypesToAggregate(ItemStack stack, IArmorDistribution dist) {
				double baseline = ModConfig.resist.armorParseRule == ArmorParsingType.IMPLIED ? ModConfig.resist.impliedArmorEffectiveness : 0.0;
				return dist.getCategories().stream().filter((type) -> dist.getWeight(type) != baseline).collect(Collectors.toList());
			}
		},
		DEFAULT {
			@Override
			public boolean test(Float t) {
				return true;
			}

			@Override
			boolean hasEdgeCases() {
				return false;
			}

			@Override
			Optional<Stream<DDDDamageType>> handleEdgeCase(IArmorDistribution dist) {
				return Optional.empty();
			}

			@Override
			Collection<DDDDamageType> getStartingTypesToAggregate(ItemStack stack, IArmorDistribution dist) {
				return dist.getCategories();
			}
		};
		
		private final EnumSet<ArmorDistributionNumberFormat> formats;
		private IconAggregatingType(ArmorDistributionNumberFormat format) {
			this.formats = EnumSet.of(format);
		}
		
		private IconAggregatingType(IconAggregatingType type) {
			this.formats = EnumSet.complementOf(type.formats);
		}
		
		private IconAggregatingType() {
			this.formats = EnumSet.noneOf(ArmorDistributionNumberFormat.class);
		}
		
		static IconAggregatingType getType() {
			for(IconAggregatingType type : IconAggregatingType.values()) {
				if(type.formats.contains(ModConfig.client.armorFormat)) {
					return type;
				}
			}
			return DEFAULT;
		}
		
		abstract boolean hasEdgeCases();
		
		abstract Optional<Stream<DDDDamageType>> handleEdgeCase(IArmorDistribution dist);
		
		abstract Collection<DDDDamageType> getStartingTypesToAggregate(ItemStack stack, IArmorDistribution dist);
	}

	protected ArmorDistributionIconAggregator() {
		this(ArmorDistributionFormatter.getInstance(), DDDAPI.accessor::getArmorResistances);
	}
	
	protected ArmorDistributionIconAggregator(AbstractCapabilityTooltipFormatter<IArmorDistribution, ItemStack> formatter, Function<ItemStack, Optional<IArmorDistribution>> capExtractor) {
		super(formatter, capExtractor);
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static ArmorDistributionIconAggregator getInstance() {
		return instance == null ? instance = new ArmorDistributionIconAggregator() : instance;
	}
	
	@Override
	protected final Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		IconAggregatingType type = IconAggregatingType.getType();
		Optional<IArmorDistribution> cap = this.getCap(stack);
		if(type.hasEdgeCases()) {
			Optional<Stream<DDDDamageType>> result = cap.flatMap(type::handleEdgeCase);
			if(result.isPresent()) {
				return result.get();
			}
		}
		return cap.map((a) -> type.getStartingTypesToAggregate(stack, a).stream().filter((t) -> !t.isHidden() && type.test(a.getWeight(t))).sorted()).orElse(Stream.empty());
	}
	
	@Override
	protected boolean shouldKeepUnknown() {
		return false;
	}
}
