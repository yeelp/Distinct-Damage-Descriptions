package yeelp.distinctdamagedescriptions.integration.thebewteenlands;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class TheBetweenlandsCompat implements IModIntegration {

	private static Class<?> blShieldClass, blShieldHandlerClass;
	
	private static Iterable<Handler> handler;
	
	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.BETWEENLANDS_TITLE;
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.BETWEENLANDS_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return handler; 
	}

	static Class<?> getBLShieldClass() {
		return blShieldClass;
	}
	
	static Class<?> getBLShieldHandlerClass() {
		return blShieldHandlerClass;
	}
	
	@Override
	public boolean preInit(FMLPreInitializationEvent evt) {
		try {
			blShieldClass = Class.forName("thebetweenlands.common.item.tools.ItemBLShield");
			for(Class<?> c : blShieldClass.getDeclaredClasses()) {
				DistinctDamageDescriptions.debug(c.getCanonicalName());
			}
			blShieldHandlerClass = Class.forName("thebetweenlands.common.item.tools.ItemBLShield$EventHandler");
		}
		catch(ClassNotFoundException e) {
			DistinctDamageDescriptions.fatal("Could not hook into ItemBLShield for Betweenlands! Integration will halt!");
			e.printStackTrace();
			return false;
		}
		try {
			handler = ImmutableList.of(BetweenlandsShieldHandler.getInstance());
		}
		catch(NoSuchFieldException | SecurityException | IllegalAccessException | NoSuchMethodException e) {
			DistinctDamageDescriptions.fatal("Could not initialize Betweenlands Shield Handler!");
			e.printStackTrace();
			return false;
		}
		return IModIntegration.super.preInit(evt);
	}	
}
