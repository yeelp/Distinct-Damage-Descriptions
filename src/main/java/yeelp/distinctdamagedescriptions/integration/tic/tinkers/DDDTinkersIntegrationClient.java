package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.gui.book.GuiBook;
import yeelp.distinctdamagedescriptions.event.ShouldDrawIconsEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegrationClient;

@SideOnly(Side.CLIENT)
public class DDDTinkersIntegrationClient implements IModIntegrationClient {

	private static final class ShouldDrawIconsHandler extends Handler {
		@SubscribeEvent
		@SuppressWarnings("static-method")
		public void shouldDrawIcons(ShouldDrawIconsEvent evt) {
			if(Minecraft.getMinecraft().currentScreen instanceof GuiBook) {
				evt.setResult(Result.DENY);
			}
		}
	}
	
	@SuppressWarnings("synthetic-access")
	@Override
	public Iterable<Handler> getClientSideHandlers() {
		return ImmutableList.of(new ShouldDrawIconsHandler());
	}
}
