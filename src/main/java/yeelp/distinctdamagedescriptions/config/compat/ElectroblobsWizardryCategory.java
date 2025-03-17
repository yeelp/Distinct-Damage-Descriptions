package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;

public class ElectroblobsWizardryCategory {
	
	@Name("Minion Capabilities")
	@Comment({
		"Specify what capabilities minions mimic. Minions will basically copy the entire config entry for mob resistances (adaptability included) and damage distribution of the specified entity and use it as their own.",
		"They will only copy resistances if and only if they don't have an entry in the mob resistances config already. Same applies to damage distirbution.",
		"You technically don't need this, you can specify the mob resistances and damage distribution for each minion in the mob resistances and mob damage section, this is just to save some obvious copying,",
		"such as how zombie minions are likely to be configured to use the same resistances and damage as regular zombies.",
		"Format is minionid;mobid where:",
		"   minionid is the namespaced id of the minion. The mod id can be excluded ONLY if the minion is from ebwizardry. Minions added by addons will need the full namespaced id.",
		"   mobid is the id of the mob the minion resembles and will copy the capabilities of.",
		"If mobid is not in the config, the minion will get the default resistances and damage copied instead.",
		"Minions not listed here will check the regular mob resistances config for a config entry.",
		"Malformed entries in this list will be ignored."
	})
	public String[] minionCapabilities = DefaultValues.MINON_CAPABILITIES;
	
	@Name("Spell Damage Type Distributions")
	@Comment({
		"Set what damage spells do based off their damage type. The defaults include all the damage types based of their internal name.",
		"format is type;[(t,a)] where:",
		"   type is the Wizardry damage type.",
		"   [(t,a)] is a comma separated list of tuples that create a valid damage distribution, as described in the damage category configs.",
		"ALL weights must add up to 1.",
		"Types not listed here, will do force damage.",
		"Malformed entries in this list will be ignored."
	})
	public String[] spellDamageTypeDistributions = DefaultValues.SPELL_TYPE_DISTRIBUTIONS;
	
	@Name("Linked Throwables")
	@Comment({
		"Link throwables to their damage type.",
		"What this does is give DDD a hint as to what damage type a throwable inflicts. Then, DDD will link a projectile distribution to the item, so it shows the damage distribution in the tooltip.",
		"This can be done manually through the projectil distribution config, these is just to make it easier, as all you need here is the Wizardry damage type it deals, and not a full damage distribution.",
		"If you specify a damage type that isn't defined in Spell Damage Type Distributions, the entry will be ignored.",
		"Format is id;type where:",
		"   id is the namespaced ITEM id of the throwable. The mod id can be excluded ONLY if the item is from ebwizardry. Items added by addon mods will need the full namespaced id.",
		"   type is the Wizardry damage type. NOT the DDD damage type.",
		"Malformed entries in this list will be ignored."
	})
	public String[] linkedThrowables = DefaultValues.LINKED_THROWABLES;
	
	@Name("Show Spell Distirbution Tooltips")
	@Comment("If enabled, DDD will add distribution info to the currently selected spell in a wand or spell stored in a scroll or book if it is configured in spellDamageType. The distribution will be shown by holding CTRL.")
	public boolean showSpellDistributions = true;
	
	@Name("Spell Damage Type")
	@Comment({
		"This is to give DDD a hint as to what spells inflict what kind of damage from EBWizardry. Spells configured here will have special Spell Distribution tooltips show up when they are the selected spell on a wand or stored in a scroll or book.",
		"Spells not configured here won't have that tooltip show up at all. Consider using showSpellNameInfo and showSpellDamageInfo if you are trying to configure this.",
		"Format is id;type where:",
		"   id is the namespaced id of the spell. The mod id can be excluded ONLY if the spell is from ebwizardry. Spells added by other addons will need the full namespaced id.",
		"   type is the Wizardry damage type. NOT the DDD damage type.",
		"Malformed entries in this list will be ignored."
	})
	public String[] spellDamageType = DefaultValues.SPELL_DAMAGE_TYPE;
	
	@Name("Show Spell Damage Info")
	@Comment({
		"If enabled, DDD will gather the information from a spell's damage to report what kind of damage type it inflicts when it deals damage to something. Used with showSpellNameInfo to configure spellDamageType.",
		"You also need DDD's Developer Mode enabled as well; this functions as an EBWizardry specific Developer Mode toggle.",
		"Setting this to ENABLED_AND_IN_CHAT will additionally send this information to the in game chat (i.e. to all players in game)"
	})
	public DeveloperStatus showSpellDamageInfo = DeveloperStatus.DISABLED;
	
	@Name("Show Spell Name Info")
	@Comment({
		"If enabled, DDD will show the registry name of the spell being cast. Used with showSpellDamageInfo to configure spellDamageType.",
		"You also need DDD's Developer Mode enabled as well; this functions as an EBWizardry specific Developer Mode toggle.",
		"Setting this to ENABLED_AND_IN_CHAT will additionally send this information to the in game chat (i.e. to all players in game)"
	})
	public DeveloperStatus showSpellNameInfo = DeveloperStatus.DISABLED;
}