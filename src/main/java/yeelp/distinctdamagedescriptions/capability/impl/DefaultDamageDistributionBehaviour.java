package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDefaultDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

public enum DefaultDamageDistributionBehaviour implements IDefaultDistribution, IDamageDistribution{
	BLUDGEONING(DDDBuiltInDamageType.BLUDGEONING),
	PIERCING(DDDBuiltInDamageType.PIERCING),
	NORMAL(DDDBuiltInDamageType.NORMAL),
	BYPASS(DDDBuiltInDamageType.UNKNOWN);

	private final DDDDamageType type;
	
	private DefaultDamageDistributionBehaviour(DDDDamageType type) {
		this.type = type;
	}
	@Override
	public float getWeight(DDDDamageType type) {
		return type == this.type ? 1.0f : 0.0f;
	}

	@Override
	public Set<DDDDamageType> getCategories() {
		return ImmutableSet.of(this.type);
	}

	@Override
	public DamageMap distributeDamage(float dmg) {
		DamageMap map = DDDMaps.newDamageMap();
		map.put(this.type, dmg);
		return map;
	}

	@Override
	public IDamageDistribution copy() {
		return this;
	}

	@Override
	public IDamageDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(EntityLivingBase owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(IProjectile owner) {
		return this;
	}

}
