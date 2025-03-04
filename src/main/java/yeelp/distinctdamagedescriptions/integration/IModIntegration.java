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
	 * @return true if preinit successful, will have integration continued in the
	 *         start of initialization phase.
	 */
	default boolean preInit(FMLPreInitializationEvent evt) {
		return true;
	}

	/**
	 * START of Init stage. Ideally used for information that is not accessible
	 * during pre init but needs to be handled before config is read.
	 * 
	 * @param evt Initialization event.
	 * @return true if initialization before config was successful, will move on to
	 *         init.
	 */
	default boolean initStart(FMLInitializationEvent evt) {
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
	 * Get the proper name for this mod.
	 * 
	 * @return The proper name of the mod.
	 */
	String getModTitle();

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

	/**
	 * The integration can register cross mod compat here if needed. This is called
	 * after integration completes so calls to
	 * {@link ModIntegrationKernel#wasIntegrationLoaded(String)} will return true if
	 * integration was successful.
	 */
	default void registerCrossModCompat() {
		return;
	}

	/**
	 * Mod Integration Metadata. Every Mod Integration needs one of these.
	 * 
	 * @author Yeelp
	 *
	 */
	public final class ModIntegrationMetadata {
		private final String id, name;

		public ModIntegrationMetadata(String id, String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the id
		 */
		String getId() {
			return this.id;
		}

		/**
		 * @return the name
		 */
		String getName() {
			return this.name;
		}

	}
}
