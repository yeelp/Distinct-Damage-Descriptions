package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class ConarmCategory {
	@Name("Armor Material Distribution")
	@Comment({
			"Determine the armor distribution for materials' plates pieces.",
			"Each entry is the same as a regular armor distribution entry, that is, of the form material;[(t,a)] where:",
			"   material is the registration name of the Tinker's material. Pretty much always lowercase",
			"   [(t,a}] is a comma sepatated list of tuples, (t,a), where:",
			"      t is the type. requires the 'ddd_' prefix, but can use s, p, b as shorthand for slashing, piercing and bludgeoning",
			"      a is the effectiveness this material has against that damage type."})
	@RequiresMcRestart
	public String[] armorMatDist = DefaultValues.ARMOR_MATERIAL_DISTRIBUTION;

	@Name("Armor Immunity Traits")
	@Comment({
			"A list of entries of the form material;types that grant immunity traits to armor pieces. Only works for built in types.",
			"material is the registration name of the Tinker's material. Pretty much always lowercase.",
			"types is a comma separated list of DDD types, with the 'ddd_' prefix."})
	@RequiresMcRestart
	public String[] armorImmunities = DefaultValues.ARMOR_IMMUNITIES;

	@Name("Armor Immunity Trait Location")
	@Comment({
			"This dictates where DDD puts armor immunity traits, on the platers or on the core.",
			"Only applies to new pieces made. Will not overwrite or change existing armor pieces.",
			"No Trim option, as that's a little OP."})
	@RequiresMcRestart
	public TraitLocation traitLocation = TraitLocation.PLATES;

	/**
	 * Can't have this TraitLocation directly reference Conarm's core and plates
	 * part strings, as that would cause problems if Conarm isn't loaded. Has to be
	 * handled elsewhere.
	 * 
	 * @author Yeelp
	 *
	 */
	public enum TraitLocation {
		CORE,
		PLATES;
	}
}
