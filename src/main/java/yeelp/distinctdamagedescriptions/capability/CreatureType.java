package yeelp.distinctdamagedescriptions.capability;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.capability.providers.CreatureTypeProvider;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;

public class CreatureType implements ICreatureType {
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
		return capability == CreatureTypeProvider.creatureType;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CreatureTypeProvider.creatureType ? CreatureTypeProvider.creatureType.<T>cast(this) : null;
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

	public static void register() {
		CapabilityManager.INSTANCE.register(ICreatureType.class, new CreatureTypeStorage(), new CreatureTypeFactory());
	}

	private static class CreatureTypeFactory implements Callable<ICreatureType> {
		public CreatureTypeFactory() {
		}

		@Override
		public ICreatureType call() throws Exception {
			return CreatureType.UNKNOWN;
		}
	}

	private static class CreatureTypeStorage implements IStorage<ICreatureType> {
		public CreatureTypeStorage() {
		}

		@Override
		public NBTBase writeNBT(Capability<ICreatureType> capability, ICreatureType instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ICreatureType> capability, ICreatureType instance, EnumFacing side, NBTBase nbt) {
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
