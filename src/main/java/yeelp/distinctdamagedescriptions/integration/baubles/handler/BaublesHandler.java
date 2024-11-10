package yeelp.distinctdamagedescriptions.integration.baubles.handler;

import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.google.common.base.Predicates;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.baubles.BaublesConfigurations;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.IDDDBaubleModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class BaublesHandler extends Handler {

	private static final Predicate<ItemStack> IS_NON_EMPTY = Predicates.not(ItemStack::isEmpty);
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onDetermineDamage(DetermineDamageEvent evt) {
		processBaublesOnTarget(evt.getTrueAttacker(), evt, BaubleModifierType.DAMAGE_MOD);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onGatherDefenses(GatherDefensesEvent evt) {
		processBaublesOnTarget(evt.getDefender(), evt, BaubleModifierType.RESISTANCE_MOD, BaubleModifierType.IMMUNITY);
		processBaublesOnTarget(evt.getTrueAttacker(), evt, BaubleModifierType.BRUTE_FORCE, BaubleModifierType.SLY_STRIKE);
	}
	
	@SuppressWarnings("unchecked")
	private static <E extends Event> void processBaublesOnTarget(Entity entity, E evt, BaubleModifierType...types) {
		if(entity instanceof EntityPlayer) {
			IBaublesItemHandler handler = BaublesApi.getBaublesHandler((EntityPlayer) entity);
			IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot).filter(IS_NON_EMPTY).map(YResources::getRegistryString).forEach((opt) -> {
				opt.ifPresent((s) -> {
					for(BaubleModifierType type : types) {
						if(type.doesRespondToEvent(evt)) {
							BaublesConfigurations.baubleModifiers.getModifier(s, type).ifPresent((mod) -> ((IDDDBaubleModifier<E>) mod).respondToEvent(evt));						
						}
					}
				});
			});
		}
	}
}
