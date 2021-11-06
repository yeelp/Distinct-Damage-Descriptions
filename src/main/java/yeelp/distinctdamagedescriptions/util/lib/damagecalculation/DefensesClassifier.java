package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraftforge.common.MinecraftForge;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ResistMap;

final class DefensesClassifier implements IClassifier<MobDefenses> {

	@Override
	public Optional<MobDefenses> classify(CombatContext context) {
		IMobResistances resists = DDDAPI.accessor.getMobResistances(context.getDefender());
		if(resists != null) {
			Set<DDDDamageType> immunities = DDDRegistries.damageTypes.getAll().stream().filter(resists::hasImmunity).collect(Collectors.toSet());
			ResistMap map = resists.getAllResistances();
			MinecraftForge.EVENT_BUS.post(new GatherDefensesEvent(context.getImmediateAttacker(), context.getSource().getTrueSource(), context.getDefender(), context.getSource(), map, immunities));
			return Optional.of(new MobDefenses(map, immunities));
		}
		return Optional.empty();
	}
}
