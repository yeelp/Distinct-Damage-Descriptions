package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;

public final class EnderSkillsCategory {

	@Name("Use Thorny Distribution")
	@Comment({
			"If enabled, DDD will have the Thorny skill inflict piercing damage back to attackers similar to the Guardian Thorns extra Distribution.",
			"Having the Thorns enchantment activate while the Thorny skill is active will do piercing damage instead of force damage, as this distribution has priority over regular Thorns.",
			"Otherwise, it will behave like the Thorns enchantment and do force damage if the thorns distribution is enabled."})
	public boolean useThornyDistribution = true;

	@Name("Skill Distribution")
	@Comment({
			"Set what Ender Skills do what kind of damage. If the skill is inflicting damage over time effects, those effects should be defined in Skill DOT Distirbution, not here.",
			"Some skills do damage via fire or explosions; these methods of damage are accounted for already with DDD's fire and explosion distributions. As such, skills that only do damage in that way don't need to be included in this list.",
			"If they are included, they take precedence over DDD's fire and explosion distributions. The default values include all skills that require a distribution to be set to 'work'.",
			"Format is id;[(t,a)] where:",
			"   id is the namespaced id of the skill. If the skill belongs to enderskills (and not added by some addon), the mod id can be omitted.",
			"   [(t,a)] defines a valid damage distribution as described in the damage category configs.",
			"ALL weights must add up to 1.",
			"Types not listed here, will do force damage.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] skillDistribution = DefaultValues.SKILL_DISTRIBUTION;

	@Name("Skill DOT Distribution")
	@Comment({
			"Some Skills from Ender Skills apply a damage over time effect. Set what damage these damage over time effects deal here.",
			"Some damage over time effects count as fire damage or explosion damage and as such, are already accounted for with DDD's fire and explosion distributions. As such, they aren't included here.",
			"If they are included, they take precedence over DDD's fire and explosion distributions. The default values here include all damage over time effects that require a distribution to 'work'.",
			"Format is id;[(t,a)] where:",
			"   id is the namespaced id of the damage over time effect. (It still counts as a skill, but will be differentiated from regular skill distributions). If the skill belongs to enderskills (and not added by some addon), the mod id can be omitted.",
			"   [(t,a)] defines a valid damage distribution as described in the damage category configs.",
			"ALL weights must add up to 1.",
			"Types not listed here, will do force damage.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] skillDotDistribution = DefaultValues.SKILL_DOT_DISTRIBUTION;

	@Name("Shadow Distribution")
	@Comment("The distribution the shadow uses with the Shadow skill; a valid damage distribution as defined in the damage category")
	public String shadowDist = DefaultValues.SHADOW_DISTRIBUTION;
	
	@Name("Smash Distribution")
	@Comment("The distribution the Smash skill uses; a valid damage distribution as defined in the damage category.")
	public String smashDist = DefaultValues.SMASH_DISTRIBUTION;

	@Name("Show Skill Type Info")
	@Comment({
			"If enabled, show the kind of damage a skill deals.",
			"Not it's damage distribution; this shows if the skill uses direct damage (and should thus be defined in Skill Distributions) or DOT damage (and should thus be defined in Skill DOT Distribution",
			"You also need DDD's Developer Mode enabled; this functions as a Developer Mode toggle for EnderSkills.",
			"Setting this to ENABLED_AND_IN_CHAT will additionally send this information to the in game chat (i.e. to all players in game)"})
	public DeveloperStatus showSkillTypeInfo = DeveloperStatus.DISABLED;

	@Name("Show Skill Name Info")
	@Comment({
			"If enabled, show the registry name of a skill when it is used.",
			"You also need DDD's Developer Mode enabled; this functions as a Developer Mode toggle for EnderSkills.",
			"Setting this to ENABLED_AND_IN_CHAT will additionally send this information to the in game chat (i.e. to all players in game)"})
	public DeveloperStatus showSkillNameInfo = DeveloperStatus.DISABLED;
}
