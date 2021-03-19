package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.HashMap;
import java.util.Map;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.IDDDDamageTypeRegistry;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DDDDamageTypes extends DDDBaseRegistry<DDDDamageType> implements IDDDDamageTypeRegistry
{
	public DDDDamageTypes()
	{
		super(d -> d.getTypeName(), "Damage Type");
	}
	
	@Override
	public void init()
	{
		this.registerAll(DDDBuiltInDamageType.ACID, 
						 DDDBuiltInDamageType.BLUDGEONING, 
						 DDDBuiltInDamageType.COLD, 
						 DDDBuiltInDamageType.FIRE, 
						 DDDBuiltInDamageType.FORCE, 
						 DDDBuiltInDamageType.LIGHTNING, 
						 DDDBuiltInDamageType.NECROTIC, 
						 DDDBuiltInDamageType.NORMAL, 
						 DDDBuiltInDamageType.PIERCING, 
						 DDDBuiltInDamageType.POISON, 
						 DDDBuiltInDamageType.PSYCHIC, 
						 DDDBuiltInDamageType.RADIANT, 
						 DDDBuiltInDamageType.SLASHING, 
						 DDDBuiltInDamageType.THUNDER, 
						 DDDBuiltInDamageType.UNKNOWN);
	}
}