package yeelp.distinctdamagedescriptions.config.resists;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultArmorDistributionBehaviour;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultShieldDistributionBehaviour;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.util.lib.ArmorCalculationType;
import yeelp.distinctdamagedescriptions.util.lib.ArmorParsingType;
import yeelp.distinctdamagedescriptions.util.lib.NegativeArmorHandling;

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
			"Shields not listed here will use the default shield distribution option.",
			"Shield effectiveness determines how much damage a shield can block. A shield with 0.3 slashing effectiveness can only block 30% of incoming slashing damage. The remaining 70% goes through the shield and damages the player, following regular damage calculation.",
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
			"Armors that aren't listed here will use the default armor distribution option.",
			"Resistances effectiveness determines how armor points are distributed. That is, an value of 0.5 means that armor only uses 50% of its usual armor points when defending against that type",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] armorResist = DefaultValues.ARMOR_BASE_RESISTS;

	@Name("Default Armor Distribution")
	@Comment({
			"Change the default behaviour of armor pieces with no distribution defined in the Armor Resistance config. Note that setting this to anything but NO_EFFECTIVENESS will make undefined armor pieces very strong, as their armor values will basically be multiplied by the number of types being inflicted (that they are effective against)",
			"    NO_EFFECTIVENESS: Armor not defined will have no effectiveness. DDD calculated damage will effectively go right through the armor.",
			"    ALL_EFFECTIVENESS: Armor not defined will have 100% effectiveness against all types. This sort of reverts the armor to \"vanilla\" behaviour, though again, the strength of the armor will be multiplied by the amount of damage types inflicted.",
			"    EFFECTIVE_TO_REGULAR_TYPES: Armor not defined will have 100% effectiveness to all regular types. Regular types are types that DDD doesn't treat differently (only two \"non-regular\" types exist, and they are built-in.",
			"    ALLOW_BYPASS_DAMAGE_TYPE: Armor not defined will have 100% effectiveness to almost all types, except for one internal built-in type; the unknown type. This unknown type will bypass the armor entirely."})
	public DefaultArmorDistributionBehaviour defaultArmorResists = DefaultArmorDistributionBehaviour.NO_EFFECTIVENESS;

	@Name("Default Shield Distribution")
	@Comment({
			"Change the default behaviour of shields with no distribution defined in the Shield Effectiveness config.",
			"    NO_EFFECTIVENESS: Shields not defined will have no effectiveness, i.e. they will block no damage at all.",
			"    ALL_EFFECTIVENESS: Shields not defined will have 100% effectiveness to all types. This sort of reverts the shields to their normal \"vanilla\" behaviour.",
			"    EFFECTIVE_TO_REGULAR_TYPES: Shields not defined will have 100% effectiveness to all regular types. Regular types are types that DDD doesn't treat differently (only two \"non-regular\" types exist, and they are built-in.",
			"    ALLOW_BYPASS_DAMAGE_TYPE: Shields not defined will have 100% effectiveness to almost all types, except for one internal built-in type; the unknown type. This unknown type will bypass the shield entirely."})
	public DefaultShieldDistributionBehaviour defaultShieldResists = DefaultShieldDistributionBehaviour.NO_EFFECTIVENESS;

	@Name("Armor Calculation Rule")
	@Comment({
			"Define the rule DDD will use when calculating how armor effectiveness translates to actual armor values.",
			"    ADD: For all armor values that are applicable, DDD will add them together. This typically means that damage that gets split into multiple types will be resisted more easily as the effectiveness of armor will be added together.",
			"    MAX: DDD will take the maximum armor values that are applicable. So DDD will never give a higher armor value than the highest armor effectiveness rating that is applicable.",
			"    AVG: DDD will average together all applicable armor values."})
	public ArmorCalculationType armorCalcRule = ArmorCalculationType.ADD;

	@Name("Armor Parsing Rule")
	@Comment({
			"Define the rule DDD will use when parsing the armorResist config.",
			"    LITERAL: DDD will parse the config exactly as it is written. Only the types written in an item's armor distribution will be used.",
			"    IMPLIED: DDD will add in armor effectiveness for types not mentioned in an armor distribution. For example, an armor distribution of [(s, 0.8), (p, 0.5)] will have all the other registered DDD types added to its distribution. The effectiveness these types get is defined by impliedArmorEffectivess."})
	public ArmorParsingType armorParseRule = ArmorParsingType.LITERAL;

	@Name("Negative Armor Handling")
	@Comment({
			"Change how DDD handles negative armor and toughness.",
			"    DEFAULT: DDD's default behaviour, which is to do nothing different. When DDD computes armor modifications, it computes it relative to the original negative armor/toughness value, which results in a positive armor modification value if the effectiveness is less than 100% and a negative armor modification value if the effectivenss if over 100%.",
			"    ABS: DDD will convert negative armor/toughness to their absolute value when computing armor modifications relative to their original armor/toughness values. This results in larger negative armor/toughness values for armor with effectivness below 100%, and smaller negative armor/toughness values for armor with effectiveness above 100%."})
	public NegativeArmorHandling negativeRule = NegativeArmorHandling.DEFAULT;

	@Name("Implied Armor Effectiveness")
	@Comment({
			"The armor effectiveness that non-specified types get when using the IMPLIED armor parsing rule."})
	@RangeDouble(min = 0.0)
	public double impliedArmorEffectiveness = 1.0;

	@Name("Enable Shield Calculations")
	@Comment("If enabled, DDD will use shields and shield distributions. If turned off, DDD will let shields behave as they would in vanilla. DDD won't even assign or detect Shield Distributions if disabled.")
	public boolean enableShieldCalcs = true;

	@Name("Enable Armor Calculations")
	@Comment("If enabled, DDD will use armor and armor distributions. If turned off, DDD will not alter armor values in damage calculations, allowing armor to behave as it would in vanilla. Armor Distributions won't even be assigned or detected if disabled.")
	public boolean enableArmorCalcs = true;
}
