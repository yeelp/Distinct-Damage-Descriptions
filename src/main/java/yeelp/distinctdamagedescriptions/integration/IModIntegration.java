package yeelp.distinctdamagedescriptions.integration;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

/**
 * Interface for mod compat. Integrators override the initialization phases they
 * need.
 * 
 * @author Yeelp
 * @author Failure
 *
 */
public interface IModIntegration {

	/**
	 * Pre init stage. Do what needs to be done during preinit
	 * 
	 * @param evt The preinitialization event
	 * @return true if preinit successful, will have integration continued in
	 *         initialization phase.
	 */
	default boolean preInit(FMLPreInitializationEvent evt) {
		return true;
	}

	/**
	 * Init stage. Don't register handlers here unless they absolutely have to be.
	 * Instead, return those handlers in {@link #getHandlers()}
	 * 
	 * @param evt Initialization event.
	 * @return true if initialization was successful, will move on to post init.
	 */
	default boolean init(FMLInitializationEvent evt) {
		return true;
	}

	/**
	 * Post init stage. Likely not needed, but here in case,
	 * 
	 * @param evt The post init event
	 * @return true if post init successful. By this point, the mod integration
	 *         needs to be fully completed on DDD's end.
	 */
	default boolean postInit(FMLPostInitializationEvent evt) {
		return true;
	}

	/**
	 * Mod id needed for integration. If we know a mod by this name, we'll load
	 * integration.
	 * 
	 * @return Mod ID integrated with.
	 */
	String getModID();

	/**
	 * Get Handlers that should be registered in init.
	 * 
	 * @return all the handlers that should be registered for this integration.
	 */
	Iterable<Handler> getHandlers();
}
