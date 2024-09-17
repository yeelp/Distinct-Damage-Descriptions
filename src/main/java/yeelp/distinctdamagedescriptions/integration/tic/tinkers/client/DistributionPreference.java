package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.util.Tuple;
import slimeknights.tconstruct.library.tools.ToolCore;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

enum DistributionPreference implements IEnumTranslation {
	ONLY {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, toolName, strongType.getDisplayName());
		}
	},
	STRONGLY {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, toolName, strongType.getDisplayName());
		}
	},
	MODERATELY {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, toolName, strongType.getDisplayName());
		}
	},
	BARELY {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, barelyType.getDisplayName());
		}
	},
	BARELY_TWO {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, moderateType.getDisplayName(), barelyType.getDisplayName());
		}
	},
	BALANCED {
		@Override
		protected IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType) {
			return new EnumBookString<DistributionPreference>(this, toolName, strongType.getDisplayName(), moderateType.getDisplayName());
		}
	};

	@SuppressWarnings("incomplete-switch")
	static Tuple<IBookString, Optional<IBookString>> determinePreferences(ToolCore tool, IDamageDistribution dist) {
		// sort on distribution weight reversed, which will give the highest weighted
		// type as the first element
		DamageMap map = dist.distributeDamage(1.0f);
		DDDMaps.adjustHiddenWeightsToUnknown(map);
		List<DDDDamageType> topThreeTypes = map.keySet().stream().sequential().sorted(Comparator.comparingDouble(map::get).reversed()).limit(3).collect(Collectors.toList());
		DistributionPreference first, second = null;
		String toolName = tool.getLocalizedToolName();
		DDDDamageType firstType = getFromListIfInBounds(topThreeTypes, 0).get(); // this is safe, distribution must
																					// have at least one type.
		Optional<DDDDamageType> secondType = getFromListIfInBounds(topThreeTypes, 1);
		Optional<DDDDamageType> thirdType = getFromListIfInBounds(topThreeTypes, 2);
		if(secondType.isPresent()) {
			boolean balanced = Math.abs(dist.getWeight(firstType) - secondType.map(dist::getWeight).orElse(0.0f)) < 0.2;
			if(balanced) {
				first = BALANCED;
				if(thirdType.isPresent()) {
					second = BARELY;
				}
			}
			else {
				second = thirdType.isPresent() ? BARELY_TWO : BARELY;
				if(dist.getWeight(firstType) > 0.7) {
					first = STRONGLY;
				}
				else {
					first = MODERATELY;
				}
			}
		}
		else {
			first = ONLY;
		}
		IBookString firstString = null;
		Optional<IBookString> secondString = Optional.empty();
		switch(topThreeTypes.size()) {
			case 1:
				firstString = first.convertToBookString(toolName, firstType, firstType, firstType);
				break;
			case 2:
				DDDDamageType type = secondType.get();
				firstString = first.convertToBookString(toolName, firstType, type, type);
				secondString = Optional.ofNullable(second).map((d) -> d.convertToBookString(toolName, firstType, type, type));
				break;
			case 3:
				DDDDamageType extraType1 = secondType.get(), extraType2 = thirdType.get();
				firstString = first.convertToBookString(toolName, firstType, extraType1, extraType2);
				secondString = Optional.of(second).map((d) -> d.convertToBookString(toolName, firstType, extraType1, extraType2));
				break;
		}
		return new Tuple<IBookString, Optional<IBookString>>(Objects.requireNonNull(firstString), secondString);
	}

	private static <T> Optional<T> getFromListIfInBounds(List<T> lst, int index) {
		if(index < lst.size()) {
			return Optional.of(lst.get(index));
		}
		return Optional.empty();
	}

	protected abstract IBookString convertToBookString(String toolName, DDDDamageType strongType, DDDDamageType moderateType, DDDDamageType barelyType);

	@Override
	public String getRootString() {
		return "distributions.favoured";
	}
}
