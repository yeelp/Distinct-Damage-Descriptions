package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;

public final class DDDExplosionDist implements DDDPredefinedDistribution {
	private static IDamageDistribution dist;

	@Override
	public boolean enabled() {
		return ModConfig.dmg.extraDamage.enableExplosionDamage;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource source, EntityLivingBase target) {
		return source.isExplosion() && this.enabled() ? dist.getCategories() : Collections.emptySet();
	}

	@Override
	public IDamageDistribution getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return src.isExplosion() ? dist : DDDBuiltInDamageType.NORMAL.getBaseDistribution();
	}

	@Override
	public String getName() {
		return "explosion";
	}

	public static void update() {
		if(ModConfig.dmg.extraDamage.enableExplosionDamage) {
			dist = new DamageDistribution(DDDConfigReader.buildMap(0.0f, DDDConfigReader.parseListOfTuples(ModConfig.dmg.extraDamage.explosionDist)));
		}
	}
}
