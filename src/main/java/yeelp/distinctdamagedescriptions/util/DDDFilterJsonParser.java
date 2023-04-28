package yeelp.distinctdamagedescriptions.util;

import java.util.Set;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import yeelp.distinctdamagedescriptions.util.filtersystem.FilterApplicationType;
import yeelp.distinctdamagedescriptions.util.filtersystem.FilterOperation;
import yeelp.distinctdamagedescriptions.util.filtersystem.IDDDFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.InDimensionFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.IsBelowYLevelFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.IsDDDCreatureTypeFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.IsVanillaCreatureAttributeFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.IsVanillaCreatureTypeFilter;
import yeelp.distinctdamagedescriptions.util.filtersystem.SimpleFilterOperation;
import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

public class DDDFilterJsonParser extends AbstractJsonParser<IDDDFilter> {

	protected DDDFilterJsonParser(JsonObject root) {
		super(root);
	}

	@Override
	public IDDDFilter parseJson() {
		FilterApplicationType appType = this.getApplicationType();
		IDDDFilter.FilterCombinationMethod comboMethod = IDDDFilter.FilterCombinationMethod.valueOf(this.getString("filter_combination_method").toUpperCase().trim());
		Set<AbstractJsonParser<FilterOperation>> ops = this.getArrayOfNestedObjects("filters", FilterOperationJsonParser::getParser);
		return null;
	}

	private FilterApplicationType getApplicationType() {
		switch(this.getString("application_type")) {
			case "base":
				return FilterApplicationType.BASE;
			case "modifier":
				switch(this.getString("resistance_combination_type")) {
					case "add":
						return FilterApplicationType.ADD;
					case "multiply":
						return FilterApplicationType.MULTIPLY;
					case "percent_multiply":
					case "multiply_percent":
						return FilterApplicationType.MULTIPLY_PERCENT;
					default:
						throw new SyntaxException("Invalid resistance_combination_type");
				}
			default:
				throw new SyntaxException("Invalid application_type");
		}
	}

	private static abstract class FilterOperationJsonParser extends AbstractJsonParser<FilterOperation> {

		protected FilterOperationJsonParser(JsonObject root) {
			super(root);
		}

		static FilterOperationJsonParser getParser(JsonObject root) {
			switch(root.get("type").getAsString()) {
				case "isBoss":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_BOSS);
				case "isMonster":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_MONSTER);
				case "isAquatic":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_AQUATIC);
				case "isAnimal":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_ANIMAL);
				case "isAmbient":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_AMBIENT);
				case "isUndead":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_UNDEAD);
				case "isArthopod":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_ARTHOPOD);
				case "isIllager":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_ILLAGER);
				case "isUndefined":
					return new SimpleFilterParser(root, SimpleFilterParser.FilterOptions.IS_UNDEFINED);
				case "dimension":
					return new DimensionFilterParser(root);
				case "isBelowYLevel":
					return new BelowYLevelParser(root);
				case "isDDDCreatureType":
					return new IsDDDCreatureTypeParser(root);

				// TODO: Add deep search true/false keys to hasNBT filter type metadata, note
				// that if searching all keys in NBT structure, some keys may appear more than
				// once
				default:
					throw new SyntaxException("Invalid filter type");
			}
		}

		protected boolean shouldNegate() {
			return this.getBoolean("negate");
		}

		protected abstract FilterOperation parseJsonMetadata();

		@Override
		public FilterOperation parseJson() {
			FilterOperation op = this.parseJsonMetadata();
			op.setNegateStatus(this.shouldNegate());
			return op;
		}

		private static final class SimpleFilterParser extends FilterOperationJsonParser {

			private enum FilterOptions {
				IS_BOSS(SimpleFilterOperation.IsBossFilter::new),
				IS_MONSTER(IsVanillaCreatureTypeFilter.IsMonsterCreatureType::new),
				IS_AQUATIC(IsVanillaCreatureTypeFilter.IsWaterCreatureType::new),
				IS_ANIMAL(IsVanillaCreatureTypeFilter.IsAnimalCreatureType::new),
				IS_AMBIENT(IsVanillaCreatureTypeFilter.IsAmbientCreatureType::new),
				IS_ARTHOPOD(IsVanillaCreatureAttributeFilter.IsArthopodMob::new),
				IS_ILLAGER(IsVanillaCreatureAttributeFilter.IsIllagerMob::new),
				IS_UNDEAD(IsVanillaCreatureAttributeFilter.IsUndeadMob::new),
				IS_UNDEFINED(IsVanillaCreatureAttributeFilter.IsUndefinedMob::new);

				private final Supplier<? extends FilterOperation> sup;

				private FilterOptions(Supplier<? extends FilterOperation> sup) {
					this.sup = sup;
				}

				FilterOperation getOp() {
					return this.sup.get();
				}
			}

			private final FilterOptions option;

			protected SimpleFilterParser(JsonObject root, FilterOptions option) {
				super(root);
				this.option = option;
			}

			@Override
			public FilterOperation parseJsonMetadata() {
				return this.option.getOp();
			}

		}

		private static final class DimensionFilterParser extends FilterOperationJsonParser {

			protected DimensionFilterParser(JsonObject root) {
				super(root);
			}

			@Override
			protected FilterOperation parseJsonMetadata() {
				return new InDimensionFilter(this.getInt("metadata.id"));
			}

		}

		private static final class BelowYLevelParser extends FilterOperationJsonParser {

			protected BelowYLevelParser(JsonObject root) {
				super(root);
			}

			@Override
			protected FilterOperation parseJsonMetadata() {
				return new IsBelowYLevelFilter(this.getInt("metadata.y_level"));
			}

		}

		private static final class IsDDDCreatureTypeParser extends FilterOperationJsonParser {

			protected IsDDDCreatureTypeParser(JsonObject root) {
				super(root);
			}

			@Override
			protected FilterOperation parseJsonMetadata() {
				return new IsDDDCreatureTypeFilter(this.getString("metadata.type_name"));
			}

		}
	}

}
