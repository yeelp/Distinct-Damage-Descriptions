package yeelp.distinctdamagedescriptions.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.init.DDDItems;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

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
		@SuppressWarnings("static-method")
		@SubscribeEvent
		public final void onLivingDrops(LivingDropsEvent evt) {
			Entity source = evt.getSource().getTrueSource();
			if(source == null || !(source instanceof EntityLivingBase)) {
				return;
			}
			EntityLivingBase entity = evt.getEntityLiving();
			YResources.getEntityIDString(source).filter((s) -> ModConfig.core.discListType.checkMob(s, ModConfig.core.discDropList)).ifPresent((s) -> {
				evt.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(DDDItems.disc)));				
			});
		}
	}
}
