package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;

public final class ExtraMap
{
	private final Map<String, Supplier<DDDDamageType>> map = new HashMap<String, Supplier<DDDDamageType>>();
	private IDamageDistribution explosionDist;
	
	ExtraMap()
	{
		update();
		this.map.put(DamageSource.ANVIL.damageType, () -> ModConfig.dmg.extraDamage.enableAnvilDamage ? DDDBuiltInDamageType.BLUDGEONING : null);
		this.map.put(DamageSource.CACTUS.damageType, () -> ModConfig.dmg.extraDamage.enableCactusDamage ? DDDBuiltInDamageType.PIERCING : null);
		this.map.put(DamageSource.FALL.damageType, () -> ModConfig.dmg.extraDamage.enableFallDamage ? DDDBuiltInDamageType.BLUDGEONING : null);
		this.map.put(DamageSource.FALLING_BLOCK.damageType, () -> ModConfig.dmg.extraDamage.enableFallingBlockDamage ? DDDBuiltInDamageType.BLUDGEONING : null);
		this.map.put(DamageSource.FLY_INTO_WALL.damageType, () -> ModConfig.dmg.extraDamage.enableFlyIntoWallDamage ? DDDBuiltInDamageType.BLUDGEONING : null);
		this.map.put(DamageSource.LIGHTNING_BOLT.damageType, () -> ModConfig.dmg.extraDamage.enableLightningDamage ? DDDBuiltInDamageType.LIGHTNING : null);
		this.map.put(DamageSource.WITHER.damageType, () -> ModConfig.dmg.extraDamage.enableWitherDamage ? DDDBuiltInDamageType.NECROTIC : null);
	}
	
	void update()
	{
		this.explosionDist = new DamageDistribution(DDDConfigReader.buildMap(0.0f, DDDConfigReader.parseListOfTuples(ModConfig.dmg.extraDamage.explosionDist)));
	}
	
	Set<DDDDamageType> get(LivingAttackEvent evt)
	{
		DamageSource src = evt.getSource();
		EntityLivingBase target = evt.getEntityLiving();
		DDDDamageType type = this.map.get(src.damageType).get();
		if(type == null)
		{
			if(src.isExplosion() && ModConfig.dmg.extraDamage.enableExplosionDamage)
			{
				return explosionDist.getCategories();
			}
			else if(src.isMagicDamage())
			{
				/* few cases to check. we also check the corresponding config option:
				 * 1. No direct or indirect source, Poison effect active and was ready one tick ago? -> POISON
				 * 2. Was the damage type thorns? Evoker Fangs? Guardian? -> FORCE
				 * 3. Target Undead, indirect source was thrown potion of healing? -> RADIANT
				 * 4. Target NOT Undead, indirect source was thrown potion of harming? -> NECROTIC
				 * 5. 
				 */
				if(isPoisonDamage(src, target))
				{
					type = DDDBuiltInDamageType.POISON;
				}
				else if(isForceDamage(src, target))
				{
					type = DDDBuiltInDamageType.FORCE;
				}
				type = getInstantEffectDamage(src, target);
			}
			else if(src.isFireDamage() && src != DamageSource.ON_FIRE)
			{
				type = DDDBuiltInDamageType.FIRE;
			}
		}
		return Sets.newHashSet(type);
	}
	
	private DDDDamageType getInstantEffectDamage(DamageSource src, EntityLivingBase target)
	{
		DDDDamageType type = DDDBuiltInDamageType.NORMAL;
		Entity source = src.getImmediateSource();
		if(source instanceof EntityPotion)
		{
			EntityPotion potion = (EntityPotion) source;
			ItemStack stack = potion.getPotion();
			if(stack.getItem() instanceof ItemPotion)
			{
				List<PotionEffect> effects = PotionUtils.getEffectsFromStack(potion.getPotion());
				for(PotionEffect effect : effects)
				{
					Potion appliedPotion = effect.getPotion();
					if(appliedPotion.isInstant())
					{
						switch(target.getCreatureAttribute())
						{
							case UNDEAD:
								type = appliedPotion == MobEffects.INSTANT_HEALTH ? DDDBuiltInDamageType.RADIANT : DDDBuiltInDamageType.NORMAL;
							default:
								type = appliedPotion == MobEffects.INSTANT_DAMAGE ? DDDBuiltInDamageType.NECROTIC : DDDBuiltInDamageType.NORMAL;
						}
					}
				}	
			}
		}
		return type;
	}
	
	private boolean isForceDamage(DamageSource src, EntityLivingBase target)
	{
		if(src.damageType.equals("thorns"))
		{
			return true;
		}
		else if(src.getImmediateSource() instanceof EntityGuardian)
		{
			return true;
		}
		else if(src.getImmediateSource() instanceof EntityEvokerFangs)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean isPoisonDamage(DamageSource src, EntityLivingBase target)
	{
		if(target.isPotionActive(MobEffects.POISON))
		{
			PotionEffect effect = target.getActivePotionEffect(MobEffects.POISON);
			return effect.getPotion().isReady(effect.getDuration()+1, effect.getAmplifier());
		}
		return false;
	}
}
