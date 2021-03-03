package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.registries.impl.DDDCreatureTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.util.DDDJsonIO;

public abstract class DDDRegistries
{
	public static IDDDCreatureTypeRegistry creatureTypes;
	public static IDDDDamageTypeRegistry damageTypes;
	
	public static void init()
	{
		damageTypes = new DDDDamageTypes();
		creatureTypes = new DDDCreatureTypes();
		DDDJsonIO.init();
	}
}
