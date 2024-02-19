package yeelp.distinctdamagedescriptions.registries.impl;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.IDDDDamageTypeRegistry;

public final class DDDDamageTypes extends DDDSourcedRegistry<DDDDamageType> implements IDDDDamageTypeRegistry {
	public DDDDamageTypes() {
		super(d -> d.getTypeName(), "Damage Type");
	}

	@Override
	public void init() {
		this.registerAll(DDDBuiltInDamageType.ACID, DDDBuiltInDamageType.BLUDGEONING, DDDBuiltInDamageType.COLD, DDDBuiltInDamageType.FIRE, DDDBuiltInDamageType.FORCE, DDDBuiltInDamageType.LIGHTNING, DDDBuiltInDamageType.NECROTIC, DDDBuiltInDamageType.NORMAL, DDDBuiltInDamageType.PIERCING, DDDBuiltInDamageType.POISON, DDDBuiltInDamageType.PSYCHIC, DDDBuiltInDamageType.RADIANT, DDDBuiltInDamageType.SLASHING, DDDBuiltInDamageType.THUNDER, DDDBuiltInDamageType.UNKNOWN);
	}
}
