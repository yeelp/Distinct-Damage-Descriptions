package yeelp.distinctdamagedescriptions.config.damage;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class DamageCategory {
	@Name("Mob Base Damage")
	@Comment({
			"Modify the base damage type distribution of mobs.",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the mob (e.g. minecraft:zombie)",
			"   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type a mob does.",
			"      t is the type of damage. Requires the 'ddd_' prefix. can use s, p, b, instead for slashing, piercing or bludegeoning",
			"      a is the percent of this damage this mob does. if a = 0, it is ignored.",
			"      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero damages (Or just bludgeoning if none are non zero)",
			"   All percents MUST add to 1",
			"Mobs that aren't listed here will inflict full bludgeoning damage.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] mobBaseDmg = DefaultValues.MOB_BASE_DAMAGE;

	@Name("Weapon Base Damage")
	@Comment({
			"Modify the base damage type distribution of weapons/items.",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the item (e.g. minecraft:diamond_sword)",
			"   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type an item does.",
			"      t is the type of damage. Requires the 'ddd_' prefix. can use s, p, b, instead for slashing, piercing or bludegeoning",
			"      a is the percent of this damage this mob does. if a = 0, it is ignored.",
			"      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero damages (Or just bludgeoning if none are non zero)",
			"   All percents MUST add to 1",
			"Items that aren't listed here will inflict 100% bludgeoning damage, no matter the item.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] itemBaseDamage = DefaultValues.WEAPON_BASE_DAMAGE;

	@Name("Projectile Damage Type")
	@Comment({
			"Modify the damage type of projectiles",
			"Each entry is of the form id;[(t,a)];items where:",
			"   id is the namespaced id of the projectile ENTITY (e.g. minecraft:arrow)",
			"   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type a projectile does.",
			"      t is the type of damage. Requires the 'ddd_' prefix. can use s, p, b, instead for slashing, piercing or bludegeoning",
			"      a is the percent of this damage this mob does. if a = 0, it is ignored.",
			"      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero damages (Or just bludgeoning if none are non zero)",
			"   All percents MUST add to 1",
			"   items are the item ids associated with this projectile, separated by a comma (For example, arrow entities are associated with the item ids minecraft:arrow and minecraft:tipped_arrow). This is used for tooltips.",
			"      If the projectile has no item form, omit this part, including the semicolon.",
			"Projectiles that aren't listed here will inflict piercing damage, no matter the projectile.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] projectileDamageTypes = DefaultValues.PROJECTILE_BASE_DAMAGE;

	@Name("Extra Damage Classification")
	@Comment("Enable/disable damage distributions for certain vanilla damage sources.")
	public ExtraDamageDistsCategory extraDamage = new ExtraDamageDistsCategory();
}
