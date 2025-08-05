package yeelp.distinctdamagedescriptions.config.client;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.ModConsts.DamageTypeNames;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionNumberFormat;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DamageDistributionNumberFormat;

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
	
	@Name("Armor Tooltip Format")
	@Comment({
		"Set the format of tooltips for Armor Distributions.",
		"    PLAIN: Formats the Armor Distribution as the approximate armor and toughness that the armor would provide per type. Using this option always uses gray colour for text (outside of the damage type). Types not listed in the tooltip have zero effectiveness.",
		"    PERCENT: Formats the Armor Distribution as the percent of effectiveness it has. Types not listed on the tooltip have zero effectiveness.",
		"    RELATIVE: Formats the Armor Distribution as its relative effectiveness to 100%. Types not listed on the tooltip have 100% effectiveness."
	})
	public ArmorDistributionNumberFormat armorFormat = ArmorDistributionNumberFormat.PERCENT;
	
	@Name("Damage Distribution Format")
	@Comment({
		"Set the format of tooltips for item Damage Distributions.",
		"    PLAIN: Formats the Damage Distribution as the approximate amount of damage that would be inflicted per type.",
		"    PERCENT: Formats the Damage Distribution as the percent of damage that would be inflicted of that type."
	})
	public DamageDistributionNumberFormat itemDamageFormat = DamageDistributionNumberFormat.PERCENT;
	
	@Name("Mob Damage Distribution Format")
	@Comment({
		"Set the format of tooltips for mob Damage Distributions that appear on spawn eggs if showMobDamage is true.",
		"    PLAIN: Formats the Damage Distribution as the approximate amount of damage that would be inflicted per type.",
		"    PERCENT: Formats the Damage Distribution as the percent of damage that would be inflicted of that type."
	})
	public DamageDistributionNumberFormat mobDamageFormat = DamageDistributionNumberFormat.PERCENT;
	
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
	
	@Name("Enable Particles")
	@Comment("Enable/disable resistance/weakness/immunity particles.")
	public boolean enableParticles = true;
	
	@Name("Enable SFX")
	@Comment("Enable/disable DDD's sound effects for resistance/weakness/immunity.")
	public boolean enableSfx = true;
	
	@Name("Built-In Damage Type Colours")
	@Comment("The colours used in tooltips for built in damage types. The colour needs to be a valid HEX colour (rrggbb)")
	@RequiresMcRestart
	public Map<String, String> damageTypeColours = Maps.newHashMap();
	
	public ClientCategory() {
		this.damageTypeColours.put(DamageTypeNames.UNKNOWN, "aaaaaa");
		this.damageTypeColours.put(DamageTypeNames.NORMAL, "000000");
		this.damageTypeColours.put(DamageTypeNames.SLASHING, "ffffff");
		this.damageTypeColours.put(DamageTypeNames.PIERCING, "ffffff");
		this.damageTypeColours.put(DamageTypeNames.BLUDGEONING, "ffffff");
		this.damageTypeColours.put(DamageTypeNames.ACID, "00e600");
		this.damageTypeColours.put(DamageTypeNames.COLD, "00dbd8");
		this.damageTypeColours.put(DamageTypeNames.FIRE, "db5f00");
		this.damageTypeColours.put(DamageTypeNames.FORCE, "db2100");
		this.damageTypeColours.put(DamageTypeNames.LIGHTNING, "0000cf");
		this.damageTypeColours.put(DamageTypeNames.NECROTIC, "404040");
		this.damageTypeColours.put(DamageTypeNames.POISON, "7600ba");
		this.damageTypeColours.put(DamageTypeNames.PSYCHIC, "ff0084");
		this.damageTypeColours.put(DamageTypeNames.RADIANT, "fffa5e");
		this.damageTypeColours.put(DamageTypeNames.THUNDER, "c9c9c9");
	}
}
