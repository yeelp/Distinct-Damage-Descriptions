package yeelp.distinctdamagedescriptions.proxy;

import yeelp.distinctdamagedescriptions.init.DDDItems;

public class ClientProxy extends Proxy {

	@Override
	public void preInit() {
		super.preInit();
		DDDItems.initRenders();
	}
}
