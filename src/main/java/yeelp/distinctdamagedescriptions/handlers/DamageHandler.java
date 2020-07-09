package yeelp.distinctdamagedescriptions.handlers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorResistances;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

public class DamageHandler extends Handler
{
	private Map<UUID, Boolean> shouldKnockback = new NonNullMap<UUID, Boolean>(false);
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingHurtEvent evt)
	{
		Entity source = evt.getSource().getTrueSource();
		if(!(source instanceof EntityLivingBase))
		{
			return;
		}
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource dmgSource = evt.getSource();
		DistinctDamageDescriptions.debug("starting damage: "+evt.getAmount());
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		Map<DamageType, Float> dmgMap = DDDAPI.accessor.classifyDamage(mobResists, dmgSource, evt.getAmount());
		Map<EntityEquipmentSlot, IArmorResistances> armors = DDDAPI.accessor.getArmorResistancesForEntity(defender);
		float[] resists = DDDAPI.accessor.getResistanceValuesForEntity(defender);
		DistinctDamageDescriptions.debug("Damage Total: ("+dmgMap.get(DamageType.SLASHING)+", "+dmgMap.get(DamageType.PIERCING)+", "+dmgMap.get(DamageType.BLUDGEONING)+"), Mob Resistances: ("+resists[0]+", "+resists[1]+", "+resists[2]+")");
		float[] absorb = new float[3];
		shouldKnockback.put(defender.getUniqueID(), dmgMap.size() == 0);
		boolean classified = false;
		float totalDamage = 0;
		if(dmgMap.containsKey(DamageType.SLASHING))
		{
			DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, dmgMap.get(DamageType.SLASHING));
			MinecraftForge.EVENT_BUS.post(slashEvent);
			float dmg = slashEvent.getAmount();
			float newDmg = modDmg(dmg, resists[0], resists[3]);
			absorb[0] = dmg - newDmg; 
			totalDamage += newDmg;
			classified = true;
		}
		if(dmgMap.containsKey(DamageType.PIERCING))
		{
			DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, dmgMap.get(DamageType.PIERCING));
			MinecraftForge.EVENT_BUS.post(pierceEvent);
			float dmg = pierceEvent.getAmount();
			float newDmg = modDmg(dmg, resists[1], resists[3]);
			absorb[1] = dmg - newDmg; 
			totalDamage += newDmg;
			classified = true;
		}
		if(dmgMap.containsKey(DamageType.BLUDGEONING))
		{
			DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, dmgMap.get(DamageType.BLUDGEONING));
			MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
			float dmg = bludgeoningEvent.getAmount();
			float newDmg = modDmg(dmg, resists[2], resists[3]);
			absorb[2] = dmg - newDmg; 
			totalDamage += newDmg;
			classified = true;
		}
		DistinctDamageDescriptions.debug("new damage: "+totalDamage);
		if(classified)
		{
			evt.setAmount(totalDamage);
			dmgSource.setDamageBypassesArmor();
			if(mobResists.hasAdaptiveImmunity())
			{
				mobResists.updateAdaptiveImmunity(dmgMap.keySet().toArray(new DamageType[0]));
			}
			for(ItemStack stack : defender.getArmorInventoryList())
			{
				Item item = stack.getItem();
				if(!(item instanceof ItemArmor || item instanceof ISpecialArmor))
				{
					continue;
				}
				ItemArmor armorItem = (ItemArmor) item;
				IArmorResistances armorResist = armors.get(armorItem.armorType);
				float damageAmount = 0;
				damageAmount += armorResist.getBludgeoningResistance() <= 0 && dmgMap.containsKey(DamageType.BLUDGEONING) ? 1 : armorResist.getBludgeoningResistance()/resists[2]*absorb[2];
				damageAmount += armorResist.getPiercingResistance() <= 0 && dmgMap.containsKey(DamageType.PIERCING) ? 1 : armorResist.getPiercingResistance()/resists[1]*absorb[1];
				damageAmount += armorResist.getSlashingResistance() <= 0 && dmgMap.containsKey(DamageType.SLASHING) ? 1 : armorResist.getSlashingResistance()/resists[0]*absorb[0];
				if(item instanceof ISpecialArmor)
				{
					ISpecialArmor armor = (ISpecialArmor) item;
					if(armor.handleUnblockableDamage(defender, stack, dmgSource, totalDamage, armorItem.getEquipmentSlot().ordinal()))
					{
						continue;
					}
					else
					{
						DistinctDamageDescriptions.debug("Damaging ISpecialArmor by: "+(int) damageAmount);
						armor.damageArmor(defender, stack, dmgSource, (int) damageAmount, armorItem.getEquipmentSlot().ordinal());
					}
				}
				else
				{
					DistinctDamageDescriptions.debug("Damaging ItemArmor by: "+(int) damageAmount);
					stack.damageItem((int) damageAmount, defender);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent evt)
	{
		UUID uuid = evt.getEntityLiving().getUniqueID();
		if(shouldKnockback.get(uuid))
		{
			evt.setCanceled(true);
			shouldKnockback.remove(uuid);
		}
	}
	
	private float modDmg(float damage, float armor, float toughness)
	{
		if(armor > 0)
		{
			return (float) Math.max(0, damage - damage*(Math.max(0, armor - 0.1*damage/(2+toughness/4))));
		}
		else if (armor < 0) 
		{
			return damage - damage*armor;
		}
		else
		{
			return damage;
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
