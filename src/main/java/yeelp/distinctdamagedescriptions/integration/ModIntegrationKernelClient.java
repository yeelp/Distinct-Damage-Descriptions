package yeelp.distinctdamagedescriptions.integration;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.DDDTinkersIntegrationClient;

@SideOnly(Side.CLIENT)
public class ModIntegrationKernelClient {

	private static final Map<String, Supplier<IModIntegrationClient>> MOD_LIST = Maps.newHashMap();
	
	static {
		MOD_LIST.put(IntegrationIds.TCONSTRUCT_ID, () -> new DDDTinkersIntegrationClient());
	}
	
	public static final void registerClientHandlers() {
		MOD_LIST.forEach((id, sup) -> {
			if(Loader.isModLoaded(id)) {
				DistinctDamageDescriptions.info(String.format("(CLIENT) Distinct Damage Descriptions found %s client side!", id));
				sup.get().getClientSideHandlers().forEach(Handler::register);
			}
		});
	}
}
