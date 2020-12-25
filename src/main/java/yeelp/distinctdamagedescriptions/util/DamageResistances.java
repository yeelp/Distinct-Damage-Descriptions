package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * Base capability for damage resistance capabilities
 * @author Yeelp
 *
 */
public abstract class DamageResistances implements IDamageResistances
{
	private Map<String, Float> resistances;
	private Set<String> immunities;
	
	DamageResistances(Map<String, Float> resistances, Collection<String> immunities)
	{
		this.resistances = resistances;
		this.immunities = new HashSet<String>(immunities);
	}
	
	public float getResistance(String type)
	{
		return this.resistances.get(type);
	}
	
	public boolean hasImmunity(String type)
	{
		return this.immunities.contains(type);
	}
	
	public void setResistance(String type, float amount)
	{
		this.resistances.put(type, amount);
	}
	
	public void setImmunity(String type, boolean status)
	{
		if(status)
		{
			this.immunities.add(type);
		}
		else
		{
			this.immunities.remove(type);
		}
	}
	
	public void clearImmunities()
	{
		this.immunities.clear();
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList lst = new NBTTagList();
		NBTTagList immunities = new NBTTagList();
		for(Entry<String, Float> entry : resistances.entrySet())
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("type", entry.getKey());
			compound.setFloat("amount", entry.getValue());
			lst.appendTag(compound);
		}
		tag.setTag("resistances", lst);
		for(String s : this.immunities)
		{
			immunities.appendTag(new NBTTagString(s));
		}
		tag.setTag("immunities", immunities);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		this.resistances = new NonNullMap<String, Float>(0.0f);
		this.immunities = new HashSet<String>();
		for(NBTBase nbt : tag.getTagList("resistances", new NBTTagCompound().getId()))
		{
			NBTTagCompound resist = (NBTTagCompound) nbt;
			resistances.put(resist.getString("type"), resist.getFloat("amount"));
		}
		for(NBTBase nbt : tag.getTagList("immunities", new NBTTagString().getId()))
		{
			immunities.add(((NBTTagString) nbt).getString());
		}
	}
}
