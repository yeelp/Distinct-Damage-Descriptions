package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Base capability for damage resistance capabilities
 * @author Yeelp
 *
 */
public abstract class DamageResistances implements IDamageResistances
{
	private float slashing;
	private float piercing;
	private float bludgeoning;
	private boolean slashImmune;
	private boolean pierceImmune;
	private boolean bludgeImmune;
	
	DamageResistances(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune)
	{
		this.slashing = slashing;
		this.piercing = piercing;
		this.bludgeoning = bludgeoning;
		this.slashImmune = slashImmune;
		this.pierceImmune = pierceImmune;
		this.bludgeImmune = bludgeImmune;
	}
	
	@Override
	public float getSlashingResistance()
	{
		return slashing;
	}
	
	@Override
	public float getPiercingResistance()
	{
		return piercing;
	}
	
	@Override
	public float getBludgeoningResistance()
	{
		return bludgeoning;
	}
	
	@Override
	public void setSlashingResistance(float resist) throws InvariantViolationException
	{
		if(resist > 1)
		{
			throw new InvariantViolationException("Damage resistance can't be greater than 1!");
		}
		slashing = resist;
	}
	
	@Override 
	public void setPiercingResistance(float resist) throws InvariantViolationException
	{
		if(resist > 1)
		{
			throw new InvariantViolationException("Damage resistance can't be greater than 1!");
		}
		piercing = resist;
	}
	
	@Override
	public void setBludgeoningResistance(float resist) throws InvariantViolationException
	{
		if(resist > 1)
		{
			throw new InvariantViolationException("Damage resistance can't be greater than 1!");
		}
		bludgeoning = resist;
	}
	
	@Override
	public void setSlashingImmunity(boolean immune)
	{
		slashImmune = immune;
	}
	
	@Override
	public void setPiercingImmunity(boolean immune)
	{
		pierceImmune = immune;
	}
	
	@Override
	public void setBludgeoningImmunity(boolean immune)
	{
		bludgeImmune = immune;
	}
	
	@Override
	public boolean isSlashingImmune()
	{
		return slashImmune;
	}
	
	@Override
	public boolean isPiercingImmune()
	{
		return pierceImmune;
	}
	
	@Override
	public boolean isBludgeoningImmune()
	{
		return bludgeImmune;
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("slashingResist", slashing);
		tag.setFloat("piercingResist", piercing);
		tag.setFloat("bludgeoningResist", bludgeoning);
		tag.setBoolean("slashingImmunity", slashImmune);
		tag.setBoolean("piercingImmunity", pierceImmune);
		tag.setBoolean("bludgeoningImmunity", bludgeImmune);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		slashing = tag.getFloat("slashingResist");
		piercing = tag.getFloat("piercingResist");
		bludgeoning = tag.getFloat("bludgeoningResist");
		slashImmune = tag.getBoolean("slashingImmunity");
		pierceImmune = tag.getBoolean("piercingImmunity");
		bludgeImmune = tag.getBoolean("bludgeoningImmunity");
	}
}
