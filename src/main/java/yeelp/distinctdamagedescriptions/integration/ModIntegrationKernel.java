package yeelp.distinctdamagedescriptions.integration;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.events.CTEventHandler;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla;

public class ModIntegrationKernel {
	/**
	 * Lists of mod that will try to load on DDD startup
	 */
	public static final ImmutableList<IModIntegration> loadedMods = ImmutableList.of(new CTEventHandler(), new Hwyla());

	public void load() {
		for(IModIntegration mod : loadedMods) {
			String modID = mod.getModID();

			if(Loader.isModLoaded(modID)) {
				DistinctDamageDescriptions.info("Distinct Damage Descriptions found " + modID + "!");
				boolean registerSuccessful = mod.register();

				if(!registerSuccessful) {
					DistinctDamageDescriptions.err("There is some problem loading mod " + modID);
				}
			}
		}
	}
}
