package yeelp.distinctdamagedescriptions.integration.lycanites;

import com.lycanitesmobs.core.item.ItemBase;

import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.util.DDDFontRenderer;

public final class FontRendererHandler extends Handler {

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOW)
	@SideOnly(Side.CLIENT)
	public final void onTooltipRenderPre(RenderTooltipEvent.Pre evt) {
		if(evt.getStack().getItem() instanceof ItemBase) {
			ItemBase item = (ItemBase) evt.getStack().getItem();
			evt.setFontRenderer(DDDFontRenderer.getInstance(item.getFontRenderer(evt.getStack())));
		}
	}
}
