package yeelp.distinctdamagedescriptions.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(ModConsts.MODID);
	public static int id = 0;
	public static final void init()
	{
		//create dummy message to get their handlers.
		MobResistancesMessage mobMsg = new MobResistancesMessage();
		INSTANCE.registerMessage(mobMsg.getMessageHandler(), MobResistancesMessage.class, id++, Side.CLIENT);
	}
}
