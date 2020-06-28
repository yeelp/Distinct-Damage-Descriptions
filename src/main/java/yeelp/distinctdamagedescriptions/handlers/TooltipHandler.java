package yeelp.distinctdamagedescriptions.handlers;

import java.util.List;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IDamageCategories;

public class TooltipHandler extends Handler
{
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		IDamageCategories damages = DDDAPI.accessor.getDamageCategories(evt.getItemStack());
		if(damages != null)
		{
			float slashing = damages.getDamage(DamageType.SLASHING);
			float piercing = damages.getDamage(DamageType.PIERCING);
			float bludgeoning = damages.getDamage(DamageType.BLUDGEONING);
			List<String> tooltips = evt.getToolTip();
			if(slashing > 0)
			{
				tooltips.add("Slashing: "+slashing);
			}
			if(piercing > 0)
			{
				tooltips.add("Piercing: "+piercing);
			}
			if(bludgeoning > 0)
			{
				tooltips.add("Bludgeoning: "+bludgeoning);
			}
			for(String s : tooltips)
			{
				if(s.matches(" [0-9]+([.][0-9]+)? Attack Damage"))
				{
					evt.getToolTip().remove(s);
				}
				//DistinctDamageDescriptions.info(s);
			}
		}
	}
}
