package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.melee.item.BattleSign;
import yeelp.distinctdamagedescriptions.handlers.AbstractTracker;

public class BattleSignTracker extends AbstractTracker {

	@Override
	public boolean shouldStartTracking(EntityLivingBase entity) {
		return checkBattleSignUsage(entity);
	}

	@Override
	public boolean shouldStopTracking(EntityLivingBase entity) {
		return !checkBattleSignUsage(entity);
	}

	@Override
	public String getName() {
		return "battlesign";
	}
	
	private static boolean checkBattleSignUsage(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack activeStack = player.getActiveItemStack();
			return activeStack.getItem() instanceof BattleSign && !ToolHelper.isBroken(activeStack);
		}
		return false;
	}

}
