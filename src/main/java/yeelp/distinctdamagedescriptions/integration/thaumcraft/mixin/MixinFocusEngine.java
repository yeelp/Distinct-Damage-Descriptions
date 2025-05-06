package yeelp.distinctdamagedescriptions.integration.thaumcraft.mixin;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Predicates;

import net.minecraft.util.math.RayTraceResult;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.casters.Trajectory;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.ThaumcraftFocusTracker;

@Mixin(value = FocusEngine.class, remap = false)
public abstract class MixinFocusEngine {

	@Inject(method = "runFocusPackage(Lthaumcraft/api/casters/FocusPackage;[Lthaumcraft/api/casters/Trajectory;[Lnet/minecraft/util/math/RayTraceResult;)V", at = @At("HEAD"))
	private static void analyzeFocusPackage(FocusPackage focusPackage, @SuppressWarnings("unused") Trajectory[] trajectories, RayTraceResult[] targets, @SuppressWarnings("unused") CallbackInfo info) {
		DistinctDamageDescriptions.debug("In Thaumcraft runFocusPackage!");
		if(targets != null && targets.length > 0) {
			ThaumcraftFocusTracker.getInstance().trackAspects(focusPackage, targets, Arrays.stream(focusPackage.getFocusEffects()).map(FocusEffect::getAspect).filter(Predicates.notNull()).collect(Collectors.toSet()));
		}
	}
	
	@Inject(method = "runFocusPackage(Lthaumcraft/api/casters/FocusPackage;[Lthaumcraft/api/casters/Trajectory;[Lnet/minecraft/util/math/RayTraceResult;)V", at = @At("TAIL"))
	private static void focusPackageCleanup(@SuppressWarnings("unused") FocusPackage focusPackage, @SuppressWarnings("unused") Trajectory[] trajectories, @SuppressWarnings("unused") RayTraceResult[] targets, @SuppressWarnings("unused") CallbackInfo info) {
		DistinctDamageDescriptions.debug("Leaving Thaumcraft runFocusPackage!");
	}
}
