package yeelp.distinctdamagedescriptions.integration.lycanites.capability;

import java.util.Optional;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.ModUpdatingDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class LycanitesProjectileDistribution extends ModUpdatingDamageDistribution {

	@CapabilityInject(LycanitesProjectileDistribution.class)
	public static Capability<LycanitesProjectileDistribution> cap;

	private String proj;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public Optional<DDDBaseMap<Float>> getUpdatedWeights(IProjectile owner) {
		BaseProjectileEntity projectile = (BaseProjectileEntity) owner;
		if(projectile.entityName != null && !projectile.entityName.equals(this.proj)) {
			this.proj = projectile.entityName;
			return Optional.of(copyMap((DamageDistribution) DDDConfigurations.projectiles.getOrFallbackToDefault(ModConsts.IntegrationIds.LYCANITES_ID.concat(":").concat(projectile.entityName))));
		}
		return Optional.empty();
	}
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(EntityLivingBase owner) {
		return Optional.empty();
	}
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(ItemStack owner) {
		return Optional.empty();
	}

	public static void register() {
		DDDUpdatableCapabilityBase.register(LycanitesProjectileDistribution.class, LycanitesProjectileDistribution::new);
	}

	@CapabilityInject(LycanitesProjectileDistribution.class)
	public static void onRegister(Capability<LycanitesProjectileDistribution> cap) {
		DDDAPI.mutator.registerProjectileCap(IDamageDistribution.class, cap);
	}

}
