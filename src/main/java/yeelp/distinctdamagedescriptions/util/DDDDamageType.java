package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.DamageSource;

public final class DDDDamageType extends DamageSource
{
	private String type;
	public DDDDamageType(DamageSource parentSource, String type)
	{
		super(parentSource.damageType);
		this.type = type;
	}
	
	public String getExtendedType()
	{
		return this.type;
	}
}
