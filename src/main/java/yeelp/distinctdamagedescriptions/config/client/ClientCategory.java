package yeelp.distinctdamagedescriptions.config.client;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;

public final class ClientCategory {
	@Name("Show Damage Distribution Tooltip for all items")
	@Comment("If true, the Damage Distribution tooltip appears for all items. If false, it only appears for items with a damage distribution manually set.")
	public boolean alwaysShowDamageDistTooltip = true;

	@Name("Never Hide Info")
	@Comment("If true, All Distint Damage Descriptions related tooltip information will always be shown (Basically, SHIFT or CTRL never need to be held on tooltips).")
	public boolean neverHideInfo = false;

	@Name("Use Damage Type Icons")
	@Comment({
			"If true, Distinct Damage Descriptions will use icons for built in damage types (slashing, piercing, bludgeoning).",
			"These icons will appear in place of those names everwhere except under the \"Starting Immunities\" in spawn egg tooltips."})
	public boolean useIcons = false;

	@Name("Show Mob Damage On Spawn Items")
	@Comment({
			"If true, Distinct Dasmage Descriptions will show mob damage in spawn egg tooltips when holding <SHIFT>.",
			"Regular item damage distribution information will not be shown so long as the spawn eggs are not configured to have a particular distribution.",
			"It is recommended to not set a distribution for spawn eggs if this is set to true, as this will make the tooltip cleaner."})
	public boolean showMobDamage = false;

	@Name("Use Numerical Values When Possible")
	@Comment({
			"If true, Distinct Damage Descriptions will try to show numerical values when possible instead of percent values.",
			"This works for armor and items only. Projectiles and resistances don't have numerical values to use, so the config doesn't apply here."})
	public boolean showNumberValuesWhenPossible = false;
	
	@Name("Armor Tooltip Colour Scheme")
	@Comment({
		"Set the colour scheme for armor tooltips.",
		"    RED_GREEN: Use red when the armor effectiveness is less than 100% and green otherwise.",
		"    GRAYSCALE: Use dark gray when the armor effectiveness is less than 100% and white otherwise.",
		"    WHITE: Always use white."
	})
	public DDDTooltipColourScheme armorColourScheme = DDDTooltipColourScheme.RED_GREEN;
	
	@Name("Mob Resistances Colour Scheme")
	@Comment({
		"Set the colour scheme for mob resistance tooltips.",
		"    RED_GREEN: Use red when a mob's resistance is less than 0% and green otherwise.",
		"    GRAYSCALE: Use dark gray when a mob's resistance is less than 0% and white otherwise.",
		"    WHITE: Always use white."
	})
	public DDDTooltipColourScheme mobResistColourScheme = DDDTooltipColourScheme.RED_GREEN;
	
	
}
