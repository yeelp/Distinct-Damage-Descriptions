package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DefaultResistances extends DamageResistances implements IMobResistances {

	private static DefaultResistances instance;
	
	public static DefaultResistances getInstance() {
		return instance == null ? instance = new DefaultResistances() : instance;
	}
	
	private DefaultResistances() {
		super(new NonNullMap<DDDDamageType, Float>(() -> 0.0f), Collections.emptySet());
	}

	@Override
	public IMessage getIMessage() {
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}

	@Override
	public boolean hasAdaptiveResistance() {
		return false;
	}
	
	@Override
	public boolean isOriginallyAdaptive() {
		return false;
	}

	@Override
	public float getAdaptiveAmount() {
		return 0;
	}
	
	@Override
	public float getBaseAdaptiveAmount() {
		return 0;
	}

	@Override
	public float getAdaptiveResistanceModified() {
		return 0;
	}

	@Override
	public boolean isAdaptiveTo(DDDDamageType type) {
		return false;
	}

	@Override
	public void setAdaptiveAmount(float amount) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");
	}
	
	@Override
	public void setBaseAdaptiveAmount(float amount) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");
		
	}

	@Override
	public void setAdaptiveResistance(boolean status, boolean permanent) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");

	}

	@Override
	public boolean updateAdaptiveResistance(DamageMap dmgMap) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");
	}

	@Override
	public IMobResistances copy() {
		return this;
	}

	@Override
	public IMobResistances update(EntityLivingBase owner) {
		return this;
	}

	@Override
	public Set<String> getModifiers() {
		return Sets.newHashSet();
	}

	@Override
	public void addModifier(String s) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");
	}

	@Override
	public void removeModifier(String s) {
		throw new UnsupportedOperationException("Default Resistances can't be changed!");
	}

	@Override
	public Class<NBTTagCompound> getSpecificNBTClass() {
		return NBTTagCompound.class;
	}
}
