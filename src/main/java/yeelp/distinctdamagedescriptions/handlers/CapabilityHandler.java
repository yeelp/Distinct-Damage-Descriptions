package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;
import yeelp.distinctdamagedescriptions.util.ArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ComparableTriple;
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
			evt.addCapability(mobs, new MobResistances(0, 0, 0, false, false, false, false));
		}
		else if(entity instanceof EntityLivingBase)
		{
			String key = EntityList.getKey(entity).toString();
			ComparableTriple<Float, Float, Float> dmges = DistinctDamageDescriptions.getMobDamage(key);
			MobResistanceCategories resists = DistinctDamageDescriptions.getMobResistances(key);
			evt.addCapability(dmg, new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight()));
			evt.addCapability(mobs, new MobResistances(resists.getSlashingResistance(), resists.getPiercingResistance(), resists.getBludgeoningResistance(), resists.getSlashingImmunity(), resists.getPiercingImmunity(), resists.getBludgeoningImmunity(), Math.random() < resists.adaptiveChance()));
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
		ComparableTriple<Float, Float, Float> dmges = DistinctDamageDescriptions.getWeaponDamage(key);
		evt.addCapability(dmg, new DamageDistribution(dmges.getLeft(), dmges.getMiddle(), dmges.getRight()));
		if(item instanceof ItemArmor)
		{
			ComparableTriple<Float, Float, Float> resists = DistinctDamageDescriptions.getArmorDistribution(key);
			evt.addCapability(armor, new ArmorDistribution(resists.getLeft(), resists.getMiddle(), resists.getRight()));
		}
	}
	
	public static void syncResistances(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new MobResistancesMessage(DDDAPI.accessor.getMobResistances(player)), (EntityPlayerMP) player);
	}
}
