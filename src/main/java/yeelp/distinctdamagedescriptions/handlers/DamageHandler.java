package yeelp.distinctdamagedescriptions.handlers;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.IArmorResistances;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;

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
		EntityLivingBase defender = evt.getEntityLiving();
		ItemStack heldItem = attacker.getHeldItemMainhand();
		boolean hasEmptyHand = heldItem.isEmpty();
		double baseDmg = evt.getAmount(); 	
		double weaponDmg = 0; 
		if(!hasEmptyHand)
		{
			if(attacker instanceof EntityPlayer)
			{
				baseDmg = 0;
				weaponDmg = evt.getAmount();
			}
			else
			{
				baseDmg = attacker.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				weaponDmg = hasEmptyHand ? 0 : evt.getAmount() - baseDmg;
			}
		}
		IDamageDistribution mobDist = DDDAPI.accessor.getDamageDistribution(attacker);
		IDamageDistribution weaponDist = DDDAPI.accessor.getDamageDistribution(heldItem);
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		float[] resists = DDDAPI.accessor.getArmorResistanceValuesForEntity(defender);
		DamageCategories mobDmgCat = mobDist.distributeDamage((float) baseDmg);
		DamageCategories weaponDmgCat = weaponDist == null ? new DamageCategories(0, 0, 0) : weaponDist.distributeDamage((float) weaponDmg);
		float slashing = mobDmgCat.getSlashingDamage() + weaponDmgCat.getSlashingDamage();
		float piercing = mobDmgCat.getPiercingDamage() + weaponDmgCat.getPiercingDamage();
		float bludgeoning = mobDmgCat.getBludgeoningDamage() + weaponDmgCat.getBludgeoningDamage();
		
		boolean classified = false;
		float totalDamage = 0;
		if(slashing > 0 && !mobResists.isSlashingImmune())
		{
			DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, (float) slashing);
			MinecraftForge.EVENT_BUS.post(slashEvent);
			float dmg = slashEvent.getAmount();
			dmg = modDmg(dmg, resists[0], resists[3]);
			totalDamage += dmg;
			classified = true;
		}
		if(piercing > 0 && !mobResists.isPiercingImmune())
		{
			DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, (float) piercing);
			MinecraftForge.EVENT_BUS.post(pierceEvent);
			float dmg = pierceEvent.getAmount();
			dmg = modDmg(dmg, resists[1], resists[3]);
			totalDamage += dmg;
			classified = true;
		}
		if(bludgeoning > 0 && !mobResists.isBludgeoningImmune())
		{
			DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, (float) bludgeoning);
			MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
			float dmg = bludgeoningEvent.getAmount();
			dmg = modDmg(dmg, resists[2], resists[3]);
			totalDamage += dmg;
			classified = true;
		}
		DistinctDamageDescriptions.info("new damage: "+totalDamage);
		if(classified)
		{
			evt.setAmount(totalDamage);
			evt.getSource().setDamageBypassesArmor();
			//TODO damage armor, as bypassing armor prevents it from being damaged, or change the way this is done. Need to figure out how armor is damaged.
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
