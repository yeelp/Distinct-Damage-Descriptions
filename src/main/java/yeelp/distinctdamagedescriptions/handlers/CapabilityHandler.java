package yeelp.distinctdamagedescriptions.handlers;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ComparableTriple;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.CreatureType;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.MobResistances;

public class CapabilityHandler extends Handler
{
	private static final ResourceLocation dmg = new ResourceLocation(ModConsts.MODID, "dmgDistribution");
	private static final ResourceLocation armor = new ResourceLocation(ModConsts.MODID, "armorResists");
	private static final ResourceLocation mobs = new ResourceLocation(ModConsts.MODID, "mobResists");
	private static final ResourceLocation projDmg = new ResourceLocation(ModConsts.MODID, "projectileDmgDistribution");
	private static final ResourceLocation creatureType = new ResourceLocation(ModConsts.MODID, "creatureTypes");
	
	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		Entity entity = evt.getObject();
		if(entity instanceof EntityPlayer)
		{
			evt.addCapability(dmg, new DamageDistribution());
			evt.addCapability(mobs, new MobResistances(0, 0, 0, false, false, false, false, 0.0f));
			evt.addCapability(creatureType, CreatureType.UNKNOWN);
		}
		else if(entity instanceof EntityLivingBase)
		{
			ResourceLocation loc = EntityList.getKey(entity);
			if(loc != null)
			{
				String key = loc.toString();
				Optional<ComparableTriple<Float, Float, Float>> oDmges = DDDRegistries.mobDamage.getMobDamage(key);
				Optional<MobResistanceCategories> oResists = DDDRegistries.mobResists.getResistancesForMob(key);
				IDamageDistribution dist;
				IMobResistances mobResists;
				if(oDmges.isPresent())
				{
					ComparableTriple<Float, Float, Float> dmges = oDmges.get();
					dist = new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight());
				}
				else
				{
					dist = ConfigGenerator.getOrGenerateMobDamage((EntityLivingBase) entity, loc);
				}
				if(oResists.isPresent())
				{
					MobResistanceCategories resists = oResists.get();
					mobResists = new MobResistances(resists.getSlashingResistance(), resists.getPiercingResistance(), resists.getBludgeoningResistance(), resists.getSlashingImmunity(), resists.getPiercingImmunity(), resists.getBludgeoningImmunity(), Math.random() < resists.adaptiveChance(), resists.getAdaptiveAmount());
				}
				else
				{
					mobResists = ConfigGenerator.getOrGenerateMobResistances((EntityLivingBase) entity, loc);
				}
				Tuple<CreatureTypeData, CreatureTypeData> types = DDDRegistries.creatureTypes.getCreatureTypeForMob(key);
				evt.addCapability(dmg, dist);
				evt.addCapability(mobs, mobResists);
				evt.addCapability(creatureType, new CreatureType(types.getFirst(), types.getSecond()));
			}
			else
			{
				DistinctDamageDescriptions.warn("ResourceLocation was null for: "+entity.getName()+", but entity is non null!");
			}
		}
		else if(entity instanceof IProjectile)
		{
			ResourceLocation loc = EntityList.getKey(entity);
			if(loc != null)
			{
				String key = loc.toString();
				Optional<ComparableTriple<Float, Float, Float>> oDmges = DDDRegistries.projectileProperties.getProjectileDamageTypes(key);
				IDamageDistribution dist;
				if(oDmges.isPresent())
				{
					ComparableTriple<Float, Float, Float> dmges = oDmges.get();
					dist = new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight());
				}
				else
				{
					dist = ConfigGenerator.getOrGenerateProjectileDistribution((IProjectile) entity, loc);
				}
				evt.addCapability(projDmg, dist);
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
		Optional<ComparableTriple<Float, Float, Float>> oDmges = DDDRegistries.itemProperties.getDamageDistributionForItem(key);
		IDamageDistribution dist;
		if(oDmges.isPresent())
		{
			ComparableTriple<Float, Float, Float> dmges = oDmges.get();
			dist = new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight());
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
				ComparableTriple<Float, Float, Float> dmges = DDDRegistries.itemProperties.getDefaultDamageDistribution();
				dist = new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight()); 
			}
		}
		evt.addCapability(dmg, dist);
		if(item instanceof ItemArmor)
		{
			Optional<ComparableTriple<Float, Float, Float>> oResists = DDDRegistries.itemProperties.getArmorDistributionForItem(key);
			IArmorDistribution armorResists;
			if(oResists.isPresent())
			{
				ComparableTriple<Float, Float, Float> resists = oResists.get();
				armorResists = new ArmorDistribution(resists.getLeft(), resists.getMiddle(), resists.getRight());
			}
			else
			{
				armorResists = ConfigGenerator.getOrGenerateArmorResistances((ItemArmor) item, evt.getObject());
			}
			evt.addCapability(armor, armorResists);
		}
	}
	
	public static void syncResistances(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new MobResistancesMessage(DDDAPI.accessor.getMobResistances(player)), (EntityPlayerMP) player);
	}
}
