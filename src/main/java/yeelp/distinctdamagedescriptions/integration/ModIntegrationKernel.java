package yeelp.distinctdamagedescriptions.integration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraftforge.fml.common.Loader;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.events.CTEventHandler;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla;

public class ModIntegrationKernel {
	/**
	 * Lists of mod that will try to load on DDD startup
	 */
	public static final Map<String, Supplier<IModIntegration>> integratableMods = new HashMap<String, Supplier<IModIntegration>>();
	private static final List<IModIntegration> loadedMods = new LinkedList<IModIntegration>();
	
	static {
		integratableMods.put(ModConsts.CRAFTTWEAKER_ID, () -> new CTEventHandler());
		integratableMods.put(ModConsts.HWYLA_ID, () -> new Hwyla());
	}

	/**
	 * Load
	 */
	@SuppressWarnings("static-method")
	public void load() {
		integratableMods.forEach((mod, sup) -> {
			if(Loader.isModLoaded(mod)) {
				DistinctDamageDescriptions.info("Distinct Damage Descriptions found " + mod + "!");
				IModIntegration iIntegration = sup.get();
				boolean registerSuccessful = iIntegration.register();

				if(!registerSuccessful) {
					DistinctDamageDescriptions.err("There is some problem loading mod " + mod);
				}
				else {
					loadedMods.add(iIntegration);
				}
			}
		});
	}
}
