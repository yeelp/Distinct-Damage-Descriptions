package yeelp.distinctdamagedescriptions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDAPI;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICommandSender, net.minecraftforge.common.capabilities.ICapabilitySerializable<NBTTagCompound> {

	
	@Inject(method = "setFire(I)V", at = @At("HEAD"), cancellable = true)
	private void setFire(@SuppressWarnings("unused") int seconds, CallbackInfo ci) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof EntityLivingBase) {
			DDDAPI.accessor.getMobCreatureType((EntityLivingBase) entity).ifPresent((type) -> {
				if(!type.isFlammable()) {
					entity.extinguish();
					ci.cancel();
				}
			});
		}
	}
	
	@Inject(method = "isBurning()Z", at = @At("HEAD"), cancellable = true)
	private void isBurning(CallbackInfoReturnable<Boolean> ci) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof EntityLivingBase) {
			DDDAPI.accessor.getMobCreatureType((EntityLivingBase) entity).ifPresent((type) -> {
				if(!type.isFlammable()) {
					ci.setReturnValue(false);
				}
			});
		}
	}
}
