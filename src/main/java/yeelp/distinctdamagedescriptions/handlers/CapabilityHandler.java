package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ArmorResistanceCategories;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.ArmorResistances;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.MobResistances;

public class CapabilityHandler extends Handler
{
	private static final ResourceLocation dmg = new ResourceLocation(ModConsts.MODID, "dmgDistribution");
	private static final ResourceLocation armor = new ResourceLocation(ModConsts.MODID, "armorResists");
	private static final ResourceLocation mobs = new ResourceLocation(ModConsts.MODID, "mobResists");
	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		Entity entity = evt.getObject();
		if(entity instanceof EntityPlayer)
		{
			evt.addCapability(dmg, new DamageDistribution());
		}
		else if(entity instanceof EntityLivingBase)
		{
			String key = EntityList.getKey(entity).toString();
			DamageCategories dmges = DistinctDamageDescriptions.getMobDamage(key);
			MobResistanceCategories resists = DistinctDamageDescriptions.getMobResistances(key);
			evt.addCapability(dmg, new DamageDistribution(dmges.getSlashingDamage(), dmges.getPiercingDamage(), dmges.getBludgeoningDamage()));
			evt.addCapability(mobs, new MobResistances(resists.getSlashingResistance(), resists.getPiercingResistance(), resists.getBludgeoningResistance(), resists.getSlashingImmunity(), resists.getPiercingImmunity(), resists.getBludgeoningImmunity(), resists.isAdaptive()));
		}
		else
		{
			//projectile stuff
		}
	}
	
	@SubscribeEvent
	public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> evt)
	{
		Item item = evt.getObject().getItem();
		String key = item.getRegistryName().toString();
		DamageCategories dmges = DistinctDamageDescriptions.getWeaponDamage(key);
		evt.addCapability(dmg, new DamageDistribution(dmges.getSlashingDamage(), dmges.getPiercingDamage(), dmges.getBludgeoningDamage()));
		if(item instanceof ItemArmor)
		{
			ArmorResistanceCategories resists = DistinctDamageDescriptions.getArmorResist(key);
			evt.addCapability(armor, new ArmorResistances(resists.getSlashingResistance(), resists.getPiercingResistance(), resists.getBludgeoningResistance(), resists.getSlashingImmunity(), resists.getPiercingImmunity(), resists.getBludgeoningImmunity(), resists.getToughnessRating()));
		}
	}
}
