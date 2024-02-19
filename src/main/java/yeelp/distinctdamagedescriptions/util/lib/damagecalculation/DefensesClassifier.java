package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

final class DefensesClassifier implements IClassifier<MobDefenses> {

	@Override
	public Optional<MobDefenses> classify(CombatContext context) {
		return DDDAPI.accessor.getMobResistances(context.getDefender()).map((resists) -> {
			Set<DDDDamageType> immunities = DDDRegistries.damageTypes.getAll().stream().filter(resists::hasImmunity).collect(Collectors.toSet());
			ResistMap map = resists.getAllResistances();
			DDDHooks.fireGatherDefenses(context.getImmediateAttacker(), context.getTrueAttacker(), context.getDefender(), context.getSource(), map, immunities);
			return new MobDefenses(map, immunities);
		});
	}
}
