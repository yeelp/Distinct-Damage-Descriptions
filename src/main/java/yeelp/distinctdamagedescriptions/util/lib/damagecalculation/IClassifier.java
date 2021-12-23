package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;
import java.util.function.Function;

interface IClassifier<Output> extends Function<CombatContext, Optional<Output>> {
	
	@Override
	default Optional<Output> apply(CombatContext t) {
		return classify(t);
	}

	Optional<Output> classify(CombatContext context);
}
