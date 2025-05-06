package yeelp.distinctdamagedescriptions.api.impl.dists;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class EnderPearlDist extends AbstractSingleTypeDist {

	private static final Set<UUID> TELEPORTED = Sets.newHashSet();
	
	public EnderPearlDist() {
		super("enderpearl", Source.BUILTIN, () -> ModConfig.dmg.extraDamage.enableEnderPearlDamage);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.FORCE;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source == DamageSource.FALL && TELEPORTED.contains(target.getUniqueID());
	}
	
	@Override
	public int priority() {
		return 3;
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEnderTeleport(EnderTeleportEvent evt) {
		if(evt.getAttackDamage() > 0) {
			TELEPORTED.add(evt.getEntityLiving().getUniqueID());
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingHurt(LivingHurtEvent evt) {
		TELEPORTED.remove(evt.getEntityLiving().getUniqueID());
	}

}
