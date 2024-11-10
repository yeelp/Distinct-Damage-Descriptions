package yeelp.distinctdamagedescriptions.config.readers;

import yeelp.distinctdamagedescriptions.capability.distributors.ShieldDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public final class DDDShieldClassesConfigReader implements DDDConfigReader {

	@Override
	public void read() {
		for(String s : ModConfig.compat.shieldClasses) {
			if(ConfigReaderUtilities.isCommentEntry(s)) {
				continue;
			}
			try {
				if(s.contains(".")) {
					//likely a fully qualified class. We can check if it exists.
					Class.forName(s);
					ShieldDistributionCapabilityDistributor.addFullyQualifiedClassNameAsShield(s);
				}
				else {
					ShieldDistributionCapabilityDistributor.addSimpleClassNameAsShield(s);
				}
			}
			catch(ClassNotFoundException e) {
				//Class doesn't exist, we don't need to add it. Continue.
				continue;
			}
		}
	}

	@Override
	public String getName() {
		return "DDD Compat: Shield Classes";
	}

	@Override
	public boolean shouldTime() {
		return false;
	}

}
