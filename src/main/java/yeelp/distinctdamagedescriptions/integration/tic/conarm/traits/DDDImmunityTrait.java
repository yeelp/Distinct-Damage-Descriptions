package yeelp.distinctdamagedescriptions.integration.tic.conarm.traits;

import java.util.Iterator;

import c4.conarm.lib.tinkering.TinkersArmor;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.DDDInfoEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class DDDImmunityTrait extends AbstractArmorTrait {

	private final DDDDamageType type;
	
	public DDDImmunityTrait(DDDDamageType type) {
		super(type.getTypeName()+"_immunity", type.getColour());
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public DDDDamageType getType() {
		return this.type;
	}

	public static final class DamageHandler extends Handler {
		
		@SuppressWarnings("static-method")
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public final void onGatherImmunities(DDDInfoEvent.GatherImmunities evt) {
			for(ItemStack stack : evt.getEntity().getArmorInventoryList()) {
				if(stack.getItem() instanceof TinkersArmor && !ToolHelper.isBroken(stack)) {
					Iterator<NBTBase> it = TagUtil.getTraitsTagList(stack).iterator();
					while(it.hasNext()) {
						ITrait trait = TinkerRegistry.getTrait(((NBTTagString) it.next()).getString());
						if(trait instanceof DDDImmunityTrait) {
							DDDImmunityTrait immunity = (DDDImmunityTrait) trait;
							evt.immunities.add(immunity.getType());
						}
					}
				}
			}
		}
	}
}
