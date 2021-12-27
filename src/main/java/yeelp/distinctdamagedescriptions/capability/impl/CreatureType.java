package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;

public class CreatureType implements ICreatureType {

	@CapabilityInject(ICreatureType.class)
	public static Capability<ICreatureType> cap;

	public static final CreatureType UNKNOWN = new CreatureType(CreatureTypeData.UNKNOWN);
	private Set<String> types;
	private Set<String> potionImmunities;
	private boolean critImmunity = false;

	public CreatureType(Set<CreatureTypeData> datas) {
		this.types = new HashSet<String>();
		this.potionImmunities = new HashSet<String>();
		for(CreatureTypeData data : datas) {
			this.types.add(data.getTypeName());
			this.potionImmunities.addAll(data.getPotionImmunities());
			this.critImmunity = this.critImmunity || data.isImmuneToCriticals();
		}
	}

	public CreatureType(CreatureTypeData... datas) {
		this(new HashSet<CreatureTypeData>(ImmutableList.copyOf(datas)));
	}

	@Override
	public Set<String> getCreatureTypeNames() {
		return this.types;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == cap ? cap.<T>cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList types = new NBTTagList(), potImmunities = new NBTTagList();
		for(String s : this.types) {
			types.appendTag(new NBTTagString(s));
		}
		for(String s : this.potionImmunities) {
			potImmunities.appendTag(new NBTTagString(s));
		}
		tag.setTag("types", types);
		tag.setTag("potionImmunities", potImmunities);
		tag.setBoolean("critImmunity", this.critImmunity);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.types = new HashSet<String>();
		this.potionImmunities = new HashSet<String>();
		this.critImmunity = nbt.getBoolean("critImmunity");
		for(NBTBase s : nbt.getTagList("types", new NBTTagString().getId())) {
			this.types.add(((NBTTagString) s).getString());
		}
		for(NBTBase s : nbt.getTagList("potionImmunities", new NBTTagString().getId())) {
			this.potionImmunities.add(((NBTTagString) s).getString());
		}
	}

	@Override
	public boolean isImmuneToPotionEffect(PotionEffect effect) {
		return this.potionImmunities.contains(effect.getPotion().getRegistryName().toString());
	}

	@Override
	public boolean isImmuneToCriticalHits() {
		return this.critImmunity;
	}
}
