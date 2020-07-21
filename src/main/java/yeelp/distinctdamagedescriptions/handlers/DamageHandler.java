package yeelp.distinctdamagedescriptions.handlers;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.client.render.patricle.DDDParticle;
import yeelp.distinctdamagedescriptions.client.render.patricle.DDDParticleType;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

public class DamageHandler extends Handler
{
	private static Map<UUID, Boolean> shouldKnockback = new NonNullMap<UUID, Boolean>(false);
	private static Random particleDisplacement = new Random();
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingHurtEvent evt)
	{
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource dmgSource = evt.getSource();
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		Map<DamageType, Float> dmgMap = DDDAPI.accessor.classifyDamage(mobResists, dmgSource, evt.getAmount());
		if(dmgMap == null)
		{
			return;
		}
		DistinctDamageDescriptions.debug("starting damage: "+evt.getAmount());
		boolean bootsOnly = false;
		boolean helmetOnly = false;
		boolean applyAnvilReductionCap = false;
		float bruteForceAmount = 0;
		if(dmgSource == DamageSource.FALL)
		{
			bootsOnly = true;
		}
		else if(dmgSource == DamageSource.ANVIL)
		{
			helmetOnly = true;
			applyAnvilReductionCap = true;
		}
		else if(dmgSource == DamageSource.FALLING_BLOCK)
		{
			helmetOnly = true;
		}
		else if(dmgSource.getImmediateSource() instanceof EntityLivingBase)
		{
			bruteForceAmount = 0.1f*EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.bruteForce, (EntityLivingBase) dmgSource.getImmediateSource());
		}
		Map<DamageType, Tuple<Float, Float>> armors = DDDAPI.accessor.getArmorValuesForEntity(defender, bootsOnly, helmetOnly);
		DistinctDamageDescriptions.debug("Damage Total: ("+dmgMap.get(DamageType.SLASHING)+", "+dmgMap.get(DamageType.PIERCING)+", "+dmgMap.get(DamageType.BLUDGEONING)+")");
		float[] absorb = new float[3];
		boolean shouldKnockbackFlag = true;
		boolean immunityResisted = false;
		boolean resisted = false;
		boolean weakness = false;
		for(Float f : dmgMap.values())
		{
			if(f != null && f.floatValue() > 0)
			{
				shouldKnockbackFlag = true;
				break;
			}
		}
		shouldKnockback.put(defender.getUniqueID(), shouldKnockbackFlag);
		float totalDamage = 0;
		for(DamageType type : DamageType.values())
		{
			if(dmgMap.containsKey(type))
			{
				float dmg = dmgMap.get(type);
				if(dmg < 0)
				{
					immunityResisted = true;
					continue;
				}
				float mobMod = 0.0f;
				switch(type)
				{
					case SLASHING:
						DamageDescriptionEvent.SlashingDamage slashEvent = new DamageDescriptionEvent.SlashingDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(slashEvent);
						dmg = slashEvent.getAmount();
						mobMod = mobResists.getSlashingResistance();
						break;
					case PIERCING:
						DamageDescriptionEvent.PiercingDamage pierceEvent = new DamageDescriptionEvent.PiercingDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(pierceEvent);
						dmg = pierceEvent.getAmount();
						mobMod = mobResists.getPiercingResistance();
						break;
					case BLUDGEONING:
						DamageDescriptionEvent.BludgeoningDamage bludgeoningEvent = new DamageDescriptionEvent.BludgeoningDamage(evt, dmgMap.get(type));
						MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
						dmg = bludgeoningEvent.getAmount();
						mobMod = mobResists.getBludgeoningResistance();
						break;
				}
				if(mobMod > 0)
				{
					mobMod = MathHelper.clamp(mobMod - bruteForceAmount, 0, Float.MAX_VALUE);
				}
				Tuple<Float, Float> resists = armors.get(type);
				float newDmg = modDmg(dmg, resists.getFirst(), resists.getSecond(), applyAnvilReductionCap);
				newDmg -= newDmg*mobMod;
				if(!resisted && newDmg < dmg)
				{
					resisted = true;
				}
				if(!weakness && newDmg > dmg)
				{
					weakness = true;
				}
				absorb[type.ordinal()] = dmg - newDmg; 
				totalDamage += newDmg;
			}
		}
		DistinctDamageDescriptions.debug("new damage: "+totalDamage);
		float ratio = totalDamage/evt.getAmount();
		evt.setAmount(totalDamage);
		dmgSource.setDamageBypassesArmor();
		//Only spawn particles and play sounds for players.
		if(dmgSource.getTrueSource() instanceof EntityPlayer)
		{
			EntityPlayer attacker = (EntityPlayer) dmgSource.getTrueSource();
			if(resisted)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.RESISTANCE);
			}
			if(weakness)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.WEAKNESS);
			}
			if(immunityResisted)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.IMMUNITY);
				DDDSounds.playSound(attacker, DDDSounds.IMMUNITY_HIT, 1.5f, 1.0f);
			}
			else if(ratio != 0)
			{
				if(ratio > 1)
				{
					DDDSounds.playSound(attacker, DDDSounds.WEAKNESS_HIT, 0.7f, 1.0f);
				}
				else if(ratio < 1)
				{
					DDDSounds.playSound(attacker, DDDSounds.RESIST_DING, 1.6f, 1.0f);
				}
			}
		}
		if(mobResists.hasAdaptiveResistance())
		{
			DistinctDamageDescriptions.debug("Updating mob's adaptive immunity, since it is present...");
			//TODO add sfx
			mobResists.updateAdaptiveResistance(dmgMap.keySet().toArray(new DamageType[0]));
		}
		Map<EntityEquipmentSlot, IArmorDistribution> armorMap = DDDAPI.accessor.getArmorDistributionsForEntity(defender);
		for(ItemStack stack : defender.getArmorInventoryList())
		{
			Item item = stack.getItem();
			if(!(item instanceof ItemArmor || item instanceof ISpecialArmor))
			{
				continue;
			}
			ItemArmor armorItem = (ItemArmor) item;
			if(helmetOnly && armorItem.armorType != EntityEquipmentSlot.HEAD)
			{
				continue;
			}
			else if(bootsOnly && armorItem.armorType != EntityEquipmentSlot.FEET)
			{
				continue;
			}
			IArmorDistribution armorDist = armorMap.get(armorItem.armorType);
			int damageAmount = (int) MathHelper.clamp(Math.floor(absorb[0]*armorDist.getSlashingWeight() + absorb[1]*armorDist.getPiercingWeight() + absorb[2]*armorDist.getBludgeoningWeight()), 1.0f, Float.MAX_VALUE);
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
	
	private static float modDmg(float damage, float armor, float toughness, boolean applyAnvilReductionCap)
	{
		return (float) MathHelper.clamp(damage*(1-Math.max(armor/5.0f, armor - damage/(6+toughness/4.0f))/25.0f), 0.0f, applyAnvilReductionCap ? 0.75*damage : Float.MAX_VALUE);
	}
	
	private static void spawnRandomAmountOfParticles(Entity origin, DDDParticleType type)
	{
		ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
		int amount = (int)(3*Math.random()+2);
		for(int i = 0; i <= amount; i++)
		{
			manager.addEffect(new DDDParticle(origin, 0, 4, 0, type, particleDisplacement));
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
