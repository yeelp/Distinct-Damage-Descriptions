package yeelp.distinctdamagedescriptions.capability;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.ConarmArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.LycanitesEquipmentDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.TinkerToolDistribution;
import yeelp.distinctdamagedescriptions.handlers.Handler;

/**
 * Our hacky solution for items that need their distribution updated after creation.
 * @author Yeelp
 */
public interface IDistributionRequiresUpdate extends DDDCapabilityBase<NBTTagByte> {

	final class PlayerHandler extends Handler {
		private static final List<Capability<? extends IDistributionRequiresUpdate>> caps = Lists.newArrayList(TinkerToolDistribution.Tool.cap, TinkerToolDistribution.Shield.cap, ConarmArmorDistribution.cap, LycanitesEquipmentDistribution.cap);
		private static final Predicate<IDistributionRequiresUpdate> filter = Predicates.and(Objects::nonNull, Predicates.not(IDistributionRequiresUpdate::updated));
		
		@SuppressWarnings("static-method")
		@SubscribeEvent
		public final void onPlayerTick(final PlayerTickEvent evt) {
			if(evt.phase == Phase.END) {
				EntityPlayer player = evt.player;
				InventoryPlayer inv = player.inventory;
				for(NonNullList<ItemStack> lst : ImmutableList.of(inv.armorInventory, inv.mainInventory, inv.offHandInventory)) {
					lst.stream().forEach(PlayerHandler::checkAndUpdateCaps);
				}
			}
		}
		
		private static void checkAndUpdateCaps(ItemStack stack) {
			caps.stream().filter((c) -> stack.hasCapability(c, null)).map((c) -> stack.getCapability(c, null)).filter(filter).forEach((d) -> d.applyDistributionToStack(stack));
		}
	}
	
	/**
	 * If this distribution has been updated on the stack correctly
	 * @return true if updated.
	 */
	boolean updated();
	
	/**
	 * Mark this cap as needing to recalculate
	 */
	void markForUpdate();
	
	/**
	 * Get the updated distribution to apply to the ItemStack
	 * @param stack the stack to calculate the new distribution for.
	 * @return The updated distribution map if it's different
	 */
	Optional<Map<DDDDamageType, Float>> getUpdatedDistribution(ItemStack stack);
	
	/**
	 * Apply the updated distribution to the ItemStack
	 * @param stack the stack to apply it to.
	 */
	void applyDistributionToStack(ItemStack stack);
}
