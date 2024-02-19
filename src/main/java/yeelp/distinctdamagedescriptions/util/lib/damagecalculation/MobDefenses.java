package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Set;

import com.google.common.collect.Sets;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

class MobDefenses {

	public ResistMap resistances;
	public Set<DDDDamageType> immunities;

	public MobDefenses() {
		this(DDDMaps.newResistMap(), Sets.newHashSet());
	}

	public MobDefenses(ResistMap resistances, Set<DDDDamageType> immunities) {
		this.resistances = resistances;
		this.immunities = immunities;
	}

}
