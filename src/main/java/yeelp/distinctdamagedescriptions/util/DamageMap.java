package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.DamageSource;

public final class DamageMap extends DDDAbstractMap<Float>
{
	public DamageMap()
	{
		super(0.0f);
	}
	
	public DDDDamageSource makeDamageSource(DamageSource src)
	{
		return new DDDDamageSource(src, this.keySet());
	}
}
