package yeelp.distinctdamagedescriptions.util;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

public class ShieldDistribution extends Distribution implements IDistribution
{
	public ShieldDistribution()
	{
		this(new NonNullMap<String, Float>(1.0f));
	}
	
	public ShieldDistribution(Map<String, Float> blockMap)
	{
		super(blockMap);
	}
	
	public DamageCategories block(IDamageDistribution dist, float dmg)
	{
		DamageCategories dmges = dist.distributeDamage(dmg);
		Map<String, Float> fullDamage = dmges.getDistribution();
		Map<String, Float> remainingDamage = new NonNullMap<String, Float>(0.0f);
		for(Entry<String, Float> entry : fullDamage.entrySet())
		{
			remainingDamage.compute(entry.getKey(), (t, w) -> blockDamage(entry.getValue(), w));
		}
		return new DamageCategories(remainingDamage);
	}


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ShieldDistributionProvider.shieldDist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ShieldDistributionProvider.shieldDist ? ShieldDistributionProvider.shieldDist.<T> cast(this) : null;
	}
	
	private float blockDamage(float damage, float weight)
	{
		return MathHelper.clamp(damage*(1-weight), 0, Float.MAX_VALUE);
	}
}