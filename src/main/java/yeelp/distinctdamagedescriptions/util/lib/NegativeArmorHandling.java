package yeelp.distinctdamagedescriptions.util.lib;

public enum NegativeArmorHandling {
	DEFAULT {
		@Override
		public ArmorValues handlePotentialNegativeArmorValues(ArmorValues av) {
			return av;
		}
	},
	ABS {
		@Override
		public final ArmorValues handlePotentialNegativeArmorValues(ArmorValues av) {
			return av.abs();
		}
	};
	
	public abstract ArmorValues handlePotentialNegativeArmorValues(ArmorValues av);
}
