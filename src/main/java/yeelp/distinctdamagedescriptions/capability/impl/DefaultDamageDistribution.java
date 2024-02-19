package yeelp.distinctdamagedescriptions.capability.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

public final class DefaultDamageDistribution extends DefaultDistribution implements IDamageDistribution {

	private static DefaultDamageDistribution instance;
	
	public static DefaultDamageDistribution getInstance() {
		return instance == null ? instance = new DefaultDamageDistribution() : instance;
	}
	
	private DefaultDamageDistribution() {
		super(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, 1.0f));
	}
	
	@Override
	public DamageMap distributeDamage(float dmg) {
		return DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution().distributeDamage(dmg);
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
