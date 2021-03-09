package yeelp.distinctdamagedescriptions.api.impl;

public class DDDCustomDamageType extends DDDAbstractDamageType
{
	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour)
	{
		super(name, isPhysical, deathAttackerMessage, deathMessage, colour);
		this.displayName = displayName;
	}

	@Override
	public boolean isCustomDamage()
	{
		return true;
	}
}
