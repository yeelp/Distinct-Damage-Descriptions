package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public abstract class Distribution implements IDistribution
{
	private float slash;
	private float pierce;
	private float bludge;
	
	protected boolean invariantViolated(float slash, float pierce, float bludge)
	{
		return Math.min(Math.min(slash, pierce), bludge) < 0;
	}
	
	Distribution(float slash, float pierce, float bludge)
	{
		if(invariantViolated(slash, pierce, bludge))
		{
			throw new InvariantViolationException("New weights are invalid!");
		}
		this.slash = slash;
		this.pierce = pierce;
		this.bludge = bludge;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("slashing", slash);
		tag.setFloat("piercing", pierce);
		tag.setFloat("bludgeoning", bludge);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		slash = nbt.getFloat("slashing");
		pierce = nbt.getFloat("piercing");
		bludge = nbt.getFloat("bludgeoning");
	}

	@Override
	public float getSlashingWeight()
	{
		return slash;
	}

	@Override
	public float getPiercingWeight()
	{
		return pierce;
	}

	@Override
	public float getBludgeoningWeight()
	{
		return bludge;
	}

	@Override
	public void setNewWeights(float slashing, float piercing, float bludgeoning) throws InvariantViolationException
	{
		if(invariantViolated(slashing, piercing, bludgeoning))
		{
			throw new InvariantViolationException("New damage weights are either non positive or do not add to 1!");
		}
		this.slash = slashing;
		this.pierce = piercing;
		this.bludge = bludgeoning;
	}

	ComparableTriple<Float, Float, Float> distribute(float value)
	{
		return new ComparableTriple<Float, Float, Float>(value*slash, value*pierce, value*bludge);
	}
}
