package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.util.DamageTypeData;

public interface IDDDDamageTypeRegistry extends IDDDRegistry
{
	void registerDamageType(String name, String fromType, DamageTypeData...datas);
}
