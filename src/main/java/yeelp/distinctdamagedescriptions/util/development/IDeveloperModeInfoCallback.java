package yeelp.distinctdamagedescriptions.util.development;

import java.util.function.Supplier;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;

/**
 * The callback fired from the {@link DeveloperModeKernel} when DDD's Developer
 * Mode is enabled.
 * 
 * @author Yeelp
 *
 * @param <E> The Event that this callback works on.
 */
public interface IDeveloperModeInfoCallback<E extends Event> {

	/**
	 * The actual callback. This is the information that should be printed to the
	 * console/chat. You can split the String up with new line characters as needed
	 * (recommended).
	 * 
	 * @param evt the event
	 * @return The String to be printed.
	 */
	String callback(E evt);

	/**
	 * The current status of this callback; whether it should display in console,
	 * chat and conse, or not at all. While you can just return the config option
	 * you've set to this callback, it is recommended to use a {@link Supplier} in
	 * case the option is updated. A Supplier will always get the currently set
	 * value.
	 * 
	 * @return The DeveloperStatus.
	 */
	DeveloperStatus getStatus();

	/**
	 * Any other checks for if the callback should fire or not.
	 * 
	 * @param evt the event
	 * @return True if the event should fire, false if not.
	 */
	boolean shouldFire(E evt);

	/**
	 * Get the world from the event. This is used for a {@link World#isRemote}
	 * check.
	 * 
	 * @param evt the event
	 * @return The World
	 */
	World getWorld(E evt);
}
