package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.ProjectileEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

public class TinkerProjectileHandler extends Handler {
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onProjectileLaunch(final ProjectileEvent.OnLaunch evt) {
		IDamageDistribution itemDist = DDDAPI.accessor.getDamageDistribution(evt.projectile.tinkerProjectile.getItemStack());
		DDDAPI.accessor.getDamageDistribution(evt.projectile).setNewWeights(itemDist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(0.0f, itemDist::getWeight)));
	}
}
