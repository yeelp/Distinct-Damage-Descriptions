package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public abstract class Distribution implements IDistribution
{
	private float slash;
	private float pierce;
	private float bludge;
	
	Distribution(float slash, float pierce, float bludge)
	{
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
	public void setNewWeights(float slash, float pierce, float bludgeoning)
	{
		this.slash = slash;
		this.pierce = pierce;
		this.bludge = bludgeoning;
	}

	ComparableTriple<Float, Float, Float> distribute(float value)
	{
		return new ComparableTriple<Float, Float, Float>(value*slash, value*pierce, value*bludge);
	}
}
