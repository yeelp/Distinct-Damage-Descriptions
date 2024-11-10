package yeelp.distinctdamagedescriptions.config.damage;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultDamageDistributionBehaviour;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class DamageCategory {
	@Name("Mob Base Damage")
	@Comment({
			"Modify the base damage type distribution of mobs.",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the mob (e.g. minecraft:zombie)",
			"   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type a mob does.",
			"      t is the type of damage. Requires the 'ddd_' prefix. Can use s, p, b, instead for slashing, piercing or bludegeoning",
			"      a is the percent of this damage this mob does. if a = 0, it is ignored.",
			"      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero damages (Or just bludgeoning if none are non zero)",
			"   All percents MUST add to 1",
			"Mobs that aren't listed here will use the default mob damage distribution.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] mobBaseDmg = DefaultValues.MOB_BASE_DAMAGE;

	@Name("Weapon Base Damage")
	@Comment({
			"Modify the base damage type distribution of weapons/items.",
			"Each entry is of the form id;[(t,a)] where:",
			"   id is the namespaced id of the item (e.g. minecraft:diamond_sword)",
			"   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type an item does.",
			"      t is the type of damage. Requires the 'ddd_' prefix. Can use s, p, b, instead for slashing, piercing or bludegeoning",
			"      a is the percent of this damage this mob does. if a = 0, it is ignored.",
			"      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero damages (Or just bludgeoning if none are non zero)",
			"   All percents MUST add to 1",
			"Items that aren't listed here will use the default item damage distribution, no matter the item.",
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
			"Projectiles that aren't listed here will use the default projectile damage distribution, no matter the projectile.",
			"Malformed entries in this list will be ignored."})
	@RequiresMcRestart
	public String[] projectileDamageTypes = DefaultValues.PROJECTILE_BASE_DAMAGE;
	
	@Name("Default Mob Damage Distribution")
	@Comment({"Change the default damage distributions mobs use when they aren't given one in the config.",
		"    BLUDGEONING: Mobs will use a 100% bludgeoning damage distribution.",
		"    PIERCING: Mobs will use a 100% piercing damage distribution.",
		"    NORMAL: Mobs will use a 100% normal damage distribution. Normal damage is an internal type recognized by DDD.",
		"    BYPASS: Mobs will use a 100% unknown damage distribution. Unknown damage is an internal type recognized by DDD."})
	public DefaultDamageDistributionBehaviour defaultMobDamage = DefaultDamageDistributionBehaviour.BLUDGEONING;
	
	@Name("Default Item Damage Distribution")
	@Comment({"Change the default damage distributions items use when they aren't given one in the config.",
		"    BLUDGEONING: Items will use a 100% bludgeoning damage distribution.",
		"    PIERCING: Items will use a 100% piercing damage distribution.",
		"    NORMAL: Items will use a 100% normal damage distribution. Normal damage is an internal type recognized by DDD.",
		"    BYPASS: Items will use a 100% unknown damage distribution. Unknown damage is an internal type recognized by DDD."})
	public DefaultDamageDistributionBehaviour defaultItemDamage = DefaultDamageDistributionBehaviour.BLUDGEONING;
	
	@Name("Default Projectile Damage Distribution")
	@Comment({"Change the default damage distributions projectiles use when they aren't given one in the config.",
		"    BLUDGEONING: Projectiles will use a 100% bludgeoning damage distribution.",
		"    PIERCING: Projectiles will use a 100% piercing damage distribution.",
		"    NORMAL: Projectiles will use a 100% normal damage distribution. Normal damage is an internal type recognized by DDD.",
		"    BYPASS: Projectiles will use a 100% unknown damage distribution. Unknown damage is an internal type recognized by DDD."})
	public DefaultDamageDistributionBehaviour defaultProjectileDamage = DefaultDamageDistributionBehaviour.PIERCING;

	@Name("Extra Damage Classification")
	@Comment("Enable/disable damage distributions for certain vanilla damage sources.")
	public ExtraDamageDistsCategory extraDamage = new ExtraDamageDistsCategory();
}
