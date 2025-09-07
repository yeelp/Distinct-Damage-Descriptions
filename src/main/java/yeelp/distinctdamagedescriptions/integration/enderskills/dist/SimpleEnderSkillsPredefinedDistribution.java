package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public class SimpleEnderSkillsPredefinedDistribution extends EnderSkillsPredefinedDistribution {

	private final String damageSourceName;
	public SimpleEnderSkillsPredefinedDistribution(String damageSourceName, Supplier<String> configSup, Supplier<String> fallbackSup) {
		super(String.format("Ender Skills %s", YLib.capitalize(damageSourceName)), configSup, fallbackSup);
		this.damageSourceName = damageSourceName;
	}

	@Override
	protected boolean isApplicable(DamageSource src, EntityLivingBase target) {
		return src.damageType.equals(this.damageSourceName);
	}

}
