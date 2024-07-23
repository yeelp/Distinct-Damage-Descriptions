package yeelp.distinctdamagedescriptions.items;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.init.DDDItems;
import yeelp.distinctdamagedescriptions.init.DDDSounds;

public class DDDDiscItem extends ItemRecord {

	public DDDDiscItem() {
		super("distinction", DDDSounds.DISTINCTION);
		this.setRegistryName(ModConsts.MODID, "recorddistinction");
		this.setTranslationKey(ModConsts.MODID + ".recorddistinction");
	}

	@Override
	public String getRecordNameLocal() {
		return "Yeelp - Distinction";
	}

	public static final class DropHandler extends Handler {

		private static final Set<Class<? extends EntityLivingBase>> BLACKLIST = ImmutableSet.of(EntityPlayer.class, AbstractSkeleton.class, EntityGolem.class, EntityTameable.class);

		@SuppressWarnings("static-method")
		@SubscribeEvent
		public final void onLivingDrops(LivingDropsEvent evt) {
			EntityLivingBase entity = evt.getEntityLiving();
			Entity source = evt.getSource().getTrueSource();
			if(ModConfig.core.enableDiscDrop && entity instanceof AbstractSkeleton && source != null && !BLACKLIST.stream().anyMatch((clazz) -> clazz.isInstance(source)) && source instanceof EntityLivingBase) {
				evt.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(DDDItems.disc)));
			}
		}
	}
}
