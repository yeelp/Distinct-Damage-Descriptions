package yeelp.distinctdamagedescriptions.api;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;

@ParametersAreNonnullByDefault
public interface IDistinctDamageDescriptionsMutator {
	
	/**
	 * Register an item capability
	 * 
	 * @param <T>   The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for
	 *              capabilities
	 * @param cap   The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerItemCap(Class<T> clazz, Capability<? extends T> cap);

	/**
	 * Register a projectile capability
	 * 
	 * @param <T>   The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for
	 *              capabilities
	 * @param cap   The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerProjectileCap(Class<T> clazz, Capability<? extends T> cap);

	/**
	 * Register an entity capability
	 * 
	 * @param <T>   The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for
	 *              capabilities
	 * @param cap   The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerEntityCap(Class<T> clazz, Capability<? extends T> cap);
}
