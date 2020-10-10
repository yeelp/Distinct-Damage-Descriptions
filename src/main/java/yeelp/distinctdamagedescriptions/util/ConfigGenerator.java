package yeelp.distinctdamagedescriptions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;

/**
 * Generates config values on the fly for newly encountered mobs.
 * @author Yeelp
 *
 */
public final class ConfigGenerator
{
	private static final Random rng = new Random();
	private static final Map<ResourceLocation, IDamageDistribution> MOB_DAMAGE_CACHE = new HashMap<ResourceLocation, IDamageDistribution>();
	private static final Map<ResourceLocation, IMobResistances> MOB_RESISTS_CACHE = new HashMap<ResourceLocation, IMobResistances>();
	private static final Map<ResourceLocation, Float> ADAPTABILITY_CHANCE_CACHE = new HashMap<ResourceLocation, Float>();
	private static final Map<ResourceLocation, IDamageDistribution> WEAPON_CACHE = new HashMap<ResourceLocation, IDamageDistribution>();
	private static final Map<ResourceLocation, IArmorDistribution> ARMOR_CACHE = new HashMap<ResourceLocation, IArmorDistribution>();
	
	/**
	 * Generate Mob Capabilities on the fly and save them to be injected in the config later.
	 * @param entity entity in question. Needed to get health, armor for generating values.
	 * @param loc entity's ResourceLocation. CapabilityHandler already has this value when attaching capabilities, so might as well pass it here.
	 * @return a Tuple from the cache or a new Tuple representing the generated config values.
	 */
	public static final Tuple<IDamageDistribution, IMobResistances> getOrGenerateMobCapabilities(EntityLivingBase entity, ResourceLocation loc)
	{
		//if this holds, then we have both damage and resistances.
		if(MOB_DAMAGE_CACHE.containsKey(loc))
		{
			IMobResistances resists = MOB_RESISTS_CACHE.get(loc);
			IDamageDistribution dmg = MOB_DAMAGE_CACHE.get(loc);
			resists.setAdaptiveResistance(Math.random() < ADAPTABILITY_CHANCE_CACHE.get(loc));
			return new Tuple<IDamageDistribution, IMobResistances>(dmg, resists);
		}
		else
		{
			final double health = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
			final double armor = entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
			final double knockbackResist = entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
			final boolean isMonster = entity.isCreatureType(EnumCreatureType.MONSTER, false);
			final boolean isWaterMob = entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false);
			final boolean isPeaceful = !(isMonster || isWaterMob);
			final boolean isBoss = !entity.isNonBoss();
			
			IDamageDistribution damageDist;
			IMobResistances mobResists;
			boolean isArthropod = false;
			
			switch(entity.getCreatureAttribute())
			{
				case ARTHROPOD:
					//Arthropods typically bite, so give them piercing usually. Up to 20% of their damage can be bludgeoning instead.
					float bludgeAmount = rng.nextInt(20)/100.0f;
					float pierceAmount = 1 - bludgeAmount;
					damageDist = new DamageDistribution(0, pierceAmount, bludgeAmount);
					isArthropod = true;
					break;
				default:
					damageDist = new DamageDistribution();
					break;
			}
			
			/*
			 * Monsters will get no more than 30% resistance to any category by default.
			 * Non monsters (isPeaceful) will get no more than 10% to any category by default.
			 * otherwise base value is 25%.
			 * 
			 * - For each 10 health, this cap increases by 5% (Max 40% bonus).
			 * - For each point of armor, the value set is increased by 5% (Will not increase a value past 80%).
			 * - Each 10% of knockback resistance will increase the mob's bludgeoning resistance by 5%
			 * 		- A mob with full knockback resistance gets bludgeoning immunity.
			 * - A mob with over 50 health gets an immunity, provided it doesn't have a bludgeoning immunity from the point above.
			 * - Bosses get an additional 20% to resistance values below 50%
			 * - A mob's adaptability chance increases by 5% for each 5 points of max health below 20.
			 * 		- arthropods gain a bonus 25% to adaptability.
			 * - A mob's adaptability amount is set to 25% by default.
			 * 		- This increases by 5% for each resistance below 50%.
			 * 
			 * Further changes happen depending on the Entity's superclass.
			 * 
			 * AbstractSkeleton: Mob receives an additional 15% slashing resistance and loses all adaptability chance.
			 * EntityBlaze: Mob receives an additional 5% slashing, piercing, bludgeoning resistance and loses all adaptability chance.
			 * EntityCreeper: Mob receives an additional 10% bludgeoning resistance and loses 10% adaptability chance.
			 * EntityEnderman: Mob receives an additional 15% adaptability chance and 5% adaptability amount and loses 10% to all resistances.
			 * EntityEndermite: Mob receives an additional 70% adaptability chance and 50% adaptability amount and loses 15% to all resistances.
			 * EntityGuardian: Mob receives an additional 15% bludgeoning resistance and loses 15% piercing resistance.
			 * EntitySilverfish: Mob receives an additional 40% adaptability chance, 45% adaptability amount, and loses 40% bludgeoning resistance and 10% slashing resistance.
			 * EntitySpider: Mob receives an additional 20% adaptability chance and 20% piercing resistance, and loses 35% bludgeoning resistance.
			 * EntityZombie: Mob receives an additional 10% bludgeoning resistance, and loses all adaptability chance. 
			 * EntityFlying: Mob loses 60% bludgeoning resistance.
			 * EntitySlime: Mob gains a slashing immunity and loses a piercing immunity if it had it and loses 30% slashing and piercing resistance.
			 * EntityGolem: Mob gains a random immunity it didn't have before.
			 * otherwise: If not a boss, Mob gains a random amount (5-20%) in a resistance and loses that amount in another resistance.
			 */
			float slash = 0, pierce = 0, bludge = 0, adaptChance = 0, adaptAmount = 0;
			boolean slashImmune = false, pierceImmune = false, bludgeImmune = false;
			
			float modifier = (float) (isMonster ? 0.3 : isPeaceful ? 0.1 : 0.25);
			//bonus to modifier for health
			modifier += MathHelper.clamp(0.05*health/10.0, 0, 0.4);
			//bonus for armor
			float bonus = (float) (armor*0.05);
			
			//generate base resistance
			slash = generateResistance(modifier, bonus);
			pierce = generateResistance(modifier, bonus);
			bludge = generateResistance(modifier, bonus);
			
			//bonus for knockback resistance
			if(knockbackResist == 1)
			{
				bludgeImmune = true;
			}
			else
			{
				bludge += 0.05f*((int) (knockbackResist/0.1));
			}
			
			//bonus immunity
			if(health > 50 && !bludgeImmune)
			{
				switch(rng.nextInt(3))
				{
					case 0:
						slashImmune = true;
						break;
					case 1:
						pierceImmune = true;
						break;
					case 2:
						bludgeImmune = true;
						break;
				}	
			}
			
			//boss bonus
			if(isBoss)
			{
				slash += slash < 0.5 ? 0.2 : 0.0;
				pierce += pierce < 0.5 ? 0.2 : 0.0;
				bludge += bludge < 0.5 ? 0.2 : 0.0;
			}
			
			//adaptability bonus
			if(health < 20)
			{
				adaptChance = (float) (0.05*health/5.0);
				if(isArthropod)
				{
					adaptChance += 0.25;
				}
				adaptAmount = 0.25f;
				adaptAmount += slash < 0.5 ? 0.05 : 0.0;
				adaptAmount += pierce < 0.5 ? 0.05 : 0.0;
				adaptAmount += bludge < 0.5 ? 0.05 : 0.0;
			}
			
			if(entity instanceof AbstractSkeleton)
			{
				slash += 0.15;
				adaptChance = 0;
				adaptAmount = 0;
			}
			else if(entity instanceof EntityBlaze)
			{
				slash += 0.05;
				pierce += 0.05;
				bludge += 0.05;
				adaptChance = 0;
				adaptAmount = 0;
			}
			else if(entity instanceof EntityCreeper)
			{
				bludge += 0.1;
				adaptChance -= 0.1;
			}
			else if(entity instanceof EntityEnderman)
			{
				bludge -= 0.1;
				slash -= 0.1;
				pierce -= 0.1;
				adaptChance += 0.15;
				adaptAmount += 0.05;
			}
			else if(entity instanceof EntityEndermite)
			{
				adaptChance += 0.7;
				adaptAmount += 0.5;
				bludge -= 0.15;
				slash -= 0.15;
				pierce -= 0.15;
			}
			else if(entity instanceof EntityGuardian)
			{
				bludge += 0.15;
				pierce -= 0.15;
			}
			else if(entity instanceof EntitySilverfish)
			{
				adaptChance += 0.4;
				adaptAmount += 0.45;
				bludge -= 0.4;
				slash -= 0.1;
			}
			else if(entity instanceof EntitySpider)
			{
				adaptChance += 0.2;
				pierce += 0.2;
				bludge -= 0.35;
			}
			else if(entity instanceof EntityZombie)
			{
				bludge += 0.1;
				adaptChance = 0;
				adaptAmount = 0;
			}
			else if(entity instanceof EntityFlying)
			{
				bludge -= 0.6;
			}
			else if(entity instanceof EntitySlime)
			{
				slashImmune = true;
				pierceImmune = false;
				slash -= 0.3;
				pierce -= 0.3;
			}
			else if(entity instanceof EntityGolem)
			{
				List<Integer> lst = new ArrayList<Integer>(3);
				if(!slashImmune)
				{
					lst.add(0);
				}
				if(!pierceImmune)
				{
					lst.add(1);
				}
				if(!bludgeImmune)
				{
					lst.add(2);
				}
				int index = lst.get(rng.nextInt(lst.size()));
				switch(index)
				{
					case 0:
						slashImmune = true;
						break;
					case 1:
						pierceImmune = true;
						break;
					case 2:
						bludgeImmune = true;
						break;
				}
			}
			else
			{
				if(!isBoss)
				{
					float bonusResist = 0.15f*rng.nextFloat() + 0.05f;
					int bonusIndex = rng.nextInt(3);
					int penaltyIndex = (bonusIndex + (rng.nextBoolean() ? 1 : 2)) % 3;
					switch(bonusIndex)
					{
						case 0:
							slash += bonusResist;
							break;
						case 1:
							pierce += bonusResist;
							break;
						case 2:
							bludge += bonusResist;
							break;
					}
					switch(penaltyIndex)
					{
						case 0:
							slash -= bonusResist;
							break;
						case 1:
							pierce -= bonusResist;
							break;
						case 2:
							bludge -= bonusResist;
							break;
					}
				}
			}
			adaptChance = MathHelper.clamp(adaptChance, 0, 1);
			adaptAmount = adaptAmount < 0 ? 0 : adaptAmount;
			ADAPTABILITY_CHANCE_CACHE.put(loc, adaptChance);
			mobResists = new MobResistances(slash, pierce, bludge, slashImmune, pierceImmune, bludgeImmune, Math.random() < adaptChance, adaptAmount);
			MOB_DAMAGE_CACHE.put(loc, damageDist);
			MOB_RESISTS_CACHE.put(loc, mobResists);
			return new Tuple<IDamageDistribution, IMobResistances>(damageDist, mobResists);
		}	
	}
	private static float generateResistance(float modifier, float bonus)
	{
		return modifier*rng.nextFloat() + bonus;
	}
}
