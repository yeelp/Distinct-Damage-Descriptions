package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public class DamageHandler extends Handler
{
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingAttackEvent evt)
	{
		Entity source = evt.getSource().getTrueSource();
		if(!(source instanceof EntityLivingBase))
		{
			return;
		}
		EntityLivingBase attacker = (EntityLivingBase) source;
		Item weapon = attacker.getHeldItemMainhand().getItem();
		if(weapon instanceof ItemSword)
		{
			DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, evt.getAmount());
			MinecraftForge.EVENT_BUS.post(slashEvent);
			
		}
	}
	
	@SubscribeEvent
	public void onSlash(DamageDescriptionEvent.SlashingDamage evt)
	{
		LivingAttackEvent atkevt = evt.getLivingAttackEvent();
		DistinctDamageDescriptions.info("is null: "+(atkevt == null));
		EntityLivingBase entity = atkevt.getEntityLiving();
		DistinctDamageDescriptions.info("entity null?: "+ (entity == null));
		entity.setDead();
	}
	
	@SubscribeEvent
	public void onPierce(DamageDescriptionEvent.PiercingDamage evt)
	{
		
	}
	
	@SubscribeEvent
	public void onSmack(DamageDescriptionEvent.BludgeoningDamage evt)
	{
		
	}
}
