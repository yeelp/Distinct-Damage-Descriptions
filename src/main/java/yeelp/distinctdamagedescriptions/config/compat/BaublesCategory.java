package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;

public final class BaublesCategory {

	@Name("Bauble Modifiers")
	@Comment({
		"Change DDD's Bauble modifiers.",
		"Each entry is of the form id;op;[(t,a)] where:",
		"   id is the namespaced id of the bauble item.",
		"   op is the operation performed:",
		"      0 - DAMAGE MOD: Boosts damage dealt of type t by a%. The value for a can be negative to reduce damage, though will not reduce it below 0.",
		"      1 - RESISTANCE MOD: Adds a% resistance to type t when taking damage. The value for a can be negative to reduce resistance.",
		"      2 - BRUTE FORCE: Bypasses up to a% of resistance to type t when attacking.",
		"      3 - SLY STRIKE: Has an a% chance of bypassing immunity to type t when attacking.",
		"      4 - IMMUNITY: Has an a% chance to grant immunity to type t when taking damage.",
		"   [(t,a)] is a list of tuples (t,a), separated by commas, where:",
		"      t is the type. Requires the 'ddd_' prefix. Can use s, p, b, instead for slashing, piercing or bludegeoning.",
		"      a is a decimal form of a percentage (e.g. 0.2 = 20%). The effect this value has depends on the operation specified before. Unless specified, it must always be positive.",
		"Baubles not listed here will have none of these effects.",
		"Malformed entries in this list are ignored."
	})
	@RequiresMcRestart
	public String[] baubleMods = DefaultValues.BAUBLE_MODS;
	
	@Name("Bauble Tooltip Colour Scheme")
	@Comment({
		"Set the colour scheme for DDD Bauble modifiers in tooltips. Only applies to damage and resistance modifiers (ops 0 and 1)",
		"    RED_GREEN: Use red when the modifier is less than 100% and green otherwise.",
		"    GRAYSCALE: Use dark gray when the modifier is less than 100% and white otherwise.",
		"    WHITE: Always use white."
	})
	public DDDTooltipColourScheme colourScheme = DDDTooltipColourScheme.RED_GREEN;
}
