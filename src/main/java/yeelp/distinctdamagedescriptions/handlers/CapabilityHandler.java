package yeelp.distinctdamagedescriptions.handlers;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
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
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.CreatureType;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.MobResistances;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.init.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class CapabilityHandler extends Handler {
	private static final ResourceLocation dmg = new ResourceLocation(ModConsts.MODID, "dmgDistribution");
	private static final ResourceLocation armor = new ResourceLocation(ModConsts.MODID, "armorResists");
	private static final ResourceLocation shield = new ResourceLocation(ModConsts.MODID, "shieldEffectiveness");
	private static final ResourceLocation mobs = new ResourceLocation(ModConsts.MODID, "mobResists");
	private static final ResourceLocation projDmg = new ResourceLocation(ModConsts.MODID, "projectileDmgDistribution");
	private static final ResourceLocation creatureType = new ResourceLocation(ModConsts.MODID, "creatureTypes");

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt) {
		Entity entity = evt.getObject();
		if(entity == null) {
			return;
		}
		if(entity instanceof EntityPlayer) {
			evt.addCapability(dmg, new DamageDistribution());
			evt.addCapability(mobs, new MobResistances());
			evt.addCapability(creatureType, CreatureType.UNKNOWN);
		}
		else {
			Optional<ResourceLocation> oLoc = YResources.getEntityID(entity);
			if(oLoc.isPresent()) {
				ResourceLocation loc = oLoc.get();
				String key = loc.toString();
				if(entity instanceof EntityLivingBase) {
					Optional<IDamageDistribution> oDist = getConfigValue(key, DDDConfigurations.mobDamage);
					Optional<MobResistanceCategories> oResists = getConfigValue(key, DDDConfigurations.mobResists);
					IMobResistances mobResists;
					IDamageDistribution dist;
					MobResistanceCategories resists;
					if(oDist.isPresent()) {
						dist = oDist.get();
					}
					else {
						dist = ConfigGenerator.getOrGenerateMobDamage((EntityLivingBase) entity, loc);
					}
					if(oResists.isPresent()) {
						resists = oResists.get();
						mobResists = new MobResistances(resists.getResistanceMap(), resists.getImmunities(), Math.random() < resists.adaptiveChance(), resists.getAdaptiveAmount());
					}
					else {
						mobResists = ConfigGenerator.getOrGenerateMobResistances((EntityLivingBase) entity, loc);
					}
					Set<CreatureTypeData> types = DDDRegistries.creatureTypes.getCreatureTypeForMob(key);
					evt.addCapability(dmg, dist.copy());
					evt.addCapability(mobs, mobResists.copy());
					evt.addCapability(creatureType, new CreatureType(types));
				}
				else if(entity instanceof IProjectile) {
					Optional<IDamageDistribution> oDmges = getConfigValue(key, DDDConfigurations.projectiles);
					IDamageDistribution dist;
					if(oDmges.isPresent()) {
						dist = oDmges.get();
					}
					else {
						dist = ConfigGenerator.getOrGenerateProjectileDistribution((IProjectile) entity, loc);
					}
					evt.addCapability(projDmg, dist.copy());

				}
			}
			else {
				DistinctDamageDescriptions.warn("ResourceLocation was null for: " + entity.getName() + ", but entity is non null!");
			}
		}
	}

	@SubscribeEvent
	public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		Item item = evt.getObject().getItem();
		IDamageDistribution dist;
		ResourceLocation itemLoc = item.getRegistryName();
		if(itemLoc == null) {
			dist = DDDConfigurations.items.getDefaultValue();
		}
		String key = item.getRegistryName().toString();
		Optional<IDamageDistribution> oDmges = getConfigValue(key, DDDConfigurations.items);
		if(oDmges.isPresent()) {
			dist = oDmges.get();
		}
		else {
			if(item instanceof ItemSword) {
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemSword) item, evt.getObject());
			}
			else if(item instanceof ItemTool) {
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemTool) item, evt.getObject());
			}
			else if(item instanceof ItemHoe) {
				dist = ConfigGenerator.getOrGenerateWeaponCapabilities((ItemHoe) item, evt.getObject());
			}
			else {
				dist = DDDConfigurations.items.getDefaultValue();
			}
		}
		evt.addCapability(dmg, dist.copy());
		if(item instanceof ItemArmor) {
			Optional<IArmorDistribution> oResists = getConfigValue(key, DDDConfigurations.armors);
			IArmorDistribution armorResists;
			if(oResists.isPresent()) {
				armorResists = oResists.get();
			}
			else {
				armorResists = ConfigGenerator.getOrGenerateArmorResistances((ItemArmor) item, evt.getObject());
			}
			evt.addCapability(armor, armorResists.copy());
		}
		else if(item instanceof ItemShield) {
			Optional<ShieldDistribution> oCaps = getConfigValue(key, DDDConfigurations.shields);
			ShieldDistribution shieldDist;
			if(oCaps.isPresent()) {
				shieldDist = oCaps.get();
			}
			else {
				shieldDist = ConfigGenerator.getOrGenerateShieldDistribution((ItemShield) item, evt.getObject());
			}
			evt.addCapability(shield, shieldDist.copy());
		}
	}

	public static void syncResistances(EntityPlayer player) {
		if(!player.world.isRemote) {
			PacketHandler.INSTANCE.sendTo(new MobResistancesMessage(DDDAPI.accessor.getMobResistances(player)), (EntityPlayerMP) player);
		}
	}

	@Nonnull
	private <T> Optional<T> getConfigValue(String key, IDDDConfiguration<T> config) {
		if(config.configured(key)) {
			return Optional.of(config.get(key));
		}
		else if(ModConfig.generateStats) {
			return Optional.empty();
		}
		else {
			return Optional.of(config.getDefaultValue());
		}

	}
}
