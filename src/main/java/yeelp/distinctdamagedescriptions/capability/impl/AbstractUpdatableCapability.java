package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.nbt.NBTBase;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public abstract class AbstractUpdatableCapability<NBT extends NBTBase> implements DDDUpdatableCapabilityBase<NBT> {
	
	private final Set<String> mods = Sets.newHashSet();
	
	@Override
	public void addModifier(String s) {
		this.mods.add(s);
	}
	
	@Override
	public void removeModifier(String s) {
		this.mods.remove(s);
	}
	
	@Override
	public Set<String> getModifiers() {
		return this.mods;
	}
	
	protected void updateModifiers(Set<String> newMods) {
		newMods.forEach(this::addModifier);
		YMath.setDifference(this.getModifiers(), newMods).forEach(this::removeModifier);
	}
}