package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class CapabilityHandler extends Handler {

	private static final ResourceLocation creatureType = new ResourceLocation(ModConsts.MODID, "creatureTypes");

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt) {
		Entity entity = evt.getObject();
		if(entity != null) {
			if(entity instanceof EntityPlayer) {
				evt.addCapability(creatureType, CreatureType.UNKNOWN);
				DDDCapabilityDistributors.getPlayerCapabilities((EntityPlayer) entity).forEach(evt::addCapability);
			}
			else if(entity instanceof EntityLivingBase) {
				EntityLivingBase entityLiving = (EntityLivingBase) entity;
				evt.addCapability(creatureType, new CreatureType(DDDRegistries.creatureTypes.getCreatureTypeForMob(entityLiving)));
				DDDCapabilityDistributors.getCapabilities(entityLiving).ifPresent((m) -> m.forEach(evt::addCapability));
			}
			else if(entity instanceof IProjectile) {
				DDDCapabilityDistributors.getCapabilities((IProjectile) entity).ifPresent((m) -> m.forEach(evt::addCapability));
			}
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		DDDCapabilityDistributors.getCapabilities(evt.getObject()).forEach(evt::addCapability);
	}
}
