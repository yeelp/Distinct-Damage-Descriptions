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
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IDamageCategories;
import yeelp.distinctdamagedescriptions.util.ResistancesAttributes;

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
		DistinctDamageDescriptions.info("entity null?: "+(attacker == null));
		IDamageCategories damage = DDDAPI.accessor.getDamageCategories(attacker.getHeldItemMainhand());
		DistinctDamageDescriptions.info("damage null?: "+(damage == null));
		if(damage != null)
		{
			float slashing = damage.getDamage(DamageType.SLASHING);
			float piercing = damage.getDamage(DamageType.PIERCING);
			float bludgeoning = damage.getDamage(DamageType.BLUDGEONING);
			float totalDamage = damage.getTotalDamage() == 0 ? evt.getAmount() : 0;
			if(slashing > 0)
			{
				DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, slashing);
				MinecraftForge.EVENT_BUS.post(slashEvent);
				double resist = DDDAPI.accessor.getSlashingResistance(evt.getEntityLiving());
				totalDamage += slashEvent.getAmount()/resist;
			}
			if(piercing > 0)
			{
				DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, piercing);
				MinecraftForge.EVENT_BUS.post(pierceEvent);
				double resist = DDDAPI.accessor.getPiercingResistance(evt.getEntityLiving());
				totalDamage += pierceEvent.getAmount()/resist;
			}
			if(bludgeoning > 0)
			{
				DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, bludgeoning);
				MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
				double resist = DDDAPI.accessor.getBludgeoningResistance(evt.getEntityLiving());
				totalDamage += bludgeoningEvent.getAmount()/resist;
			}
			DistinctDamageDescriptions.info("new damage: "+totalDamage);
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
