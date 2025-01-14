package yeelp.distinctdamagedescriptions.mixin;

import java.util.Optional;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DamageCalculation;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICommandSender, net.minecraftforge.common.capabilities.ICapabilitySerializable<NBTTagCompound> {

	@Unique
	private static final Predicate<IMobCreatureType> NOT_FLAMMABLE = (type) -> !type.isFlammable();

	@Unique
	private static final int TICK_DELTA = 10;

	@ModifyVariable(method = "setFire(I)V", at = @At(value = "STORE"), name = "i")
	private int setFire(int ticks) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof EntityLivingBase) {
			EntityLivingBase elb = (EntityLivingBase) entity;
			Optional<IMobCreatureType> type = DDDAPI.accessor.getMobCreatureType(elb);
			if(type.filter(NOT_FLAMMABLE).isPresent()) {
				entity.extinguish();
				return 0;
			}
			//@formatter:off
			return DDDAPI.accessor.getDDDCombatTracker(elb)
					.flatMap((tracker) -> tracker.getLastCalculation(TICK_DELTA)
							.flatMap(DamageCalculation::getShieldDist)
							.map((dist) -> ModConfig.resist.shieldFireRule.alterFireTicks(ticks, dist, tracker.getRecentResults())))
					.orElse(ticks);
			//@formatter:on
		}
		return ticks;
	}

	@Inject(method = "isBurning()Z", at = @At("HEAD"), cancellable = true)
	private void isBurning(CallbackInfoReturnable<Boolean> ci) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof EntityLivingBase) {
			DDDAPI.accessor.getMobCreatureType((EntityLivingBase) entity).ifPresent((type) -> {
				if(!type.isFlammable()) {
					entity.extinguish();
					ci.setReturnValue(false);
				}
			});
		}
	}
}
