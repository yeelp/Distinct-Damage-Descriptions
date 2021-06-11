package yeelp.distinctdamagedescriptions.init.config;

import java.util.HashMap;
import java.util.Optional;

import net.minecraft.entity.Entity;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class DDDProjectileConfiguration extends DDDBaseConfiguration<IDamageDistribution> implements IDDDProjectileConfiguration {
	private final HashMap<String, String> itemProjMap = new HashMap<String, String>();

	public DDDProjectileConfiguration(IDamageDistribution defaultDist) {
		super(defaultDist);
	}

	@Override
	public IDamageDistribution getFromItemID(String itemID) {
		return this.get(itemProjMap.get(itemID));
	}

	@Override
	public boolean isProjectilePairRegistered(Entity projectile) {
		Optional<String> oLoc = YResources.getEntityIDString(projectile);
		return oLoc.isPresent() ? itemProjMap.containsValue(oLoc.get()) : false;
	}

	@Override
	public boolean isProjectilePairRegistered(String itemID) {
		return itemProjMap.containsKey(itemID);
	}

	@Override
	public void registerItemProjectilePair(String itemID, String projID) {
		itemProjMap.put(itemID, projID);
	}
}
