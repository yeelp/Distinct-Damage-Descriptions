package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

final class DamageClassifier implements IClassifier<DamageMap> {

	@Override
	public Optional<DamageMap> classify(CombatContext context) {
		Entity source = context.getImmediateAttacker();
		Entity trueSource = context.getTrueAttacker();
		return DDDRegistries.distributions.getDamageDistribution(context.getSource(), context.getDefender()).map(Optional::of).orElseGet(() -> {
			if(source != null && source instanceof IProjectile) {
				return DDDAPI.accessor.getDamageDistribution((IProjectile) context.getImmediateAttacker());
			}
			EntityLivingBase entityAttacker = null;
			if(source == null && trueSource instanceof EntityPlayer) {
				entityAttacker = (EntityLivingBase) trueSource;
			}
			else if(source != null && source instanceof EntityLivingBase) {
				entityAttacker = (EntityLivingBase) source;
			}
			return getDistForLivingEntity(entityAttacker);
		}).map((dist) -> {
			DamageMap map = dist.distributeDamage(context.getAmount());
			DDDHooks.fireDetermineDamage(source, trueSource, context.getDefender(), context.getSource(), map);
			if(map.containsValue(0.0f)) {
				Stream.Builder<DDDDamageType> builder = Stream.builder();
				map.entrySet().stream().filter((e) -> e.getValue() <= 0.0f).map(Entry::getKey).forEach(builder::add);
				builder.build().forEach(map::remove);				
			}
			return map;
		});
	}

	private static Optional<IDamageDistribution> getDistForLivingEntity(@Nullable EntityLivingBase attacker) {
		if(attacker == null) {
			return Optional.empty();
		}
		ItemStack heldItem = attacker.getHeldItemMainhand();
		if(heldItem.isEmpty()) {
			return DDDAPI.accessor.getDamageDistribution(attacker);
		}
		return DDDAPI.accessor.getDamageDistribution(heldItem);
	}

}
