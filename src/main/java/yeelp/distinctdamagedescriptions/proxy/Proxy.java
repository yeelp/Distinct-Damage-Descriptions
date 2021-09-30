package yeelp.distinctdamagedescriptions.proxy;

import yeelp.distinctdamagedescriptions.init.DDDItems;

public class Proxy {
	@SuppressWarnings("static-method")
	public void preInit() {
		DDDItems.init();
	}
}
