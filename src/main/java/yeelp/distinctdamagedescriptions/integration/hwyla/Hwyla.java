package yeelp.distinctdamagedescriptions.integration.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

@WailaPlugin
public class Hwyla implements IWailaPlugin, IModIntegration {

    public boolean register()
    {
        return FMLInterModComms.sendMessage(
                new Hwyla().getModID(),
                "register",
                "yeelp.distinctdamagedescriptions.integration.hwyla.register"
        );
    }

    /**
     * Registers all HWYLA/Waila integration handlers
     * Called during {@link FMLLoadCompleteEvent}.
     *
     * @param registrar - An instance of IWailaRegistrar to register your providers with.
     */
    @Override
    public void register(IWailaRegistrar registrar) 
    {
        registrar.registerBodyProvider(new EntityHandler(), Entity.class);
    }

    @Override
    public String getModID() 
    {
        return ModConsts.HWYLA_ID;
    }
}
