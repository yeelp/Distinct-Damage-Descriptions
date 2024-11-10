package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.util.lib.NonNullSet;

public abstract class AbstractDDDResistanceModifier extends AbstractDDDCapModifier<EntityLivingBase> implements IDDDResistanceModifier {

	private float adaptabilityMod;
	private Optional<Boolean> adaptabilityToggle;
	private final Set<DDDDamageType> addedImmunities = new NonNullSet<DDDDamageType>(), removedImmunities = new NonNullSet<DDDDamageType>();
	
	protected AbstractDDDResistanceModifier(Supplier<Float> defaultVal, Source src, boolean shouldReallocate, float adaptabilityMod) {
		this(defaultVal, src, shouldReallocate, adaptabilityMod, Optional.empty());
	}
	
	protected AbstractDDDResistanceModifier(Supplier<Float> defaultVal, Source src, boolean shouldReallocate, float adaptabilityMod, boolean adaptabilityToggle) {
		this(defaultVal, src, shouldReallocate, adaptabilityMod, Optional.of(adaptabilityToggle));
	}
	
	private AbstractDDDResistanceModifier(Supplier<Float> defaultVal, Source src, boolean shouldReallocate, float adaptabilityMod, Optional<Boolean> adaptabilityToggle) {
		super(defaultVal, src, shouldReallocate);
		this.adaptabilityMod = adaptabilityMod;
		this.adaptabilityToggle = adaptabilityToggle;
	}
	
	@Override
	public final AppliesTo getAppliesToEnum() {
		return AppliesTo.MOB_RESISTANCES;
	}
	
	@Override
	public void setAdaptabilityMod(float amount) {
		this.adaptabilityMod = amount;
	}
	
	@Override
	public float getAdaptabilityMod() {
		return this.adaptabilityMod;
	}
	
	@Override
	public Optional<Boolean> toggleAdaptability() {
		return this.adaptabilityToggle;
	}
	
	@Override
	public void setAdaptability(Boolean state) {
		this.adaptabilityToggle = Optional.ofNullable(state);
	}
	
	@Override
	public void addImmunity(DDDDamageType type) {
		this.addedImmunities.add(type);
	}
	
	@Override
	public void removeAddedImmunity(DDDDamageType type) {
		this.addedImmunities.remove(type);
	}
	
	@Override
	public Set<DDDDamageType> getImmunitiesToAdd() {
		return this.addedImmunities;
	}
	
	@Override
	public void removeImmunity(DDDDamageType type) {
		this.removedImmunities.add(type);
	}

	@Override
	public void removeRemovedImmunity(DDDDamageType type) {
		this.removedImmunities.remove(type);
	}
	
	@Override
	public Set<DDDDamageType> getImmunitiesToRemove() {
		return this.removedImmunities;
	}
}
