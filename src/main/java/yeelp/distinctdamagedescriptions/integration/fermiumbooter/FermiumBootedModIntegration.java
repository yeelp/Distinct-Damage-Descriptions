package yeelp.distinctdamagedescriptions.integration.fermiumbooter;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.OptionalMixinKernel;

/**
 * Mod integration that requires Fermium Booter for mod mixins.
 * 
 * @author Yeelp
 *
 */
public abstract class FermiumBootedModIntegration implements IModIntegration {

	/**
	 * Is this integration enabled? Fermium enabled integrations need a flag to
	 * disable if the user doesn't want to add Fermium Booter to their instance.
	 * This should return the corresponding config field.
	 * 
	 * @return True if enabled, false if not.
	 */
	protected abstract boolean enabled();

	@Override
	public boolean preInit(FMLPreInitializationEvent evt) {
		if(this.enabled() && !FermiumBooterIntegration.hasFermiumBooter()) {
			DistinctDamageDescriptions.proxy.handleFermiumBooterNotFound(this.getModTitle());
			return false;
		}
		if(!OptionalMixinKernel.wereMixinsEnqueued(this.getModID())) {
			throw new RuntimeException(String.format("FermiumBooted integration for %s has no IOptionalMixinProvider listed in OptionalMixinKernel!", this.getModID()));
		}
		return IModIntegration.super.preInit(evt);
	}

}
