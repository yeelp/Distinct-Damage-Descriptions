package yeelp.distinctdamagedescriptions.proxy;

import yeelp.distinctdamagedescriptions.init.DDDItems;

public class Proxy {
	@SuppressWarnings("static-method")
	public void preInit() {
		DDDItems.init();
	}
	
	@SuppressWarnings("static-method")
	public void handleFingerprintViolation() {
		//nothing
	}
	
	@SuppressWarnings({"static-method", "unused"})
	public void handleFermiumBooterNotFound(String integratedMod) {
		//nothing
	}
}
