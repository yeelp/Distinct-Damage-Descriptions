package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticle;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticleType;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.util.DDDCombatRules;
import yeelp.distinctdamagedescriptions.util.DDDCombatRules.CombatResults;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class DamageHandler extends Handler
{
	private static Set<UUID> noKnockback = new HashSet<UUID>();
	private static Map<UUID, ShieldDistribution> activeShield = new HashMap<UUID, ShieldDistribution>();
	private static Random particleDisplacement = new Random();
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent evt)
	{
		if(ModConfig.dmg.useCustomDeathMessages)
		{
			DamageSource source = evt.getSource();
			if(ModConfig.dmg.useCustomDamageTypes)
			{
				source = DDDAPI.accessor.getDamageContext(source);
			}
			if(evt.getEntityLiving() instanceof EntityPlayerMP)
			{
				((EntityPlayerMP) evt.getEntityLiving()).mcServer.getPlayerList().sendMessage(source.getDeathMessage(evt.getEntityLiving()));
			}
			else if(evt.getEntityLiving() instanceof EntityTameable)
			{
				EntityTameable entity = (EntityTameable) evt.getEntityLiving();
				if(entity.isTamed())
				{
					entity.getOwner().sendMessage(source.getDeathMessage(evt.getEntityLiving()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onAttack(LivingAttackEvent evt)
	{
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource src = DDDAPI.accessor.getDamageContext(evt.getSource());
		Entity attacker = src.getImmediateSource();
		DDDCombatRules.setModifiers(src, defender);
		if(src instanceof DDDDamageType)
		{
			//Let the damage bypass armor so the shield can't block it normally.
			evt.getSource().setDamageBypassesArmor();
			if(attacker instanceof EntityLivingBase && defender instanceof EntityPlayer)
			{
				EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
				EntityPlayer defendingPlayer = (EntityPlayer) defender;
				ItemStack weapon = livingAttacker.getHeldItemMainhand();
				if(defender.isActiveItemStackBlocking() && weapon.getItem().canDisableShield(weapon, defender.getActiveItemStack(), defender, livingAttacker))
				{
					defendingPlayer.disableShield(true);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onHit(LivingHurtEvent evt)
	{
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource dmgSource = evt.getSource();
		Entity attacker = dmgSource.getImmediateSource();
		if(ModConfig.showDotsOn)
		{
			Entity trueAttacker = dmgSource.getTrueSource();
			ResourceLocation attackerLoc = attacker == null || attacker instanceof EntityPlayer ? null : EntityList.getKey(attacker), defendLoc = defender == null ? null : EntityList.getKey(defender), trueAttackerLoc = trueAttacker == null ? null : EntityList.getKey(trueAttacker);
			String s1 = attackerLoc != null ? attackerLoc.toString() : "null", s2 = defendLoc != null ? defendLoc.toString() : "null", s3 = trueAttackerLoc != null ? trueAttackerLoc.toString() : "null";
			DistinctDamageDescriptions.debug("Damage Type: "+ dmgSource.damageType+" Attacker: "+ (attacker != null && attacker instanceof EntityPlayer ? "player" : s1)+", True Attacker: "+s3+" Defender: "+s2);
		}
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		Map<String, Float> dmgMap = DDDAPI.accessor.classifyDamage(dmgSource, evt.getAmount());
		DistinctDamageDescriptions.debug("starting damage: "+evt.getAmount());
		DistinctDamageDescriptions.debug("Damage Total: "+DebugLib.entriesToString(dmgMap));
		if(dmgMap == null)// couldn't classify damage, so further calculations are meaningless/undefined. Return, let vanilla handle the rest,
		{
			return;
		}
		Map<String, Float> resistMap = DDDAPI.accessor.classifyResistances(dmgMap.keySet(), mobResists);
		Map<String, Tuple<Float, Float>> armors = DDDCombatRules.getApplicableArmorValues(attacker, defender);
		DamageDescriptionEvent.Pre pre = new DamageDescriptionEvent.Pre(attacker, defender, dmgMap, resistMap, armors);
		MinecraftForge.EVENT_BUS.post(pre);
		if(pre.isCanceled())
		{
			return;
		}
		CombatResults results = DDDCombatRules.computeNewDamage(attacker, defender, pre.getAllDamages(), pre.getAllResistances(), pre.getAllArmor(), mobResists);
		//determine if knockback should occur
		if(results.wasImmunityTriggered())
		{
			noKnockback.add(defender.getUniqueID());
			for(Float f : results.getDamage().values())
			{
				if(f != null && f.floatValue() > 0)
				{
					noKnockback.remove(defender.getUniqueID());
					break;
				}
			}
		}
		else if(results.wasShieldEffective())
		{
			noKnockback.add(defender.getUniqueID());
		}
		
		//One more reduction for natural armor/enchants/potions
		float totalDamage = CombatRules.getDamageAfterAbsorb((float) YMath.sum(results.getDamage().values()), (float) defender.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue(), (float) defender.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		int enchantMods = EnchantmentHelper.getEnchantmentModifierDamage(defender.getArmorInventoryList(), dmgSource);
		if(enchantMods > 0)
		{
			totalDamage = CombatRules.getDamageAfterMagicAbsorb(totalDamage, enchantMods);
		}
		DamageDescriptionEvent.Post post = new DamageDescriptionEvent.Post(attacker, defender, results.getDamage(), results.getResistances(), results.getArmor());
		MinecraftForge.EVENT_BUS.post(post);
		totalDamage = (float) YMath.sum(post.getAllDamages().values());
		DistinctDamageDescriptions.debug("new damage after deductions: "+totalDamage);
		boolean resistancesUpdated = false; 
		if(mobResists.hasAdaptiveResistance())
		{
			resistancesUpdated = mobResists.updateAdaptiveResistance(dmgMap.keySet().toArray(new String[0]));
			DistinctDamageDescriptions.debug("Updating mob's adaptive resistance, since it is present...");
			
		}
		//Only spawn particles and play sounds for players.
		if(dmgSource.getTrueSource() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) dmgSource.getTrueSource();
			evt.setCanceled(doEffects(player, defender, results, MathHelper.clamp(totalDamage/evt.getAmount(), 0, Float.MAX_VALUE)) && ModConfig.dmg.cancelLivingHurtEventOnImmunity);
			if(resistancesUpdated)
			{
				DDDSounds.playSound(player, DDDSounds.ADAPTABILITY_CHANGE, 2.0f, 1.0f);
			}
		}
		evt.setAmount(totalDamage < 0 ? 0 : totalDamage);
		dmgSource.setDamageBypassesArmor();
		DDDCombatRules.wipeModifiers(attacker, defender);
	}
	
	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent evt)
	{
		UUID uuid = evt.getEntityLiving().getUniqueID();
		if(noKnockback.contains(uuid))
		{
			evt.setCanceled(noKnockback.remove(uuid));
		}
	}
	
	/*
	 * Return true if immunity blocked all damage.
	 */
	private static boolean doEffects(EntityPlayerMP player, EntityLivingBase defender, CombatResults results, float ratio)
	{
		if(results.wasResistanceHit())
		{
			DDDParticle.spawnRandomAmountOfParticles(player, defender, DDDParticleType.RESISTANCE);
		}
		if(results.wasWeaknessHit())
		{
			DDDParticle.spawnRandomAmountOfParticles(player, defender, DDDParticleType.WEAKNESS);
		}
		if(results.wasImmunityTriggered())
		{
			DDDParticle.spawnRandomAmountOfParticles(player, defender, DDDParticleType.IMMUNITY);
			DDDSounds.playSound(player, DDDSounds.IMMUNITY_HIT, 1.5f, 1.0f);
			return ratio == 0;
		}
		else if(ratio != 0)
		{
			if(ratio > 1)
			{
				DDDSounds.playSound(player, DDDSounds.WEAKNESS_HIT, 0.6f, 1.0f);
			}
			else if(ratio < 1)
			{
				DDDSounds.playSound(player, DDDSounds.RESIST_DING, 1.7f, 1.0f);
			}
		}
		else
		{
			DDDSounds.playSound(player, DDDSounds.HIGH_RESIST_HIT, 1.7f, 1.0f);
		}
		return false;
	}
}
