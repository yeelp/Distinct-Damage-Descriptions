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
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
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
		Map<DamageType, Tuple<Float, Float>> armors = DDDAPI.accessor.getArmorValuesForEntity(defender);
		DistinctDamageDescriptions.debug("Damage Total: ("+dmgMap.get(DamageType.SLASHING)+", "+dmgMap.get(DamageType.PIERCING)+", "+dmgMap.get(DamageType.BLUDGEONING)+")");
		float[] absorb = new float[3];
		shouldKnockback.put(defender.getUniqueID(), dmgMap.size() == 0);
		boolean classified = false;
		float totalDamage = 0;
		for(DamageType type : DamageType.values())
		{
			if(dmgMap.containsKey(type))
			{
				float dmg = dmgMap.get(type);
				switch(type)
				{
					case SLASHING:
						DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(slashEvent);
						dmg = slashEvent.getAmount();
						break;
					case PIERCING:
						DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(pierceEvent);
						dmg = pierceEvent.getAmount();
						break;
					case BLUDGEONING:
						DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
						dmg = bludgeoningEvent.getAmount();
						break;
				}
				Tuple<Float, Float> resists = armors.get(type);
				float newDmg = modDmg(dmg, resists.getFirst(), resists.getSecond());
				absorb[type.ordinal()] = dmg - newDmg; 
				totalDamage += newDmg;
				classified = true;
			}
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
				int damageAmount = (int) MathHelper.clamp(Math.floor((absorb[0] + absorb[1] + absorb[2])/4.0f), 1.0f, Float.MAX_VALUE);
				if(item instanceof ISpecialArmor)
				{
					ISpecialArmor armor = (ISpecialArmor) item;
					if(armor.handleUnblockableDamage(defender, stack, dmgSource, totalDamage, armorItem.getEquipmentSlot().ordinal()))
					{
						continue;
					}
					else
					{
						DistinctDamageDescriptions.debug("Damaging ISpecialArmor by: "+damageAmount);
						armor.damageArmor(defender, stack, dmgSource, damageAmount, armorItem.getEquipmentSlot().ordinal());
					}
				}
				else
				{
					DistinctDamageDescriptions.debug("Damaging ItemArmor by: "+damageAmount);
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
		return damage*(1-Math.min(20, Math.max(armor/5.0f, armor - damage/(2+toughness/4.0f)))/25.0f);
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
