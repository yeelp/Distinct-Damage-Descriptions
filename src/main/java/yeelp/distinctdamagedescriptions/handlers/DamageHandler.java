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
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.client.render.patricle.DDDParticle;
import yeelp.distinctdamagedescriptions.client.render.patricle.DDDParticleType;
import yeelp.distinctdamagedescriptions.event.CustomDamageEvent;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.event.PhysicalDamageEvent;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ICreatureType;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

public class DamageHandler extends Handler
{
	private static Map<UUID, Boolean> shouldKnockback = new NonNullMap<UUID, Boolean>(false);
	private static Random particleDisplacement = new Random();
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void classifyDamage(LivingHurtEvent evt)
	{
		float finalModifier = 0.0f;
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource dmgSource = ModConfig.dmg.useCustomDamageTypes ? DDDRegistries.damageTypes.getDamageType(evt.getSource()) : evt.getSource();
		Entity attacker = dmgSource.getImmediateSource();
		String[] damageTypes = null;
		if(ModConfig.resist.useCreatureTypes)
		{
			ICreatureType type = DDDAPI.accessor.getMobCreatureType(defender);
			if(dmgSource instanceof DDDDamageType)
			{
				DDDDamageType dmgType = (DDDDamageType) dmgSource;
				damageTypes = dmgType.getExtendedTypes().toArray(damageTypes);
				for(String s : dmgType.getExtendedTypes())
				{
					finalModifier += type.getModifierForDamageType(s);
				}
			}
			else
			{
				damageTypes = new String[] {dmgSource.damageType};
				finalModifier = type.getModifierForDamageType(dmgSource.damageType);
			}
		}
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		Map<DamageType, Float> dmgMap = DDDAPI.accessor.classifyDamage(mobResists, dmgSource, evt.getAmount());
		if(dmgMap == null)
		{
			float amount = evt.getAmount();
			CustomDamageEvent custEvt = new CustomDamageEvent(attacker, defender, amount, finalModifier, damageTypes);
			MinecraftForge.EVENT_BUS.post(custEvt);
			evt.setAmount(custEvt.getDamage() - custEvt.getDamage()*custEvt.getResistance());
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
						PhysicalDamageEvent.SlashingDamage slashEvent = new PhysicalDamageEvent.SlashingDamage(dmgMap.get(type), mobResists.getSlashingResistance(), attacker, defender);
						MinecraftForge.EVENT_BUS.post(slashEvent);
						dmg = slashEvent.getDamage();
						mobMod = slashEvent.getResistance();
						break;
					case PIERCING:
						PhysicalDamageEvent.PiercingDamage pierceEvent = new PhysicalDamageEvent.PiercingDamage(dmgMap.get(type), mobResists.getPiercingResistance(), attacker, defender);
						MinecraftForge.EVENT_BUS.post(pierceEvent);
						dmg = pierceEvent.getDamage();
						mobMod = pierceEvent.getResistance();
						break;
					case BLUDGEONING:
						PhysicalDamageEvent.BludgeoningDamage bludgeoningEvent = new PhysicalDamageEvent.BludgeoningDamage(dmgMap.get(type), mobResists.getBludgeoningResistance(), attacker, defender);
						MinecraftForge.EVENT_BUS.post(bludgeoningEvent);
						dmg = bludgeoningEvent.getDamage();
						mobMod = bludgeoningEvent.getResistance();
						break;
				}
				if(mobMod > 0)
				{
					mobMod = MathHelper.clamp(mobMod - bruteForceAmount, 0, Float.MAX_VALUE);
				}
				Tuple<Float, Float> resists = armors.get(type);
				float newDmg = modDmg(dmg, resists.getFirst(), resists.getSecond(), applyAnvilReductionCap);
				newDmg -= newDmg*mobMod;
				//We use an error tolerance to avoid floating point precision errors playing wrong sound effects.
				//If newDmg and dmg are within a small neighbourhood of eachother, then we can ignore the exact difference in size and call them "equal"
				boolean isVeryClose = Math.abs(newDmg - dmg) < 0.0000001D;
				if(!resisted && newDmg < dmg && !isVeryClose)
				{
					resisted = true;
				}
				if(!weakness && newDmg > dmg && !isVeryClose)
				{
					weakness = true;
				}
				absorb[type.ordinal()] = dmg - newDmg; 
				totalDamage += newDmg;
			}
		}
		DistinctDamageDescriptions.debug("new damage after physical deductions: "+totalDamage);
		if(finalModifier != 0)
		{
			CustomDamageEvent custEvt = new CustomDamageEvent(attacker, defender, totalDamage, finalModifier, damageTypes);
			MinecraftForge.EVENT_BUS.post(custEvt);
			totalDamage = custEvt.getDamage() - custEvt.getDamage()*custEvt.getResistance();
			DistinctDamageDescriptions.debug("new damage after additional reductions: "+totalDamage);
		}
		float ratio = totalDamage/evt.getAmount();
		evt.setAmount(totalDamage < 0 ? 0 : totalDamage);
		dmgSource.setDamageBypassesArmor();
		//Only spawn particles and play sounds for players.
		EntityPlayer attackerPlayer = null;
		if(dmgSource.getTrueSource() instanceof EntityPlayer)
		{
			attackerPlayer = (EntityPlayer) dmgSource.getTrueSource();
			if(resisted || finalModifier > 0)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.RESISTANCE);
			}
			if(weakness || finalModifier < 0)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.WEAKNESS);
			}
			if(immunityResisted)
			{
				spawnRandomAmountOfParticles(defender, DDDParticleType.IMMUNITY);
				DDDSounds.playSound(attackerPlayer, DDDSounds.IMMUNITY_HIT, 1.5f, 1.0f);
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
			if(mobResists.updateAdaptiveResistance(dmgMap.keySet().toArray(new DamageType[0])) && attackerPlayer != null)
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
		int amount = (int)(2*Math.random())+2;
		for(int i = 0; i < amount; i++)
		{
			manager.addEffect(new DDDParticle(origin, 0, 4, 0, type, particleDisplacement));
		}
	}
}
