package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public class ElectroblobsWizardryCategory {

	@Name("Spell Distributions")
	@Comment({
		"Set a Spell's damage distribution. Spell damage distributions only apply to spells that actually inflict damage from Electroblob's Wizardry's damage sources (something like Snowball will use the snowball's projectile distribution).",
		"While some spells are able to be configured as either projectile distributions (like magic missile) or entity distributions (like earthquake), ",
		"those spells can be set here as well, and distributions set here will take precedence over those set elsewhere (i.e. giving magic missile a distribution here will take precedence over a projectile distribution set in the projectile list",
		"Basically, just set all spell distributions here!",
		"Format is id;[(t,a)] where:",
		"   id is the mod id of the spell (e.g. ebwizardry:arc). The mod id can be excluded ONLY if the spell is from ebwizardry. Spells from addon mods will need the full namespaced id.",
		"   [(t,a)] is a comma separated list of tuples that create a valid damage distribution, as described in the damage category configs.",
		"ALL weights must add up to 1.",
		"Spells not listed here will deal force damage if they are correctly detected as a Wizardry Spell and do damage as mentioned above.",
		"Malformed entries in this list will be ignored."
	})
	public String[] spellDistributions = DefaultValues.SPELL_DISTS;
	
	@Name("Minion Resistances")
	@Comment({
		"Specify what resistances minions mimic. Minions will basically copy the entire config entry (adaptability included) of the specified entity and use it as their own.",
		"They will only copy resistances if and only if they don't have an entry in the mob resistances config already.",
		"You technically don't need this, you can specify the mob resistances for each minion in the mob resistances section, this is just to save some obvious copying,",
		"such as how zombie minions are likely to be configured to use the same resistances as regular zombies.",
		"Format is minionid;mobid where:",
		"   minionid is the namespaced id of the minion. The mod id can be excluded ONLY if the minion is from ebwizardry. Minions added by addons will need the full namespaced id.",
		"   mobid is the id of the mob the minion resembles and will copy the mob resistances of.",
		"If mobid is not in the config, the minion will get the default resistances copied instead.",
		"Minions not listed here will check the regular mob resistances config for a config entry.",
		"Malformed entries in this list will be ignored."
	})
	public String[] minionResistances = DefaultValues.MINON_RESISTANCES;
}