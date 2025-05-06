package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.capability;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.LinkedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;

public class WizardryLinkedDamageDistribution extends LinkedDamageDistribution {

	private IDamageDistribution ref;
	
	public WizardryLinkedDamageDistribution(String type) {
		this.ref = WizardryConfigurations.spellTypeDist.get(type.toLowerCase());
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
