package yeelp.distinctdamagedescriptions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

@Mixin(CombatTracker.class)
public abstract class MixinCombatTracker {

	@Accessor("fighter")
	protected abstract EntityLivingBase getFighter();
	
	@Unique
	private static final int TICK_DELTA = 50;
	
	@Inject(method = "getDeathMessage()Lnet/minecraft/util/text/ITextComponent;", at = @At("HEAD"), cancellable = true)
	public void getDeathMessage(CallbackInfoReturnable<ITextComponent> info) {
		if(!ModConfig.core.useCustomDeathMessages) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(this.getFighter()).ifPresent((ct) -> ct.getLastCalculation(TICK_DELTA).ifPresent((calc) -> {
			calc.getType().flatMap((type) -> DDDRegistries.damageTypes.getDeathMessageForType(type, calc.getContext().getSource().getTrueSource(), this.getFighter())).ifPresent(info::setReturnValue);
		}));
	}
}
