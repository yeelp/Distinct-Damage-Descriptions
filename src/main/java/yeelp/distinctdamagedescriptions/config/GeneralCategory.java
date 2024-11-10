package yeelp.distinctdamagedescriptions.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;

public final class GeneralCategory {
	@Name("Enable Distinct Disc Drop")
	@Comment({
			"Enable the Distinct Disc to drop from Skeletons",
			"The disc will drop if a Skeleton is killed by a non-player, non-skeleton, non-golem and non-tamable mob."})
	public boolean enableDiscDrop = true;

	@Name("Suppress Warnings")
	@Comment("If warning messages from Distinct Damage Descriptions are clogging the log, you can disable them here. This may be indicative of a real issue though, so make sure there's no real issue first!")
	public boolean suppressWarnings = false;

	@Name("Suppress Registration Info")
	@Comment("If registration messages from Distinct Damage Descriptions are clogging the log, you can disable them here.")
	public boolean suppressRegistrationInfo = false;

	@Name("Generate Configs")
	@Comment("If set to true, and Distinct Damage Description will try to generate appropriate config values for weapons, mobs, armor and projectiles on the fly during gameplay. This isn't super accurate and is rather primitive.")
	public boolean generateStats = false;

	@Name("Generate JSON")
	@Comment("If set, DistinctDamageDescriptions will generate example JSON files on startup for custom damage types, creature types and filters.")
	public boolean generateJSON = true;

	@Name("Use Custom Damage Types From JSON")
	@Comment("If true, Distinct Damage Descriptions will load and enable custom damage types from JSON found in config/distinctdamagedescriptions/damageTypes")
	@RequiresMcRestart
	public boolean useCustomDamageTypesFromJSON = false;

	@Name("Use Custom Death Messages")
	@Comment("Should Distinct Damage Descriptions use its custom death messages for damage types?")
	@RequiresWorldRestart
	public boolean useCustomDeathMessages = false;

	@Name("Use Creature Types From JSON")
	@Comment({
			"If true, DistinctDamageDescriptions will load custom creature types from JSON located in config/distinctdamagedescriptions/creatureTypes.",
			"These JSON files can be used to apply potion/critical hit immunities to large swaths of mobs at once. Also usuable in CraftTweaker."})
	@RequiresMcRestart
	public boolean useCreatureTypesFromJSON = false;

	@Name("Enable Adaptive Weakness")
	@Comment({
			"Enable Adaptive Weakness",
			"Adaptive Weakness kicks in when a mob that is adaptive is hit by type(s) they are weak to.",
			"Their adaptive amount is set to a percentage of the base amount, that percentage being equal to exp(avg), where: ",
			"   exp being the exponential function",
			"   avg is the average of all of the mobs weakness values that were hit (which is negative)",
			"This triggers only when a mob's adaptability is triggered."})
	public boolean enableAdaptiveWeakness = false;

	@Name("Register Potions")
	@Comment("If true, DDD will register and add potions that grant damage/resistance buffs/debuffs. Requires Register Potion Effects to be true.")
	@RequiresMcRestart
	public boolean enablePotionRegistration = false;

	@Name("Register Potion Effects")
	@Comment("If true, DDD will register potion effects that grant damage/resistance buffs/debuffs")
	@RequiresMcRestart
	public boolean enablePotionEffectRegistration = false;
}
