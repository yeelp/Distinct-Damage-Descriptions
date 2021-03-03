package yeelp.distinctdamagedescriptions.init.config;

import java.util.HashSet;

import yeelp.distinctdamagedescriptions.capability.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public abstract class DDDConfigurations
{
	public static IDDDConfiguration<IDamageDistribution> items;
	public static IDDDConfiguration<IArmorDistribution> armors;
	public static IDDDConfiguration<ShieldDistribution> shields;
	public static IDDDConfiguration<IDamageDistribution> mobDamage;
	public static IDDDConfiguration<MobResistanceCategories> mobResists;
	public static IDDDProjectileConfiguration projectiles;
	
	public static void init()
	{
		items = new DDDBaseConfiguration<IDamageDistribution>(DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution());
		armors = new DDDBaseConfiguration<IArmorDistribution>(new ArmorDistribution());
		shields = new DDDBaseConfiguration<ShieldDistribution>(new ShieldDistribution());
		mobDamage = new DDDBaseConfiguration<IDamageDistribution>(DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution());
		mobResists = new DDDBaseConfiguration<MobResistanceCategories>(new MobResistanceCategories(new NonNullMap<DDDDamageType, Float>(0.0f), new HashSet<DDDDamageType>(), 0.0f, 0.0f));
		projectiles = new DDDProjectileConfiguration(DDDBuiltInDamageType.PIERCING.getBaseDistribution());
		DDDConfigReader.readConfig();
	}
}
