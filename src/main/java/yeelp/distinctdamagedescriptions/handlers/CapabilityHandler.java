package yeelp.distinctdamagedescriptions.handlers;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.CreatureType;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.MobResistances;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class CapabilityHandler extends Handler
{
	private static final ResourceLocation dmg = new ResourceLocation(ModConsts.MODID, "dmgDistribution");
	private static final ResourceLocation armor = new ResourceLocation(ModConsts.MODID, "armorResists");
	private static final ResourceLocation shield = new ResourceLocation(ModConsts.MODID, "shieldEffectiveness");
	private static final ResourceLocation mobs = new ResourceLocation(ModConsts.MODID, "mobResists");
	private static final ResourceLocation projDmg = new ResourceLocation(ModConsts.MODID, "projectileDmgDistribution");
	private static final ResourceLocation creatureType = new ResourceLocation(ModConsts.MODID, "creatureTypes");
	
	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		Entity entity = evt.getObject();
		if(entity == null)
		{
			return;
		}
		if(entity instanceof EntityPlayer)
		{
			evt.addCapability(dmg, new DamageDistribution());
			evt.addCapability(mobs, new MobResistances());
			evt.addCapability(creatureType, CreatureType.UNKNOWN);
		}
		else
		{
			Optional<ResourceLocation> oLoc = YResources.getEntityID(entity);
			if(oLoc.isPresent())
			{
				ResourceLocation loc = oLoc.get();
				String key = loc.toString();
				if(entity instanceof EntityLivingBase)
				{	
					Optional<Map<String, Float>> oDmges = DDDRegistries.mobDamage.getMobDamage(key);
					Optional<MobResistanceCategories> oResists = DDDRegistries.mobResists.getResistancesForMob(key);
					IDamageDistribution dist;
					IMobResistances mobResists;
					if(oDmges.isPresent())
					{
						Map<String, Float> dmges = oDmges.get();
						dist = new DamageDistribution(dmges);
					}
					else
					{
						dist = ConfigGenerator.getOrGenerateMobDamage((EntityLivingBase) entity, loc);
					}
					if(oResists.isPresent())
					{
						MobResistanceCategories resists = oResists.get();
						mobResists = new MobResistances(resists.getResistanceMap(), resists.getImmunities(), Math.random() < resists.adaptiveChance(), resists.getAdaptiveAmount());
					}
					else
					{
						mobResists = ConfigGenerator.getOrGenerateMobResistances((EntityLivingBase) entity, loc);
					}
					Set<CreatureTypeData> types = DDDRegistries.creatureTypes.getCreatureTypeForMob(key);
					evt.addCapability(dmg, dist);
					evt.addCapability(mobs, mobResists);
					evt.addCapability(creatureType, new CreatureType(types));
				}
				else if(entity instanceof IProjectile)
				{
					Optional<Map<String, Float>> oDmges = DDDRegistries.projectileProperties.getProjectileDamageTypes(key);
					IDamageDistribution dist;
					if(oDmges.isPresent())
					{
						Map<String, Float> dmges = oDmges.get();
						dist = new DamageDistribution(dmges);
					}
					else
					{
						dist = ConfigGenerator.getOrGenerateProjectileDistribution((IProjectile) entity, loc);
					}
					evt.addCapability(projDmg, dist);
					
				}
			}
			else
			{
				DistinctDamageDescriptions.warn("ResourceLocation was null for: "+entity.getName()+", but entity is non null!");
			}
		}
	}
	
	@SubscribeEvent
	public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> evt)
	{
		Item item = evt.getObject().getItem();
		String key = item.getRegistryName().toString();
		Optional<Map<String, Float>> oDmges = DDDRegistries.itemProperties.getDamageDistributionForItem(key);
		IDamageDistribution dist;
		if(oDmges.isPresent())
		{
			Map<String, Float> dmges = oDmges.get();
			dist = new DamageDistribution(dmges);
		}
		else
		{
			if(item instanceof ItemSword)
			{
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemSword) item, evt.getObject());
			}
			else if(item instanceof ItemTool)
			{
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemTool) item, evt.getObject());
			}
			else if(item instanceof ItemHoe)
			{
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemHoe) item, evt.getObject());
			}
			else
			{
				Map<String, Float> dmges = DDDRegistries.itemProperties.getDefaultDamageDistribution();
				dist = new DamageDistribution(dmges); 
			}
		}
		evt.addCapability(dmg, dist);
		if(item instanceof ItemArmor)
		{
			Optional<Map<String, Float>> oResists = DDDRegistries.itemProperties.getArmorDistributionForItem(key);
			IArmorDistribution armorResists;
			if(oResists.isPresent())
			{
				Map<String, Float> resists = oResists.get();
				armorResists = new ArmorDistribution(resists);
			}
			else
			{
				armorResists = ConfigGenerator.getOrGenerateArmorResistances((ItemArmor) item, evt.getObject());
			}
			evt.addCapability(armor, armorResists);
		}
		else if(item instanceof ItemShield)
		{
			Optional<Map<String, Float>> oCaps = DDDRegistries.itemProperties.getShieldDistribution(key);
			ShieldDistribution shieldDist;
			if(oCaps.isPresent())
			{
				Map<String, Float> caps = oCaps.get();
				shieldDist = new ShieldDistribution(caps);
			}
			else
			{
				shieldDist = ConfigGenerator.getOrGenerateShieldDistribution((ItemShield) item, evt.getObject());
			}
			evt.addCapability(shield, shieldDist);
		}
	}
	
	public static void syncResistances(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new MobResistancesMessage(DDDAPI.accessor.getMobResistances(player)), (EntityPlayerMP) player);
	}
}
