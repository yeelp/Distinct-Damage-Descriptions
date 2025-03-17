package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import electroblob.wizardry.entity.living.EntityMagicSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public final class WizardrySlimeDistribution extends DDDAbstractPredefinedDistribution {
	public WizardrySlimeDistribution() {
		super("wizardry slime", Source.BUILTIN);
	}

	@Override
	public boolean enabled() {
		return true;
	}
	
	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		if(src == DamageSource.MAGIC) {
			getSlimeDamageDistribution(target).map(IDamageDistribution::getCategories).orElse(Collections.emptySet());
		}
		return Collections.emptySet();
	}
	
	
	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		if(src == DamageSource.MAGIC) {
			return getSlimeDamageDistribution(target); 
		}
		return Optional.empty();
	}
	
	private static Optional<IDamageDistribution> getSlimeDamageDistribution(EntityLivingBase target) {
		Entity entity = target.getLowestRidingEntity();
		if(entity instanceof EntityMagicSlime && entity.ticksExisted % 16 == 1) {
			return DDDAPI.accessor.getDamageDistribution((EntityLivingBase) entity);
		}
		return Optional.empty();
	}
}
