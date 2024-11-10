package yeelp.distinctdamagedescriptions.util.lib;

import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;

public enum ShieldFireRule {
	ALWAYS {
		@Override
		public int alterFireTicks(int ticks, ShieldDistribution dist, CombatResults results) {
			return ticks;
		}
	},
	VANILLA {
		@Override
		public int alterFireTicks(int ticks, ShieldDistribution dist, CombatResults results) {
			return 0;
		}
	},
	BLOCKED_THRESHOLD {
		@Override
		public int alterFireTicks(int ticks, ShieldDistribution dist, CombatResults results) {
			double ratio = results.getShieldRatio().orElse(0);
			DebugLib.outputFormattedDebug("Shield Ratio: %f", ratio);
			return Math.max(0, (int) ((1 - ratio) * ticks));
		}
	},
	FIRE_THRESHOLD {
		@Override
		public int alterFireTicks(int ticks, ShieldDistribution dist, CombatResults results) {
			float fire = dist.getWeight(DDDBuiltInDamageType.FIRE);
			DebugLib.outputFormattedDebug("Fire Weight: %f", fire);
			return Math.max(0, (int) ((1 - fire) * ticks));
		}
	};
	
	public abstract int alterFireTicks(int ticks, ShieldDistribution dist, CombatResults results);
}
