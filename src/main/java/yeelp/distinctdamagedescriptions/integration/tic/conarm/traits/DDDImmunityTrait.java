package yeelp.distinctdamagedescriptions.integration.tic.conarm.traits;

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
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class DDDImmunityTrait extends AbstractArmorTrait {

	private final DDDDamageType type;

	public DDDImmunityTrait(DDDDamageType type) {
		super(type.getTypeName() + "_immunity", type.getColour());
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
		public void onGatherImmunities(GatherDefensesEvent evt) {
			for(ItemStack stack : evt.getDefender().getArmorInventoryList()) {
				if(stack.getItem() instanceof TinkersArmor && !ToolHelper.isBroken(stack)) {
                    for(NBTBase nbtBase : TagUtil.getTraitsTagList(stack)) {
                        ITrait trait = TinkerRegistry.getTrait(((NBTTagString) nbtBase).getString());
                        if(trait instanceof DDDImmunityTrait) {
                            DDDImmunityTrait immunity = (DDDImmunityTrait) trait;
                            evt.addImmunity(immunity.getType());
                            ToolHelper.damageTool(stack, 1, evt.getDefender());
                        }
                    }
				}
			}
		}
	}
}
