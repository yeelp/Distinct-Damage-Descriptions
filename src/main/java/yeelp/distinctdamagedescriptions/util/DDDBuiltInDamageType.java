package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;

public enum DDDBuiltInDamageType implements DDDDamageType
{
	SLASHING("slashing", true),
	PIERCING("piercing", true),
	BLUDGEONING("bludgeoning", true),
	ACID("acid", false),
	COLD("cold", false),
	FIRE("fire", false),
	FORCE("force", false),
	LIGHTNING("lightning", false),
	NECROTIC("necrotic", false),
	POISON("poison", false),
	PSYCHIC("psychic", false),
	RADIANT("radiant", false),
	THUNDER("thunder", false);
	
	private IDistribution dist;
	private String name;
	private Type type;
	
	DDDBuiltInDamageType(String name, boolean isPhysical)
	{
		this.name = "ddd_"+name;
		this.dist = new DamageDistribution(new Tuple<String, Float>(this.name, 1.0f));
		this.type = isPhysical ? Type.PHYSICAL : Type.SPECIAL;
	}
	@Override
	public String getTypeName()
	{
		return this.name;
	}

	@Override
	public IDistribution getBaseDistribution()
	{
		return this.dist;
	}

	@Override
	public Type getType()
	{
		return this.type;
	}

}
