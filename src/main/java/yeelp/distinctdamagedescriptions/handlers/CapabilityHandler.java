package yeelp.distinctdamagedescriptions.handlers;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class CapabilityHandler extends Handler {

	private static final ResourceLocation creatureType = new ResourceLocation(ModConsts.MODID, "creatureTypes");

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt) {
		Entity entity = evt.getObject();
		if(entity == null) {
			DistinctDamageDescriptions.warn("Trying to get capabilities for null entity!");
			return;
		}
		if(entity instanceof EntityPlayer) {
			evt.addCapability(creatureType, MobCreatureType.UNKNOWN);
			addCapsIfNonNull(evt, DDDCapabilityDistributors.getPlayerCapabilities((EntityPlayer) entity));
		}
		else if(entity instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			evt.addCapability(creatureType, new MobCreatureType(DDDRegistries.creatureTypes.getCreatureTypeForMob(entityLiving)));
			DDDCapabilityDistributors.getCapabilities(entityLiving).ifPresent((m) -> addCapsIfNonNull(evt, m));
		}
		else if(entity instanceof IProjectile) {
			DDDCapabilityDistributors.getCapabilities((IProjectile) entity).ifPresent((m) -> addCapsIfNonNull(evt, m));
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		if(evt.getObject() == null) {
			DistinctDamageDescriptions.warn("Trying to get capabilities for null stack!");
			return;
		}
		DDDCapabilityDistributors.getCapabilities(evt.getObject()).ifPresent((m) -> addCapsIfNonNull(evt, m));
	}
	
	private static <T> void addCapsIfNonNull(AttachCapabilitiesEvent<T> evt, Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> map) {
		map.forEach((loc, cap) -> {
			if(cap != null) {
				evt.addCapability(loc, cap);
			}
		});
	}
}
