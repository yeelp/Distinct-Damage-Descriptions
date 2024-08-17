package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Comparator;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.google.common.base.Predicates;

public enum ArmorCalculationType {
	ADD {
		@Override
		public ArmorValues mergeAfterFilter(Stream<ArmorValues> aVals) {
			return aVals.reduce(new ArmorValues(), (av1, av2) -> av1.setValues(av1.getArmor() + av2.getArmor(), av1.getToughness() + av2.getToughness()));
		}
	},
	MAX {
		@Override
		public ArmorValues mergeAfterFilter(Stream<ArmorValues> aVals) {
			return aVals.max(Comparator.naturalOrder()).orElse(new ArmorValues());
		}
	},
	AVG {
		@Override
		public ArmorValues mergeAfterFilter(Stream<ArmorValues> aVals) {
			DoubleStream.Builder armors = DoubleStream.builder();
			DoubleStream.Builder toughness = DoubleStream.builder();			
			aVals.forEach((av) -> {
				armors.add(av.getArmor());
				toughness.add(av.getToughness());
			});
			return new ArmorValues((float) armors.build().average().orElse(0.0), (float) toughness.build().average().orElse(0.0));
		}
	};
	
	/**
	 * Perform stream reduction on a stream of ArmorValues.
	 * @param aVals a Stream of ArmorValues
	 * @return a reduced ArmorValues according to the ArmorCalculationType.
	 */
	public ArmorValues merge(Stream<ArmorValues> aVals) {
		return this.mergeAfterFilter(aVals.filter(Predicates.notNull()));
	}
	
	protected abstract ArmorValues mergeAfterFilter(Stream<ArmorValues> aVals);
	
}
