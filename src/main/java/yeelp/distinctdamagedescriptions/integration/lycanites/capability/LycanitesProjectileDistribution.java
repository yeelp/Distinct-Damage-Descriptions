package yeelp.distinctdamagedescriptions.integration.lycanites.capability;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;

import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;

public final class LycanitesProjectileDistribution extends DamageDistribution {

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
	public IDamageDistribution update(IProjectile owner) {
		BaseProjectileEntity projectile = (BaseProjectileEntity) owner;
		if(projectile.entityName != null && !projectile.entityName.equals(this.proj)) {
			this.setNewWeights(copyMap((DamageDistribution) DDDConfigurations.projectiles.getOrFallbackToDefault(ModConsts.LYCANITES_ID.concat(":").concat(projectile.entityName))));
			this.proj = projectile.entityName;
		}
		return super.update(owner);
	}
	
	public static void register() {
		DDDCapabilityBase.register(LycanitesProjectileDistribution.class, NBTTagList.class, LycanitesProjectileDistribution::new);
	}

	@CapabilityInject(LycanitesProjectileDistribution.class)
	public static void onRegister(Capability<LycanitesProjectileDistribution> cap) {
		DDDAPI.mutator.registerProjectileCap(IDamageDistribution.class, cap);
	}
	
}
