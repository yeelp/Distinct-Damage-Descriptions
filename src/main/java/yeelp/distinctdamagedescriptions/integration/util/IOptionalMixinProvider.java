package yeelp.distinctdamagedescriptions.integration.util;

public interface IOptionalMixinProvider {

	@SuppressWarnings("SameReturnValue")
    String getModID();
	
	boolean enabled();
}
