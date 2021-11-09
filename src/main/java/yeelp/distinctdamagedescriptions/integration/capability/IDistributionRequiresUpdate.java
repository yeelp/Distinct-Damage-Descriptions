package yeelp.distinctdamagedescriptions.integration.capability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.handlers.Handler;

/**
 * Our hacky solution for items that need their distribution updated after creation.
 * @author Yeelp
 */
public interface IDistributionRequiresUpdate extends DDDCapabilityBase<NBTTagList> {

	final class PlayerHandler extends Handler {
		private static final List<Capability<? extends IDistributionRequiresUpdate>> caps = Lists.newArrayList();
		private static final Map<UUID, Integer> map = new HashMap<UUID, Integer>();
		
		@SuppressWarnings("static-method")
		@SubscribeEvent
		public final void onPlayerTick(final PlayerTickEvent evt) {
			if(evt.phase == Phase.END && map.compute(evt.player.getUniqueID(), (uuid, i) -> i == null ? 0 : i++ % 20) == 0) {
				EntityPlayer player = evt.player;
				InventoryPlayer inv = player.inventory;
				for(NonNullList<ItemStack> lst : ImmutableList.of(inv.armorInventory, inv.mainInventory, inv.offHandInventory)) {
					lst.stream().filter((s) -> !s.isEmpty()).forEach(PlayerHandler::checkAndUpdateCaps);
				}
			}
		}
		
		public static final <C extends IDistributionRequiresUpdate> void allowCapabilityUpdates(Capability<C> cap) {
			caps.add(cap);
		}
		
		private static void checkAndUpdateCaps(ItemStack stack) {
			caps.stream().filter((c) -> stack.hasCapability(c, null)).map((c) -> stack.getCapability(c, null)).filter(Objects::nonNull).forEach((d) -> d.applyDistributionToStack(stack));
		}
	}
	
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
	
	/**
	 * Register a capability
	 * @param <C> the capability
	 * @param capClass the capability class
	 * @param capSup the capability factory
	 */
	static <C extends IDistributionRequiresUpdate> void register(Class<C> capClass, Supplier<C> capSup) {
		DDDCapabilityBase.register(capClass, NBTTagList.class, capSup);
	}
}
