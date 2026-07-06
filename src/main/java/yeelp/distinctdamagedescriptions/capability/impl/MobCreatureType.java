package yeelp.distinctdamagedescriptions.capability.impl;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.impl.CreatureType;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MobCreatureType implements IMobCreatureType {

	@CapabilityInject(IMobCreatureType.class)
	public static Capability<IMobCreatureType> cap;

	public static final MobCreatureType UNKNOWN = new MobCreatureType(CreatureType.UNKNOWN);
	private Set<String> types;
	private Set<String> potionImmunities;
	private boolean critImmunity = false;
	private boolean isFlammable = true;

	public MobCreatureType(Set<CreatureType> datas) {
		this.types = new HashSet<>();
		this.potionImmunities = new HashSet<>();
		for(CreatureType data : datas) {
			this.types.add(data.getTypeName());
			this.potionImmunities.addAll(data.getPotionImmunities());
			this.critImmunity = this.critImmunity || data.isImmuneToCriticals();
			//AND op here because the default is true and false means not flammable.
			this.isFlammable = this.isFlammable && data.isFlammable();
		}
	}

	public MobCreatureType(CreatureType... datas) {
		this(new HashSet<>(ImmutableList.copyOf(datas)));
	}

	@Override
	public Set<String> getCreatureTypeNames() {
		return this.types;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		return capability == cap ? cap.cast(this) : null;
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
		tag.setBoolean("flammable", this.isFlammable);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.types = new HashSet<>();
		this.potionImmunities = new HashSet<>();
		this.critImmunity = nbt.getBoolean("critImmunity");
		this.isFlammable = !nbt.hasKey("flammable") || nbt.getBoolean("flammable");
		for(NBTBase s : nbt.getTagList("types", new NBTTagString().getId())) {
			this.types.add(((NBTTagString) s).getString());
		}
		for(NBTBase s : nbt.getTagList("potionImmunities", new NBTTagString().getId())) {
			this.potionImmunities.add(((NBTTagString) s).getString());
		}
	}

	@Override
	public boolean isImmuneToPotionEffect(PotionEffect effect) {
		return this.potionImmunities.contains(Objects.requireNonNull(effect.getPotion().getRegistryName()).toString());
	}

	@Override
	public boolean isImmuneToCriticalHits() {
		return this.critImmunity;
	}

	@Override
	public boolean isFlammable() {
		return this.isFlammable;
	}
}
