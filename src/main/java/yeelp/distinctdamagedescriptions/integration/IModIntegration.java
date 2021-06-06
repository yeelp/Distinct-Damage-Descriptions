package yeelp.distinctdamagedescriptions.integration;

public interface IModIntegration {
	/**
	 * Entry point for registering integrated mod's handlers
	 */
	boolean register();

	String getModID();
}
