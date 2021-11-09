package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class BattleSignCounterAttackDistribution implements DDDPredefinedDistribution {

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		if(check(src)) {
			return getDist((EntityPlayer) src.getTrueSource()).getCategories();
		}
		return ImmutableSet.of();
	}

	@Override
	public String getName() {
		return "battlesign counter";
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		if(check(src)) {
			return Optional.of(getDist((EntityPlayer) src.getTrueSource()));
		}
		return Optional.empty();
	}
	
	private static boolean check(DamageSource src) {
		if(src.getTrueSource() instanceof EntityLivingBase && src instanceof EntityDamageSource) {
			EntityLivingBase entity = (EntityLivingBase) src.getTrueSource();
			EntityDamageSource source = (EntityDamageSource) src;
			return DDDRegistries.trackers.isTracking("battlesign", entity) && source.getIsThornsDamage();
		}
		return false;
	}
	
	private static IDamageDistribution getDist(EntityPlayer player) {
		return DDDAPI.accessor.getDamageDistribution(player.getActiveItemStack());
	}

}
