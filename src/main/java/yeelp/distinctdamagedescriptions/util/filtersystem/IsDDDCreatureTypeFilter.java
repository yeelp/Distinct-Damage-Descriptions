package yeelp.distinctdamagedescriptions.util.filtersystem;

import yeelp.distinctdamagedescriptions.api.DDDAPI;

public final class IsDDDCreatureTypeFilter extends SimpleFilterOperation {

	public IsDDDCreatureTypeFilter(String creatureType) {
		super((e) -> DDDAPI.accessor.getMobCreatureType(e).filter((ct) -> ct.isCreatureType(creatureType)).isPresent());
	}

}
