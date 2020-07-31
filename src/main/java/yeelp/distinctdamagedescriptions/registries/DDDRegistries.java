package yeelp.distinctdamagedescriptions.registries;

public abstract class DDDRegistries
{
	public static IDDDCreatureTypeRegistry creatureTypes;
	public static IDDDMobResistancesRegistry mobResists;
	public static IDDDMobDamageRegistry mobDamage;
	public static IDDDItemPropertiesRegistry itemProperties;
	public static IDDDProjectilePropertiesRegistry projectileProperties;
	
	public static void init()
	{
		DDDRegistriesImpl.values();
	}
}
