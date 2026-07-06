package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import com.google.common.base.Functions;
import com.lycanitesmobs.core.entity.damagesources.MinionEntityDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.lycanites.LycanitesConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class LycanitesMinionDistribution extends DDDAbstractPredefinedDistribution {

	public LycanitesMinionDistribution() {
		super("minionProjectile", Source.BUILTIN);
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
		return getDamageDistribution(src).map(IDamageDistribution::getCategories).orElse(Collections.emptySet());
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
		return getDamageDistribution(src);
	}

	private static Optional<IDamageDistribution> getDamageDistribution(DamageSource src) {
		if(src instanceof MinionEntityDamageSource) {
			return YResources.getEntityIDString(src.getImmediateSource()).flatMap(Functions.compose((o) -> o.flatMap(DDDConfigurations.projectiles::getSafe), LycanitesConfigurations.creatureProjectiles::getSafe));
		}
		return Optional.empty();
	}

}
