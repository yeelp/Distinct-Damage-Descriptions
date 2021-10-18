package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import slimeknights.tconstruct.tools.melee.item.BattleSign;
import yeelp.distinctdamagedescriptions.event.DDDInfoEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class TinkersBattleSignHandler extends Handler {
	
	public final void onShieldBlock(DDDInfoEvent.ShieldBlock evt) {
		if(evt.getBlockingShield().getItem() instanceof BattleSign) {
			BattleSign battleSign = (BattleSign) evt.getBlockingShield().getItem();
		}
	}
}
