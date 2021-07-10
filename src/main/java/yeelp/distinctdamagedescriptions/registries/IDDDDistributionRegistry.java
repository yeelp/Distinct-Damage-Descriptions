package yeelp.distinctdamagedescriptions.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDDamageSource;

public interface IDDDDistributionRegistry extends IDDDRegistry<DDDPredefinedDistribution> {
	Set<DDDDamageType> getDamageTypes(DamageSource src, EntityLivingBase target);

	IDamageDistribution getDamageDistribution(DamageSource src, EntityLivingBase target);

	default ITextComponent getDeathMessageForDist(IDamageDistribution dist, DamageSource src, @Nullable Entity attacker, @Nonnull EntityLivingBase defender) {
		if(src instanceof DDDDamageSource && dist != null) {
			DDDDamageSource dSrc = (DDDDamageSource) src;
			double weight = Math.random();
			ArrayList<DDDDamageType> lst = Lists.newArrayList(dist.getCategories());
			Collections.shuffle(lst);
			for(DDDDamageType type : lst) {
				weight -= dist.getWeight(type);
				if(weight <= 0) {
					return DDDRegistries.damageTypes.getDeathMessageForType(type, dSrc, attacker, defender);
				}
			}
			return DDDRegistries.damageTypes.getDeathMessageForType(lst.get(0), dSrc, attacker, defender);
		}
		return src.getDeathMessage(defender);
	}
}
