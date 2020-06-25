package yeelp.distinctdamagedescriptions.util;

import javax.annotation.Nonnull;

import net.minecraft.util.DamageSource;

public class DamageDescription
{
	private DamageSource originalSource;
	private DamageType type;
	public DamageDescription(@Nonnull DamageType damageType, @Nonnull DamageSource originalSource)
	{
		this.originalSource = originalSource;
		this.type = damageType;
	}
	
	@Nonnull
	public DamageSource getOriginalSource()
	{
		return originalSource;
	}
	
	@Nonnull
	public DamageType getType()
	{
		return type;
	}
}
