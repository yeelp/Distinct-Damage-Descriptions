package yeelp.distinctdamagedescriptions.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import yeelp.distinctdamagedescriptions.util.lib.FilterListType;

public final class GeneralCategory {
	@Name("Enable Distinct Disc Drop")
	@Comment({
			"Enable the Distinct Disc to drop from Skeletons",
			"The disc will drop if a Skeleton is killed by a non-player mob that matches the list , non-skeleton, non-golem and non-tamable mob."})
	public boolean enableDiscDrop = true;
	
	@Name("Distinct Disc Drop Attacker List")
	@Comment({
		"The list of non-player mobs that, when killing a Skeleton, will cause that skeleton to drop the Distinct Disc.",
		"If Distinct Disc Drop Attacker List Type is set to BLACKLIST, any non-player mob NOT in this list will cause the disc to drop instead.",
		"Format is modid:mobid"
	})
	public String[] discDropList = DefaultValues.DISTINCT_DISC_LIST;
	
	@Name("Distinct Disc Drop Attacker List Type")
	@Comment("If WHITELIST, only mobs in the Distinct Drop Attacker List can make Skeletons drop the Distinct Disc. Otherwise, mobs in the list won't cause the disc to drop.")
	public FilterListType discListType = FilterListType.BLACKLIST;

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
