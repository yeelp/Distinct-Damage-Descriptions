package yeelp.distinctdamagedescriptions.integration.hwyla;

import java.util.Collections;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class Hwyla implements IModIntegration {
	
	public interface HwylaConsts {
		String REGISTER_METHOD = "yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla.registerHwyla";
		String REGISTER_MESSAGE = "register";
		String HWYLA = "hwyla";
		String MAX = "max";
		String WITH_ARMOR = "witharmor";
		String ALL_TYPES = "alltypes";
		String ALL_OTHER_TYPES = "allothertypes";
		String ADAPTED_TO = "adaptedto";
		
		String HWYLA_RESISTS = "ddd.resists";
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		return FMLInterModComms.sendMessage(ModConsts.IntegrationIds.HWYLA_ID, HwylaConsts.REGISTER_MESSAGE, HwylaConsts.REGISTER_METHOD);
	}

	public static void registerHwyla(IWailaRegistrar registrar) {
		EntityHandler handler = new EntityHandler();
		registrar.registerBodyProvider(handler, Entity.class);
		registrar.registerNBTProvider(handler, Entity.class);
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return Collections.emptyList();
	}

	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.HWYLA_TITLE;
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.HWYLA_ID;
	}
	
	
}
