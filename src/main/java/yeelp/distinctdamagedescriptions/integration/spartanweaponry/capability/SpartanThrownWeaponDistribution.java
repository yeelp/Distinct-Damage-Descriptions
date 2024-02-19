package yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability;

import com.oblivioussp.spartanweaponry.entity.projectile.EntityThrownWeapon;

import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class SpartanThrownWeaponDistribution extends DamageDistribution {

	@CapabilityInject(SpartanThrownWeaponDistribution.class)
	public static Capability<SpartanThrownWeaponDistribution> cap;

	@Override
	public IDamageDistribution update(IProjectile owner) {
		if(owner instanceof EntityThrownWeapon) {
			DDDAPI.accessor.getDamageDistribution(((EntityThrownWeapon) owner).getWeaponStack()).ifPresent((dist) -> this.setNewWeights(dist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, dist::getWeight))));
		}
		return super.update(owner);
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
		DDDCapabilityBase.register(SpartanThrownWeaponDistribution.class, NBTTagList.class, SpartanThrownWeaponDistribution::new);
	}

	@CapabilityInject(SpartanThrownWeaponDistribution.class)
	private static void onRegister(Capability<SpartanThrownWeaponDistribution> cap) {
		DDDAPI.mutator.registerProjectileCap(IDamageDistribution.class, cap);
	}
}
