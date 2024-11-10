package yeelp.distinctdamagedescriptions.api;

import java.util.Map;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.IPriority;

/**
 * A modifier that gets applied to a Distribution dynamically during gameplay.
 * For modifiers with the same priority, they are applied simultaneously, though
 * the reallocated modifiers are applied before the non reallocating modifiers.
 * 
 * @author Yeelp
 * 
 * @param <T> The type of {@link ICapabilityProvider} this modifier applies to.
 *
 */
public interface IDDDCapModifier<T extends ICapabilityProvider> extends IHasCreationSource, IPriority, Map<DDDDamageType, Float> {

	/**
	 * Should this modifier reallocate weights to "make room" for the weights
	 * provided by this modifier? If true, the weights from this modifier will be
	 * divided up and evenly subtracted from the weights from the existing
	 * distribution before the new weights are added in. If false, the modifier will
	 * add its weights to the distribution and then the weights of the distribution
	 * will be redistributed so it adds to 100% if this modifier applied to damage
	 * distributions.
	 * 
	 * @return True if weights should be reallocated.
	 */
	boolean shouldReallocate();

	/**
	 * Get the enum information that dictates what capability this modifier applies
	 * to.
	 * 
	 * @return The AppliesTo enum.
	 */
	AppliesTo getAppliesToEnum();

	/**
	 * Does this modifier apply to this object?
	 * 
	 * @param t the object to test.
	 * @return true if it applies, false if not.
	 */
	boolean applicable(T t);

	/**
	 * Get the name of this modifier.
	 * 
	 * @return The name of this modifier.
	 */
	String getName();

	/**
	 * Reference to the type of DDD capability an IDistributionModifier applies to.
	 * 
	 * @author Yeelp
	 *
	 */
	public enum AppliesTo {
		ITEM_DAMAGE(IDamageDistribution.class),
		MOB_DAMAGE(IDamageDistribution.class),
		PROJECTILE(IDamageDistribution.class),
		MOB_RESISTANCES(IMobResistances.class),
		ARMOR(IArmorDistribution.class),
		SHIELD(ShieldDistribution.class);

		private final Class<? extends DDDCapabilityBase<?>> clazz;

		AppliesTo(Class<? extends DDDCapabilityBase<?>> clazz) {
			this.clazz = clazz;
		}

		public <T extends DDDCapabilityBase<?>> boolean appliesTo(T t) {
			return this.clazz.isInstance(t);
		}
		
		public boolean allowsNegativeWeights() {
			return !this.clazz.equals(IDamageDistribution.class);
		}
	}

	@Override
	default int compareTo(IPriority o) {
		int result = IPriority.super.compareTo(o);
		if(result == 0 && o instanceof IDDDCapModifier) {
			boolean oReallocates = ((IDDDCapModifier<?>) o).shouldReallocate();
			if(this.shouldReallocate()) {
				return oReallocates ? 0 : 1;
			}
			return oReallocates ? -1 : 0;
		}
		return result;
	}
	
	/**
	 * Validate if this modifier is legal. A legal modifier is one that if it applies to item damage, mob damage or projectile damage, then there are no negative weights.
	 * @return True if valid
	 */
	default boolean validate() {
		if(!this.getAppliesToEnum().allowsNegativeWeights() && this.values().stream().anyMatch((f) -> f < 0.0f)) {			
			return false;
		}
		return true;
	}
}
