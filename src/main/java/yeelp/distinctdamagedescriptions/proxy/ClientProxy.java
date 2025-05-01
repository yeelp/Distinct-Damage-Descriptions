package yeelp.distinctdamagedescriptions.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.client.screen.GuiFingerprintViolationWarning;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.init.DDDItems;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernelClient;
import yeelp.distinctdamagedescriptions.integration.fermiumbooter.client.screen.GuiFermiumBooterNotFound;

public class ClientProxy extends Proxy {

	public static final String FINGERPRINT_FILE = "ddd_ignorefingerprint.txt";
	public static boolean willShowFermiumScreen = false;
	
	@Override
	public void preInit() {
		super.preInit();
		DDDItems.initRenders();
		ModIntegrationKernelClient.registerClientHandlers();
	}
	
	@Override
	public void handleFingerprintViolation() {
		File f = new File(DistinctDamageDescriptions.getModConfigDirectory(), FINGERPRINT_FILE);
		if(f.exists()) {
			DistinctDamageDescriptions.debug("Found indicator file to ignore fingerprint warnings, checking if version matches.");
			try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
				String line = reader.readLine();
				if(line.contains(ModConsts.VERSION)) {
					DistinctDamageDescriptions.debug("Version matches, ignoring warnings.");
					return;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Handler() {
			private boolean openedOnce = false;
			@SubscribeEvent
			public void onGuiOpen(GuiOpenEvent evt) {
				if(!this.openedOnce && evt.getGui() instanceof GuiMainMenu) {
					this.openedOnce = true;
					evt.setGui(new GuiFingerprintViolationWarning(evt.getGui()));
				}
			}
		}.register();
	}
	
	@Override
	public void handleFermiumBooterNotFound(String integratedMod) {
		if(willShowFermiumScreen) {
			return;
		}
		willShowFermiumScreen = true;
		new Handler() {
			private boolean openedOnce = false;
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public void onGuiOpen(GuiOpenEvent evt) {
				if(!this.openedOnce && evt.getGui() instanceof GuiMainMenu) {
					this.openedOnce = true;
					evt.setGui(new GuiFermiumBooterNotFound(evt.getGui(), integratedMod));
				}
			}
		}.register();
	}
}
