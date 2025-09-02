package yeelp.distinctdamagedescriptions.integration.tetra;

import java.util.stream.Stream;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDDistributionBiasConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

@DDDLoader(modid = ModConsts.IntegrationIds.TETRA_ID, name = "Tetra Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class TetraConfigurations {

	public static IDDDConfiguration<DistributionBias> toolBiasResistance;

	public static IDDDConfiguration<DistributionBias> toolMaterialBias;

	@Initializer
	public static void init() {
		final DDDBaseMap<Float> map = Stream.of(DDDBuiltInDamageType.BLUDGEONING).collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, (t) -> 1.0f));
		toolBiasResistance = new DDDBaseConfiguration<DistributionBias>(() -> new DistributionBias(map, 0));
		toolMaterialBias = new DDDBaseConfiguration<DistributionBias>(() -> new DistributionBias(map, 0));
		try {
			DDDConfigLoader.getInstance().enqueueAll(new DDDDistributionBiasConfigReader("Tetra Compat: Tool Bias", ModConfig.compat.tetra.toolPartDists, toolBiasResistance), new DDDDistributionBiasConfigReader("Tetra Compat: Material Bias", ModConfig.compat.tetra.toolMatDists, toolMaterialBias));
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
