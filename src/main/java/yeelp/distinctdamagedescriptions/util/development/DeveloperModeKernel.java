package yeelp.distinctdamagedescriptions.util.development;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import yeelp.distinctdamagedescriptions.ModConfig;

public final class DeveloperModeKernel {

	private static DeveloperModeKernel instance;
	private Logger logger;
	
	private DeveloperModeKernel() {
		this.logger = LogManager.getLogger();
	}
	
	public void log(String msg, boolean inChat) {
		this.logger.info("[DISTINCT DAMAGE DESCRIPTIONS (DEVELOPER)]: " + msg);
		if(inChat) {
			Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentString("[DDD DEVELOPER]: " + msg), false);
		}
	}
	
	public void logIfEnabled(String msg, boolean inChat) {
		if(ModConfig.dev.enabled) {
			this.log(msg, inChat);
		}
	}
	
	public static void init() {
		
	}
	
	public static DeveloperModeKernel getInstance() {
		return instance == null ? instance = new DeveloperModeKernel() : instance;
	}
}
