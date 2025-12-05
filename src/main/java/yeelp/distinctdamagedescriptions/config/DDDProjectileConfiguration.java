package yeelp.distinctdamagedescriptions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class DDDProjectileConfiguration extends DDDDistributionConfiguration<IDamageDistribution> implements IDDDProjectileConfiguration {
	private final Map<String, String> itemProjMap = new HashMap<String, String>();

	DDDProjectileConfiguration(Supplier<IDamageDistribution> defaultDist) {
		super(defaultDist);
	}

	@Override
	public IDamageDistribution getFromItemID(String itemID) {
		return this.get(DDDMetadataAcceptingConfiguration.wrapOperationInMetadataCheck(itemID, this.itemProjMap::get, null));
	}

	@Override
	public boolean isProjectilePairRegistered(Entity projectile) {
		Optional<String> oLoc = YResources.getEntityIDString(projectile);
		return oLoc.isPresent() ? this.itemProjMap.containsValue(oLoc.get()) : false;
	}

	@Override
	public boolean isProjectilePairRegistered(String itemID) {
		return DDDMetadataAcceptingConfiguration.wrapOperationInMetadataCheck(itemID, this.itemProjMap::containsKey, false);
	}

	@Override
	public void registerItemProjectilePair(String itemID, String projID) {
		this.itemProjMap.put(itemID, projID);
	}
}
