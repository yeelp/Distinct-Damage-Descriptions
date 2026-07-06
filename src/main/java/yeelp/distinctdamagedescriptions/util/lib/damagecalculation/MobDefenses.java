package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

import java.util.Set;

@SuppressWarnings("CanBeFinal")
public class MobDefenses {

	public ResistMap resistances;
	public Set<DDDDamageType> immunities;

	public MobDefenses(ResistMap resistances, Set<DDDDamageType> immunities) {
		this.resistances = resistances;
		this.immunities = immunities;
	}

}
