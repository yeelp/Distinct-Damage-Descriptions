package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDBaseMap;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDamageDistribution;

public final class CTDamageDistribution extends CTDistribution implements ICTDamageDistribution {

	public CTDamageDistribution(IDamageDistribution dist) {
		super(dist);
	}

	@Override
	public ICTDDDBaseMap distribute(float dmg) {
		return new CTDDDBaseMap(((IDamageDistribution) this.getDist()).distributeDamage(dmg));
	}

}
