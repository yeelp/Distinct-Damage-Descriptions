package yeelp.distinctdamagedescriptions.handlers;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.util.DDDAttributes;

public class DamageHandler extends Handler
{
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingHurtEvent evt)
	{
		Entity source = evt.getSource().getTrueSource();
		if(!(source instanceof EntityLivingBase))
		{
			return;
		}
		EntityLivingBase attacker = (EntityLivingBase) source;
		double slashing = DDDAPI.accessor.getSlashingDamage(attacker);
		double piercing = DDDAPI.accessor.getPiercingDamage(attacker);
		double bludgeoning = DDDAPI.accessor.getBludgeoningDamage(attacker);
		boolean classified = false;
		float totalDamage = 0;
		if(slashing > 0)
		{
			DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, (float) slashing);
			MinecraftForge.EVENT_BUS.post(slashEvent);
			double resist = DDDAPI.accessor.getSlashingResistance(evt.getEntityLiving());
			float dmg = slashEvent.getAmount();
			dmg -= dmg*resist;
			totalDamage += dmg;
			classified = true;
		}
		if(piercing > 0)
		{
			DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, (float) piercing);
			MinecraftForge.EVENT_BUS.post(pierceEvent);
			double resist = DDDAPI.accessor.getPiercingResistance(evt.getEntityLiving());
			float dmg = pierceEvent.getAmount();
			dmg -= dmg*resist;
			totalDamage += dmg;
			classified = true;
		}
		if(bludgeoning > 0)
		{
			DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, (float) bludgeoning);
			MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
			double resist = DDDAPI.accessor.getBludgeoningResistance(evt.getEntityLiving());
			float dmg = bludgeoningEvent.getAmount();
			dmg -= dmg*resist;
			totalDamage += dmg;
			classified = true;
		}
		DistinctDamageDescriptions.info("new damage: "+totalDamage);
		if(classified)
		{
			evt.setAmount(totalDamage);
		}
	}
	
	@SubscribeEvent
	public void onSlash(DamageDescriptionEvent.SlashingDamage evt)
	{
		
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
