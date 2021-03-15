package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.registries.impl.DDDCreatureTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDistributions;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.util.DDDJsonIO;

public abstract class DDDRegistries
{
	public static IDDDCreatureTypeRegistry creatureTypes;
	public static IDDDDamageTypeRegistry damageTypes;
	public static IDDDDistributionRegistry distributions;
	
	public static void preInit()
	{
		damageTypes = new DDDDamageTypes();
		creatureTypes = new DDDCreatureTypes();
		distributions = new DDDDistributions();
		DDDExplosionDist.update();
	}
	
	public static void init()
	{		
		distributions.register(DDDJsonIO.init());	
	}
}
