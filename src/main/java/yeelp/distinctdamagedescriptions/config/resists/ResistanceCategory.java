package yeelp.distinctdamagedescriptions.config.resists;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class ResistanceCategory {
	@Name("Mob Base Resistance/Weakness")
	@Comment({
			"Modify the base resistance/weakness of mobs.",
			"Each entry is of the form id;[(t,a)];[immunities];adaptive;amount where:",
			"   id is the namespaced id of the mob (e.g. minecraft:zombie)",
			"   [(t,a)] is a list of comma separated tuples (t,a), of damage types this mob resists.",
			"      t is the damage type this mob resists. Requires the 'ddd_' prefix. Can use s, p, b instead as shorthand for slashing, piercing, or bludgeoning.",
			"      a is the base percent of resistance this mob has to that damage type.",
			"   [immunities] is a comma separated list of damage types that this mob is immune to. Requires the 'ddd_' prefix for each damage type.",
			"   adaptive is a decimal in the range [0,1] indicating the percent chance that this mob has adaptive immunity, with 0 being never, and 1 being always.",
			"   amount is the amount resistances change for this mob when adaptability triggers",
			"Mobs that aren't listed here will have no resistances. Positive values indicate a resistance, negative values indicate a weakness.",
			"Resistances and weaknesses are percentage based. That is, an value of 0.5 means that mob takes 50% less damage from that type, and a value of -0.5 means that mob takes 50% more damage from that type",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] mobBaseResist = DefaultValues.MOB_BASE_RESISTS;

	@Name("Player Base Resistance")
	@Comment({
			"Set the base resistance values for the player",
			"This is likely only applicable to new worlds! Old worlds may not reflect this change!",
			"The format for this is the same as Mob Base Resistance/Weakness, minus the leading id part and adaptability chance. All properties are usuable (immunities, adaptability), except adaptability chance, which will be set to 1 if adaptability amount is set to a non zero value"})
	@RequiresMcRestart
	public String playerResists = DefaultValues.PLAYER_BASE_RESISTS;

	@Name("Shield Effectiveness")
	@Comment({
			"Modify how shields block damage.",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the item (e.g. minecraft:shield)",
			"   [(t,a)] is a list of comma separated tuples of damage types this shield blocks.",
			"      t is the type this shield blocks. Requires the 'ddd_' prefix. Can use s, p, b as shorthand for slashing, piercing and bludgeoning damage, respectively.",
			"      a is the effectiveness the shield has against that damage type.",
			"Shields not listed here will act as normal shields (will block all damage they can interact with).",
			"Shield effectiveness determines how much physical damage a shield can block. A shield with 0.3 slashing effectiveness can only block 30% of incoming slashing damage. The remaining 70% goes through the shield and damages the player, following regular damage calculation.",
			"Blocking damage will still knock the attacker back, but the knockback strength is a percentage of the original vanilla knockback; that percentage comes from the amount of damage actually reduced (a shield that only blocks 33% of the incoming damage will knock the attacker back by about 33% of the vanilla amount).",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] shieldResist = DefaultValues.SHIELD_BASE_RESISTS;

	@Name("Armor Resistance")
	@Comment({
			"Modify the base resistance effectiveness of armor",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the item (e.g. minecraft:diamond_chestplate)",
			"   [(t,a)] is a list of comma separated tuples of damage types this armor resists (if enabled).",
			"      t is the damage type this armor resists. Requires the 'ddd_' prefix.",
			"      a is the armor's effectiveness against that damage type.",
			"Armors that aren't listed here will have no effectiveness (this doesn't mean the armor does nothing).",
			"Resistances effectiveness determines how armor points are distributed. That is, an value of 0.5 means that armor only uses 50% of its usual armor points when defending against that type",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] armorResist = DefaultValues.ARMOR_BASE_RESISTS;
}
