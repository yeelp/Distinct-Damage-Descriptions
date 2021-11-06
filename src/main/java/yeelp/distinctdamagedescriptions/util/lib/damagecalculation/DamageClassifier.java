package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DamageMap;

final class DamageClassifier implements IClassifier<DamageMap> {

	@Override
	public Optional<DamageMap> classify(CombatContext context) {
		IDamageDistribution dist = DDDRegistries.distributions.getDamageDistribution(context.getSource(), context.getDefender());
		Entity source = context.getImmediateAttacker();
		Entity trueSource = context.getSource().getTrueSource();
		if(dist.getWeight(DDDBuiltInDamageType.NORMAL) == 1) {
			if(source != null && source instanceof IProjectile) {
				dist = DDDAPI.accessor.getDamageDistribution((IProjectile) context.getImmediateAttacker());				
			}
			else {
				EntityLivingBase entityAttacker = null;
				if(source == null && trueSource instanceof EntityPlayer) {
					entityAttacker = (EntityLivingBase) trueSource;
				}
				else if(source != null && source instanceof EntityLivingBase) {
					entityAttacker = (EntityLivingBase) source;
				}
				dist = getDistForLivingEntity(entityAttacker);
			}
		}
		if(dist != null) {
			DamageMap map = dist.distributeDamage(context.getAmount());
			MinecraftForge.EVENT_BUS.post(new DetermineDamageEvent(source, trueSource, context.getDefender(), context.getSource(), map));
			return Optional.of(map);
		}
		return Optional.empty();
	}
	
	private static IDamageDistribution getDistForLivingEntity(@Nullable EntityLivingBase attacker) {
		if(attacker == null) {
			return null;
		}
		ItemStack heldItem = attacker.getHeldItemMainhand();
		if(heldItem.isEmpty()) {
			return DDDAPI.accessor.getDamageDistribution(attacker);
		}
		return DDDAPI.accessor.getDamageDistribution(heldItem);
	}

}
