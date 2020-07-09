package yeelp.distinctdamagedescriptions.handlers;

import java.util.List;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.KeyHelper;

public class TooltipHandler extends Handler
{
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		IDamageDistribution damages = DDDAPI.accessor.getDamageDistribution(evt.getItemStack());
		if(damages != null)
		{
			if(KeyHelper.isShiftHeld())
			{
				float slashPercent = damages.getSlashingWeight()*100;
				float piercePercent = damages.getPiercingWeight()*100;
				float bludgePercent = damages.getBludgeoningWeight()*100;
				List<String> tooltips = evt.getToolTip();
				if(slashPercent > 0)
				{
					tooltips.add(makeDamagePercentTooltip(slashPercent, "Slashing Damage"));
				}
				if(piercePercent > 0)
				{
					tooltips.add(makeDamagePercentTooltip(piercePercent, "Piercing Damage"));
				}
				if(bludgePercent > 0)
				{
					tooltips.add(makeDamagePercentTooltip(bludgePercent, "Bludgeoning Damage"));
				}
			}
			else
			{
				evt.getToolTip().add("Damage Distribution: <SHIFT>");
			}
		}
	}

	private String makeDamagePercentTooltip(float percent, String string)
	{
		return String.format("%.2f%% %s", percent, string);
	}
}
