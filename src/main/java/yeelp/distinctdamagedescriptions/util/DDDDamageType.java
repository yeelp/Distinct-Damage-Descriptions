package yeelp.distinctdamagedescriptions.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.DamageSource;

public final class DDDDamageType extends DamageSource
{
	private Set<String> types;
	public DDDDamageType(DamageSource parentSource, String... types)
	{
		super(parentSource.damageType);
		this.types = new HashSet<String>(Arrays.asList(types));
	}
	
	public Set<String> getExtendedTypes()
	{
		return this.types;
	}
}
