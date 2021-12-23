package yeelp.distinctdamagedescriptions.integration.lycanites;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Functions;
import com.lycanitesmobs.core.entity.damagesources.MinionEntityDamageSource;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class LycanitesMinionDistribution implements DDDPredefinedDistribution {

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return getDamageDistribution(src).map(IDamageDistribution::getCategories).orElse(Collections.emptySet());
	}

	@Override
	public String getName() {
		return "Minion Projectile Damage";
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return getDamageDistribution(src);
	}

	private static final Optional<IDamageDistribution> getDamageDistribution(DamageSource src) {
		if(src instanceof MinionEntityDamageSource) {
			return YResources.getEntityIDString(src.getImmediateSource()).flatMap(Functions.compose((o) -> o.flatMap(DDDConfigurations.projectiles::getSafe), LycanitesConfigurations.creatureProjectiles::getSafe));			
		}
		return Optional.empty();
	}

}
