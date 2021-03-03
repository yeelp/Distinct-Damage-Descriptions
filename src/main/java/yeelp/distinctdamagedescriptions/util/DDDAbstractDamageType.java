package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

/**
 * An abstract skeleton implementation of DDDDamageType
 * @author Yeelp
 *
 */
public abstract class DDDAbstractDamageType implements DDDDamageType
{
	protected String displayName;
	private final String name;
	private final String attackerDeathMessage;
	private final String noAttackerDeathMessage;
	private final IDamageDistribution dist;
	private final Type type;

	DDDAbstractDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour)
	{
		this.name = "ddd_"+name;
		this.attackerDeathMessage = deathAttackerMessage;
		this.noAttackerDeathMessage = deathMessage;
		this.dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(this, 1.0f));
		this.type = isPhysical ? Type.PHYSICAL : Type.SPECIAL;
	}
	
	@Override
	public String getTypeName()
	{
		return this.name;
	}

	@Override
	public IDamageDistribution getBaseDistribution()
	{
		return this.dist;
	}

	@Override
	public Type getType()
	{
		return this.type;
	}

	@Override
	public String getDeathMessage(boolean hasAttacker)
	{
		return hasAttacker ? this.attackerDeathMessage : this.noAttackerDeathMessage;
	}
	
	@Override
	public String getDisplayName()
	{
		return this.displayName;
	}
}
