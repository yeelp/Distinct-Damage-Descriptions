package yeelp.distinctdamagedescriptions.proxy;

import yeelp.distinctdamagedescriptions.init.DDDItems;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernelClient;

public class ClientProxy extends Proxy {

	@Override
	public void preInit() {
		super.preInit();
		DDDItems.initRenders();
		ModIntegrationKernelClient.registerClientHandlers();
	}
}
