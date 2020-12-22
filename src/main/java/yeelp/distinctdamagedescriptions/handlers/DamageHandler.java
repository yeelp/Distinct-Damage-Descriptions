package yeelp.distinctdamagedescriptions.handlers;

import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.BLUDGEONING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.PIERCING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.SLASHING;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticleType;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.network.ParticleMessage;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

public class DamageHandler extends Handler
{
	private static Map<UUID, Boolean> shouldKnockback = new NonNullMap<UUID, Boolean>(false);
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
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingHurtEvent evt)
	{
		//float finalModifier = 0.0f;
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
		Map<String, Float> resistMap = DDDAPI.accessor.classifyResistances(dmgMap.keySet(), mobResists);
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
		Map<String, Tuple<Float, Float>> armors = DDDAPI.accessor.getArmorValuesForEntity(defender, bootsOnly, helmetOnly);
		DistinctDamageDescriptions.debug("Damage Total: ("+dmgMap.get(SLASHING)+", "+dmgMap.get(PIERCING)+", "+dmgMap.get(BLUDGEONING)+")");
		DamageDescriptionEvent.Pre pre = new DamageDescriptionEvent.Pre(attacker, defender, dmgMap, resistMap, armors);
		MinecraftForge.EVENT_BUS.post(pre);
		if(pre.isCanceled())
		{
			return;
		}
		dmgMap = pre.getAllDamages();
		resistMap = pre.getAllResistances();
		armors = pre.getAllArmor();
		Map<String, Float> absorb = new NonNullMap<String, Float>(0.0f);
		boolean blockKnockbackFlag = true;
		boolean immunityResisted = false;
		boolean resisted = false;
		boolean weakness = false;
		for(Float f : dmgMap.values())
		{
			if(f != null && f.floatValue() > 0)
			{
				blockKnockbackFlag = false;
				break;
			}
		}
		shouldKnockback.put(defender.getUniqueID(), blockKnockbackFlag);
		float totalDamage = 0;
		for(Entry<String, Float> entry : dmgMap.entrySet())
		{
			String type = entry.getKey();
			float resistance = resistMap.get(type);
			if(resistance == Float.MAX_VALUE)
			{
				immunityResisted = true;
				continue;
			}
			else
			{
				Tuple<Float, Float> armorVals = armors.get(type);
				float damage = entry.getValue();
				if(resistance > 0)
				{
					resistance = MathHelper.clamp(resistance - bruteForceAmount, 0, Float.MAX_VALUE);
					resistMap.put(type, resistance);
				}
				float newDmg = modDmg(damage, armorVals.getFirst(), armorVals.getSecond(), applyAnvilReductionCap);
				newDmg -= newDmg*resistance;
				//We use an error tolerance to avoid floating point precision errors playing wrong sound effects.
				//If newDmg and dmg are within a small neighbourhood of eachother, then we can ignore the exact difference in size and call them "equal"
				boolean isVeryClose = Math.abs(newDmg - damage) < 0.0000001D;
				if(!resisted && newDmg < damage && !isVeryClose)
				{
					resisted = true;
				}
				if(!weakness && newDmg > damage && !isVeryClose)
				{
					weakness = true;
				}
				absorb.put(type, damage - newDmg); 
				dmgMap.put(type, newDmg);
			}
		}
		DamageDescriptionEvent.Post post = new DamageDescriptionEvent.Post(attacker, defender, dmgMap, resistMap, armors);
		MinecraftForge.EVENT_BUS.post(post);
		for(float f : post.getAllDamages().values())
		{
			totalDamage += f;
		}
		//One more reduction for natural armor
		totalDamage = modDmg(totalDamage, (float)defender.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue(), (float)defender.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue(), applyAnvilReductionCap);
		DistinctDamageDescriptions.debug("new damage after deductions: "+totalDamage);
		float ratio = MathHelper.clamp(totalDamage/evt.getAmount(), 0, Float.MAX_VALUE);
		evt.setAmount(totalDamage < 0 ? 0 : totalDamage);
		dmgSource.setDamageBypassesArmor();
		
		//Only spawn particles and play sounds for players.
		EntityPlayer attackerPlayer = null;
		if(dmgSource.getTrueSource() instanceof EntityPlayer)
		{
			attackerPlayer = (EntityPlayer) dmgSource.getTrueSource();
			if(resisted)
			{
				spawnRandomAmountOfParticles(attackerPlayer, defender, DDDParticleType.RESISTANCE);
			}
			if(weakness)
			{
				spawnRandomAmountOfParticles(attackerPlayer, defender, DDDParticleType.WEAKNESS);
			}
			if(immunityResisted)
			{
				spawnRandomAmountOfParticles(attackerPlayer, defender, DDDParticleType.IMMUNITY);
				DDDSounds.playSound(attackerPlayer, DDDSounds.IMMUNITY_HIT, 1.5f, 1.0f);
				evt.setCanceled(ratio == 0 && ModConfig.dmg.cancelLivingHurtEventOnImmunity);
			}
			else if(ratio != 0)
			{
				if(ratio > 1)
				{
					DDDSounds.playSound(attackerPlayer, DDDSounds.WEAKNESS_HIT, 0.6f, 1.0f);
				}
				else if(ratio < 1)
				{
					DDDSounds.playSound(attackerPlayer, DDDSounds.RESIST_DING, 1.7f, 1.0f);
				}
			}
			else if(ratio == 0)
			{
				DDDSounds.playSound(attackerPlayer, DDDSounds.HIGH_RESIST_HIT, 1.7f, 1.0f);
			}
		}
		if(mobResists.hasAdaptiveResistance())
		{
			DistinctDamageDescriptions.debug("Updating mob's adaptive resistance, since it is present...");
			if(mobResists.updateAdaptiveResistance(dmgMap.keySet().toArray(new String[0])) && attackerPlayer != null)
			{
				DDDSounds.playSound(attackerPlayer, DDDSounds.ADAPTABILITY_CHANGE, 2.0f, 1.0f);
			}
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
			int damageAmount = getDamageAmount(absorb, armorDist);
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
		if(shouldKnockback.get(uuid) && evt.getOriginalStrength() != 0)
		{
			evt.setCanceled(true);
			shouldKnockback.remove(uuid);
		}
	}
	
	private static float modDmg(float damage, float armor, float toughness, boolean applyAnvilReductionCap)
	{
		return (float) MathHelper.clamp(damage*(1-Math.max(armor/5.0f, armor - damage/(6+toughness/4.0f))/25.0f), 0.0f, applyAnvilReductionCap ? 0.75*damage : Float.MAX_VALUE);
	}
	
	private int getDamageAmount(Map<String, Float> absorption, IArmorDistribution armorDist)
	{
		float sum = 0.0f;
		for(Entry<String, Float> entry : absorption.entrySet())
		{
			sum += entry.getValue()*armorDist.getWeight(entry.getKey());
		}
		return (int) MathHelper.clamp(sum, 0, Float.MAX_VALUE);
	}
	
	private static void spawnRandomAmountOfParticles(EntityPlayer viewer, Entity origin, DDDParticleType type)
	{
		if(viewer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) viewer;
			int amount = (int)(2*Math.random())+2;
			for(int i = 0; i < amount; i++)
			{
				double x = origin.posX + origin.width*particleDisplacement.nextDouble() - origin.width/2;
				double y = origin.posY + origin.getEyeHeight() + origin.height*particleDisplacement.nextDouble() - origin.height/2;
				double z = origin.posZ + origin.width*particleDisplacement.nextDouble() - origin.width/2;
				PacketHandler.INSTANCE.sendTo(new ParticleMessage(type, x, y, z), (EntityPlayerMP) player);
			}
		}
		else
		{
			return;
		}
	}
}
