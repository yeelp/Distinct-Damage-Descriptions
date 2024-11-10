package yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability;

import java.util.Optional;

import com.oblivioussp.spartanweaponry.entity.projectile.EntityThrownWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.ModUpdatingDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class SpartanThrownWeaponDistribution extends ModUpdatingDamageDistribution {

	@CapabilityInject(SpartanThrownWeaponDistribution.class)
	public static Capability<SpartanThrownWeaponDistribution> cap;

	@Override
	public Optional<DDDBaseMap<Float>> getUpdatedWeights(IProjectile owner) {
		if(owner instanceof EntityThrownWeapon) {
			return DDDAPI.accessor.getDamageDistribution(((EntityThrownWeapon) owner).getWeaponStack()).map((dist) -> dist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, dist::getWeight)));
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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	public static void register() {
		DDDUpdatableCapabilityBase.register(SpartanThrownWeaponDistribution.class, SpartanThrownWeaponDistribution::new);
	}

	@CapabilityInject(SpartanThrownWeaponDistribution.class)
	private static void onRegister(Capability<SpartanThrownWeaponDistribution> cap) {
		DDDAPI.mutator.registerProjectileCap(IDamageDistribution.class, cap);
	}
}
