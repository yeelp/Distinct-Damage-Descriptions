package yeelp.distinctdamagedescriptions.integration.bettersurvival.capability;

import java.util.Optional;

import com.mujmajnkraft.bettersurvival.entities.projectiles.EntityFlyingSpear;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.ModUpdatingDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class BetterSurvivalThrownSpearDistribution extends ModUpdatingDamageDistribution {

	@CapabilityInject(BetterSurvivalThrownSpearDistribution.class)
	public static Capability<BetterSurvivalThrownSpearDistribution> cap;
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(ItemStack owner) {
		return Optional.empty();
	}

	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(IProjectile owner) {
		if(owner instanceof EntityFlyingSpear) {
			return DDDAPI.accessor.getDamageDistribution(((EntityFlyingSpear) owner).getSpear()).map((d) -> d.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, d::getWeight)));
		}
		return Optional.empty();
	}

	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(EntityLivingBase owner) {
		return Optional.empty();
	}
	
	public static void register() {
		DDDUpdatableCapabilityBase.register(BetterSurvivalThrownSpearDistribution.class, BetterSurvivalThrownSpearDistribution::new);
	}

	@CapabilityInject(BetterSurvivalThrownSpearDistribution.class)
	public static void onRegister(Capability<BetterSurvivalThrownSpearDistribution> cap) {
		DDDAPI.mutator.registerProjectileCap(IDamageDistribution.class, cap);
	}
}
