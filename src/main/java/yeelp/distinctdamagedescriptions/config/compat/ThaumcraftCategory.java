package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class ThaumcraftCategory {

	@Name("Enabled")
	@Comment({
			"Toggle Thaumcraft integration on or off.",
			"This is in case you have DDD and Thaumcraft installed but don't want to also install Fermium Booter.",
			"This turns off the integration entirely. Even if enabled, the integration only loads if Thaumcraft is present."})
	public boolean enabled = true;

	@Name("Aspect Distribution")
	@Comment({
			"Configure the damage distribution associated with each aspect.",
			"This is used when casting a spell; DDD analyzes the different aspects in each component and averages out their damage distributions to create a final damage distribution.",
			"Format is aspect;[(t,a)] where:",
			"   aspect is the name of the aspect. All lowercase.",
			"   [(t,a)] is a comma separated list that defines a damage distribution as described in the damage category configs.",
			"ALL weights must add to 1.",
			"Aspects not listed here will do force damage.",
			"Malformed entries in this list will be ignored."})
	public String[] aspectDistributions = DefaultValues.ASPECT_DIST;

	@Name("Use Taint Distribution")
	@Comment("If true, DDD will use the distribution listed under Taint Distribution for damage caused by the Taint potion effect.")
	public boolean useTaintDist = true;
	
	@Name("Use Liquid Death Distribution")
	@Comment("If true, DDD will use the distribution listed under Liquid Death Distribution for damage caused by Liquid Death.")
	public boolean useLiquidDeathDist = true;
	
	@Name("Taint Distribution")
	@Comment("Set the distribution caused by the Taint potion effect. The format is a comma separated list [(t,a)] to define a damage distribution the same way as described in the damage category configs.")
	public String taintDistribution = DefaultValues.TAINT_DISTRIBUTION;

	@Name("Liquid Death Distribution")
	@Comment("Set the distribution caused by Liquid Death. The format is a comma separated list [(t,a)] to define a damage distribution the same way as described in the damage category configs.")
	public String dissolveDistribution = DefaultValues.DISSOLVE_DISTRIBUTION;
}
