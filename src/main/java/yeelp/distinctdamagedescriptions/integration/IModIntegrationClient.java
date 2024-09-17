package yeelp.distinctdamagedescriptions.integration;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.handlers.Handler;

@SideOnly(Side.CLIENT)
public interface IModIntegrationClient {
	
	Iterable<Handler> getClientSideHandlers();
}
