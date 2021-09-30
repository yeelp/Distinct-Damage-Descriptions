package yeelp.distinctdamagedescriptions.integration.tic;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public abstract class DDDTiCIntegration implements IModIntegration {

	protected abstract DDDBookTransformer getBookTransformer();

	@Override
	public final boolean preInit(FMLPreInitializationEvent evt) {
		if(evt.getSide() == Side.CLIENT) {
			this.getBookTransformer().register();
		}
		return true;
	}
}
