package yeelp.distinctdamagedescriptions.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

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
		Optional<IDDDCombatTracker> tracker = DDDAPI.accessor.getDDDCombatTracker(this.getFighter());
		DebugLib.outputFormattedDebug("Combat Tracker %spresent for death message", tracker.isPresent() ? "" : "NOT ");
		tracker.ifPresent((ct) -> ct.getLastCalculation(TICK_DELTA).ifPresent((calc) -> {
			DistinctDamageDescriptions.debug("Found last calculation for death message.");
			if(calc.getType().isPresent()) {
				DistinctDamageDescriptions.debug("Has damage type for death message");
				calc.getType().flatMap((type) -> DDDRegistries.damageTypes.getDeathMessageForType(type, calc.getContext().getSource().getTrueSource(), this.getFighter())).map(ITextComponent::getFormattedText).ifPresent(DistinctDamageDescriptions::debug);
			}
			
			calc.getType().flatMap((type) -> DDDRegistries.damageTypes.getDeathMessageForType(type, calc.getContext().getSource().getTrueSource(), this.getFighter())).ifPresent(info::setReturnValue);
		}));
	}
}
