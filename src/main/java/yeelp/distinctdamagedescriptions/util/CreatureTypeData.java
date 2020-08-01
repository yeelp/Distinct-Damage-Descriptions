package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;

import net.minecraft.potion.PotionEffect;

public final class CreatureTypeData
{
	public static final CreatureTypeData UNKNOWN = new CreatureTypeData();
	
	private String type;
	private MobResistanceCategories resists;
	private HashSet<String> potionImmunities;
	public CreatureTypeData(String name, MobResistanceCategories resistances, HashSet<String> potionImmunities)
	{
		this.type = name;
		this.resists = resistances;
		this.potionImmunities = potionImmunities;
	}
	
	private CreatureTypeData()
	{
		type = "unknown";
		resists = new MobResistanceCategories(0, 0, 0, false, false, false, 0, 0);
		potionImmunities = new HashSet<String>();
	}
	
	public String getTypeName()
	{
		return type;
	}
	
	public MobResistanceCategories getMobResistances()
	{
		return resists;
	}
	
	public boolean isImmuneToPotionEffect(PotionEffect effect)
	{
		return potionImmunities.contains(effect.getPotion().getRegistryName().toString());
	}
}
