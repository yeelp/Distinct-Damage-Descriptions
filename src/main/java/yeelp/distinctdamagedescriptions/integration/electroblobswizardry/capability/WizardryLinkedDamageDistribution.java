package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.capability;

import electroblob.wizardry.util.MagicDamage.DamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.LinkedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;

public class WizardryLinkedDamageDistribution extends LinkedDamageDistribution {

	private IDamageDistribution ref;
	
	public WizardryLinkedDamageDistribution(DamageType type) {
		this.ref = WizardryConfigurations.spellTypeDist.get(type.name().toLowerCase());
	}
	
	@Override
	protected IDamageDistribution getDamageDistribution() {
		return this.ref;
	}

	@Override
	public IDamageDistribution copy() {
		return this;
	}

}
