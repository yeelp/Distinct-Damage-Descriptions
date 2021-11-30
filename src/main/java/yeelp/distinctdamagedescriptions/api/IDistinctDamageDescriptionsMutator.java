package yeelp.distinctdamagedescriptions.api;

import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.ResistMap;

@ParametersAreNonnullByDefault
public interface IDistinctDamageDescriptionsMutator {
	/**
	 * Set player resistances
	 * 
	 * @param player
	 * @param newReists      map of new resistances
	 * @param newImmunities  set of new immunities, will overwrite old ones.
	 * @param adaptive       adaptability status
	 * @param adaptiveAmount amount for adaptability
	 */
	public void setPlayerResistances(EntityPlayer player, ResistMap newReists, Set<DDDDamageType> newImmunities, boolean adaptive, float adaptiveAmount);

	/**
	 * Updates an entity's adaptive resistance. Syncs capability if the entity is a
	 * player.
	 * 
	 * @param entity
	 * @param dmgMap the distribution of damage this mob took
	 * @return true if resistances were updated
	 */
	public boolean updateAdaptiveResistances(EntityLivingBase entity, DamageMap dmgMap);
	
	/**
	 * Register an item capability
	 * @param <T> The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for capabilities
	 * @param cap The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerItemCap(Class<T> clazz, Capability<? extends T> cap);
	
	/**
	 * Register a projectile capability
	 * @param <T> The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for capabilities
	 * @param cap The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerProjectileCap(Class<T> clazz, Capability<? extends T> cap);
	
	/**
	 * Register an entity capability
	 * @param <T> The kind of capability
	 * @param clazz the capability root class. DDD uses this when searching for capabilities
	 * @param cap The capability instance
	 */
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerEntityCap(Class<T> clazz, Capability<? extends T> cap);
}
