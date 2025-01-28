package yeelp.distinctdamagedescriptions.util.lib;

import java.util.function.UnaryOperator;

import com.google.common.base.Predicates;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

/**
 * Parsing type for Armor configs
 * @author Yeelp
 *
 */
public enum ArmorParsingType implements UnaryOperator<IArmorDistribution> {
	LITERAL {
		@Override
		public IArmorDistribution apply(IArmorDistribution t) {
			return t;
		}
		
		@Override
		public boolean isDefaultValue(float f) {
			return f == 0.0f;
		}
	},
	IMPLIED {
		//Boxed so we can differentiate uninitialized
		Float cachedImpliedEffectiveness = null;
		@Override
		public IArmorDistribution apply(IArmorDistribution t) {
			DDDBaseMap<Float> configured = t.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, t::getWeight));
			DistinctDamageDescriptions.debug(configured.toString());
			DDDBaseMap<Float> unconfigured = DDDRegistries.damageTypes.getAll().stream().filter(Predicates.not(t.getCategories()::contains)).collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, (type) -> this.getImpliedEffectiveness()));
			configured.putAll(unconfigured);
			t.setNewWeights(configured);
			return t;
		}
		
		@Override
		public boolean isDefaultValue(float f) {
			return f == this.getImpliedEffectiveness();
		}
		
		private float getImpliedEffectiveness() {
			return this.cachedImpliedEffectiveness == null ? this.cachedImpliedEffectiveness = (float) ModConfig.resist.impliedArmorEffectiveness : this.cachedImpliedEffectiveness;
		}
	};
	
	public abstract boolean isDefaultValue(float f);
}
