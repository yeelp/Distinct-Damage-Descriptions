package yeelp.distinctdamagedescriptions.registries.impl.dists;

import com.lycanitesmobs.ObjectManager;

import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class LycanitesFluidDistribution {
	public static final SimpleBuiltInDist OOZE = new SimpleBuiltInDist(() -> ModConfig.compat.lycanites.enableOozeDistribution, ObjectManager.getDamageSource("ooze"), DDDBuiltInDamageType.COLD);
	public static final SimpleBuiltInDist ACID = new SimpleBuiltInDist(() -> ModConfig.compat.lycanites.enableAcidDistribution, ObjectManager.getDamageSource("acid"), DDDBuiltInDamageType.ACID);
}
