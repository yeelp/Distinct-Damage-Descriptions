package yeelp.distinctdamagedescriptions;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.HwylaTooltipMaker;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipMaker;

@Config(modid = ModConsts.MODID)
public class ModConfig {
	@Name("Damage")
	@Comment("Alter the base damage of weapons and mobs")
	public static final DamageCategory dmg = new DamageCategory();

	@Name("Resistance")
	@Comment("Alter the base resistances of armor and mobs")
	public static final ResistanceCategory resist = new ResistanceCategory();

	@Name("Enchantments")
	@Comment("Configure enchantments")
	public static final EnchantCategory enchants = new EnchantCategory();

	@Name("Debug Mode")
	@Comment("Enable debug mode. Console will be filled with debug messages. The frequency/content of the messages may vary across versions. Only enable if troubleshooting or developing.")
	public static boolean showDotsOn = false;

	@Name("Client")
	@Comment("Alter client side settings")
	public static final ClientCategory client = new ClientCategory();

	@Name("Compatibility (NYI)")
	@Comment("Tweak DDD's behaviour with other mods")
	public static final CompatCategory compat = new CompatCategory();

	@Name("Suppress Warnings")
	@Comment("If warning messages from Distinct Damage Descriptions are clogging the log, you can disable them here. This may be indicative of a real issue though, so make sure there's no real issue first!")
	public static boolean suppressWarnings = false;

	@Name("Suppress Registration Info")
	@Comment("If registration messages from Distinct Damage Descriptions are clogging the log, you can disable them here.")
	public static boolean suppressRegistrationInfo = false;

	@Name("Generate Configs")
	@Comment("If set to true, and Distinct Damage Description will try to generate appropriate config values for weapons, mobs, armor and projectiles.")
	public static boolean generateStats = false;

	@Name("Generate JSON")
	@Comment("If set, DistinctDamageDescriptions will generate example JSON files for custom damage types and creature types.")
	public static boolean generateJSON = true;

	public static class CompatCategory {
		@Name("Tool Material Bias")
		@Comment("Tools that use materials, like TiC tools, will use these distributions to influence the tool distribution.")
		public String[] toolMatBias = {};

		public String[] armorMatBias = {};
	}

	public static class EnchantCategory {
		@Name("Enable Brute Force")
		@Comment("If false, the Brute Force enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
		@RequiresMcRestart
		public boolean enableBruteForce = true;

		@Name("Enable Sly Strike")
		@Comment("If false, the Sly Strike enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
		@RequiresMcRestart
		public boolean enableSlyStrike = true;
	}

	public static class DamageCategory {
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
		public String[] mobBaseDmg = {
				"minecraft:blaze;[(b, 0.5), (ddd_fire, 0.5)]",
				"minecraft:cave_spider;[(p, 0.75), (ddd_poison, 0.25)]",
				"minecraft:magma_cube;[(b, 0.8), (ddd_fire, 0.2))]",
				"minecraft:ocelot;[(p, 0.3), (s, 0.7)]",
				"minecraft:polar_bear;[(s, 0.25), (b, 0.75)]",
				"minecraft:slime;[(b, 0.4), (ddd_acid, 0.6)]",
				"minecraft:spider;[(p, 1)]",
				"minecraft:wolf;[(p, 1)]",
				"minecraft:zombie;[(b, 0.75), (ddd_necrotic, 0.25)]",
				"minecraft:zombie_villager;[(b, 0.75), (ddd_necrotic, 0.25)]",
				"minecraft:zombie_pigman;[(b, 0.75), (ddd_necrotic, 0.25)]",
				"minecraft:wither_skeleton;[(b, 0.5), (ddd_necrotic, 0.5)]",
				"minecraft:wither;[(b, 0.1), (ddd_necrotic, 0.9)]",
				"minecraft:blaze;[(b, 0.25), (ddd_fire, 0.75)]",
				"minecraft:vex;[(ddd_psychic, 1)]",
				"minecraft:silverfish;[(p, 1)]",
				"minecraft:endermite;[(p, 1)]",
				"tconstruct:blueslime;[(b, 0.4), (ddd_acid, 0.6)]"};

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
		public String[] itemBaseDamage = {
				"minecraft:blaze_rod;[(ddd_fire, 1)]",
				"minecraft:nether_star;[(ddd_necrotic, 0.5), (ddd_force, 0.5)]",
				"minecraft:torch;[(ddd_fire, 1)]",
				"minecraft:redstone_torch;[(b, 0.9), (ddd_lightning, 0.1)]",
				"minecraft:arrow;[(p, 1)]",
				"minecraft:tipped_arrow;[(p, 1)]",
				"minecraft:spectral_arrow;[(p, 1)]",
				"minecraft:wooden_sword;[(s, 0.5), (b, 0.5)]",
				"minecraft:wooden_axe;[(s, 0.3), (b, 0.7)]",
				"minecraft:wooden_pickaxe;[(p, 0.5), (b, 0.5)]",
				"minecraft:wooden_shovel;[(b, 1)]",
				"minecraft:wooden_hoe;[(p, 0.5), (b, 0.5)]",
				"minecraft:stone_sword;[(b, 1)]",
				"minecraft:stone_axe;[(b, 1)]",
				"minecraft:stone_pickaxe;[(p, 0.2), (b, 0.8)]",
				"minecraft:stone_shovel;[(b, 1)]",
				"minecraft:stone_hoe;[(p, 0.2), (b, 0.8)]",
				"minecraft:iron_sword;[(s, 0.8), (p, 0.2)]",
				"minecraft:iron_axe;[(s, 0.6), (b, 0.4)]",
				"minecraft:iron_pickaxe;[(p, 0.9), (b, 0.1)]",
				"minecraft:iron_shovel;[(p, 0.1), (b, 0.9)]",
				"minecraft:iron_hoe;[(p, 1)]",
				"minecraft:golden_sword;[(s, 1)]",
				"minecraft:golden_axe;[(s, 1)]",
				"minecraft:golden_pickaxe;[(p, 1)]",
				"minecraft:golden_shovel;[(b, 1)]",
				"minecraft:golden_hoe;[(p, 1)]",
				"minecraft:diamond_sword;[(s, 1)]",
				"minecraft:diamond_axe;[(s, 0.8), (b, 0.2)]",
				"minecraft:diamond_pickaxe;[(p, 1)]",
				"minecraft:diamond_shovel;[(b, 1)]",
				"minecraft:diamond_hoe;[(p, 1)]",
				"tconstruct:stone_torch;[(ddd_fire, 1)]",
				"tconstruct:slimesling;[(b, 0.9), (ddd_acid, 0.1)]"};

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
		public String[] projectileDamageTypes = {
				"minecraft:arrow;[(p, 1)];minecraft:arrow,minecraft:tipped_arrow",
				"minecraft:spectral_arrow;[(p, 1)];minecraft:spectral_arrow",
				"minecraft:llama_spit;[(b, 1)]"};

		@Name("Use Custom Damage Types")
		@Comment("If true, Distinct Damage Descriptions will load and enable custom damage types from JSON found in config/distinctdamagedescriptions/damageTypes")
		@RequiresMcRestart
		public boolean useCustomDamageTypes = false;

		@Name("Use Custom Death Messages")
		@Comment({
				"If custom damage types are enabled and this is turned on, the JSON specified death messages will be used.",
				"This config option sets the showDeathMessages gamerule to false when enabled when worlds are loaded.",
				"The gamerule will be set to true when disabled of course. However, if the mod is uninstalled, this gamerule will have to be manually set back."})
		@RequiresWorldRestart
		public boolean useCustomDeathMessages = false;

		@Name("Cancel Events On Immunity")
		@Comment("If true, Distinct Damage Descriptions will cancel hurt events if immunity blocks all incoming damage. This will prevent other mods from doing anything during that event and keep damage at zero.")
		public boolean cancelLivingHurtEventOnImmunity = false;

		@Name("Extra Damage Classification")
		@Comment("Enable/disable damage distributions for certain vanilla damage sources.")
		public ExtraDamageDistsCategory extraDamage = new ExtraDamageDistsCategory();

		public static class ExtraDamageDistsCategory {
			@Name("Explosion DamageDistribution")
			@Comment("The damage distribution to use for explosion damage; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
			public String explosionDist = "[(b, 1)]";

			@Name("Cactus Distribution")
			@Comment("Enable/disable the cactus damage distribution. Cacti inflict piercing damage with this enabled. If disabled, vanilla handles it normally.")
			public boolean enableCactusDamage = true;

			@Name("Explosion Distribution")
			@Comment("Enable/disable the explosion damage distribution. This applies to player made explosions (TNT, etc.), non-player made explosions (Creepers, etc.) and Firework Rockets (while using Elytra), at least in vanilla. Explosions inflict bludgeoning damage if enabled. If disabled, vanilla handles it normally.")
			public boolean enableExplosionDamage = true;

			@Name("Falling Distribution")
			@Comment("Enable/disable the fall damage distribution. This applies to any fall damage (falling, ender pearls). Falling inflicts bludgeoning damage with this enabled, can only be reduced by boots, and WILL damage boots. If disabled, vanilla handles it normally.")
			public boolean enableFallDamage = true;

			@Name("Fly Into Wall Distribution")
			@Comment("Enable/disable the fly-into-wall damage distribution. This occurs when a player flys into a wall with Elytra. Flying into a wall inflicts bludgeoning damage with this enabled. If disabled, vanilla handles it normally.")
			public boolean enableFlyIntoWallDamage = true;

			@Name("Anvil Distribution")
			@Comment("Enable/disable the anvil damage distribution. Anvils inflict bludgeoning damage with this enabled, however this damage can only be reduced by helmets, and is capped at a 25% damage reduction. If disabled, vanilla handles it normally.")
			public boolean enableAnvilDamage = true;

			@Name("Falling Block Distribution")
			@Comment("Enable/disable the falling block damage distribution. Falling Blocks inflict bludgeoning damage with this enabled, however this damage can only be reduced by helmets. Vanilla's falling blocks inflict no damage without other mods or unless they are summoned with altered NBT data. If disabled, vanilla handles it normally.")
			public boolean enableFallingBlockDamage = true;

			@Name("Lightning Distribution")
			@Comment("Enable/disable the lightning damage distribution. Lightning bolts will inflict lightning damage with this enabled.")
			public boolean enableLightningDamage = true;

			@Name("Wither Distribution")
			@Comment("Enable/disable the wither damage distribution. Withering will inflict necrotic damage with this enabled. It still can't be blocked by armor, but it can be blocked by resistances")
			public boolean enableWitherDamage = true;

			@Name("Potion Distribution")
			@Comment("Enable/disable potion damage distribution. Thrown potions of healing will inflict radiant damage against undead, and thrown potions of harming will inflict necrotic damage against non undead when enabled. It still can't be blocked by armor, but it can be blocked by resistances. Also applies to Area of Effect clouds")
			public boolean enablePotionDamage = true;

			@Name("Poison Effect Distribution")
			@Comment("Enable/diable poison effect distribution. Suffering from the Poison effect will inflict poison damage when enabled. It still can't be blocked by armor, but it can be blocked by resistances")
			public boolean enablePoisonEffectDamage = true;

			@Name("Thorns Distribution")
			@Comment("Enable/disable thorns distribution. The Thorns enchantment will inflict force damage when enabled.")
			public boolean enableThornsDamage = true;

			@Name("Evoker Fangs Distribution")
			@Comment("Enable/disable evoker fangs distribution. Evoker fanges will inflict force damage when enabled.")
			public boolean enableEvokerFangsDamage = true;

			@Name("Guardian Beam Distribution")
			@Comment("Enable/disable guardian beam distribution. Guardian beams (not the spikes) will inflict force damage when enabled.")
			public boolean enableGuardianDamage = true;

			@Name("Fire Distribution")
			@Comment("Enable/disable the fire damage distribution. All fire sources (fire, lava, magma etc.) will count as fire damage when enabled.")
			public boolean enableFireDamage = true;

			@Name("Daylight Burning Distribution")
			@Comment("Enable/disable daylight burning distribution. Undead burning in the day exposed to the sky will receive radiant damage when enabled.")
			public boolean enableDaylightBurningDamage = true;

			@Name("Guardian Thorns Damage")
			@Comment("Enable/disable guardian thorn distribution. Guardian's thorn attack will inflict piercing damage when enabled.")
			public boolean enableGuardianSpikesDamage = true;

			@Name("Parrot Poison Damage")
			@Comment("Enable/disable parrot cookie distribution. Parrots take poison damage when fed cookies")
			public boolean enableParrotPoisonDamage = true;
		}
	}

	public static class ResistanceCategory {
		@Name("Adapt to Custom Damage")
		@Comment("If true, mobs with adaptive resistance will adapt to custom damage as well, if custom damage is enabled. If false, they will only adapt to the built in slashing, piercing, bludgeoning")
		public boolean adaptToCustom = false;

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
		public String[] mobBaseResist = {
				"minecraft:bat;[(b, -0.5), (ddd_poison, 0.2)];[];0;0",
				"minecraft:blaze;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_fire, 1), (ddd_poison, 0.5), (ddd_cold, -1)];[ddd_fire];0;0",
				"minecraft:cave_spider;[(p, 0.25), (b, -0.25)];[ddd_poison];0.3;0.25",
				"minecraft:chicken;[(b, -0.25)];[];0;0",
				"minecraft:cow;[];[];0;0",
				"minecraft:creeper;[(ddd_lightning, 0.3)];[ddd_thunder];0;0",
				"minecraft:donkey;[];[];0;0",
				"minecraft:elder_guardian;[(s, 0.25), (p, 0.25), (b, 0.25), (ddd_lightning, -0.15)];[];1.0;0.75",
				"minecraft:ender_dragon;[][ddd_psychic];0;0",
				"minecraft:enderman;[(ddd_psychic, 0.5)];[];0.7;0.5",
				"minecraft:endermite;[(ddd_psychic, 0.7)];[];0.9;0.75",
				"minecraft:evocation_illager;[(ddd_psychic, 0.3), (ddd_force, 0.4)];[];0;0",
				"minecraft:ghast;[(b, 0.5)];[ddd_psychic];0;0",
				"minecraft:guardian;[(ddd_lightning, -0.2)];[];0.25;0.25",
				"minecraft:horse;[];[];0;0",
				"minecraft:husk;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_poison, 0.5), (ddd_radiant, -0.25)];[];0;0",
				"minecraft:llama;[];[];0;0",
				"minecraft:magma_cube;[(s, 0.25), (p, 0.25), (b, 0.25), (ddd_fire, 1), (ddd_cold, -0.5), (ddd_poison, 0.2)];[b, ddd_acid, ddd_psychic, ddd_thunder, ddd_fire];0;0",
				"minecraft:mooshroom;[];[];0;0",
				"minecraft:mule;[];[];0;0",
				"minecraft:ocelot;[];[];0;0",
				"minecraft:parrot;[(b, -0.5)];[];0;0",
				"minecraft:pig;[];[];0;0",
				"minecraft:polar_bear;[(b, 0.25)];[ddd_cold];0;0",
				"minecraft:rabbit;[];[];0;0",
				"minecraft:sheep;[];[];0;0",
				"minecraft:shulker;[(s, 0.5), (p, -0.5), (b, 0.75), (ddd_psychic, 0.6), (ddd_thunder, 0.8)];[ddd_force];0.25;0.5",
				"minecraft:silverfish;[(b, -0.25), (ddd_fire, -0.1), (ddd_poison, 0.5)];[];0.95;1.0",
				"minecraft:skeleton;[(s, 0.25), (ddd_necrotic, 0.75), (ddd_radiant, -1)];[ddd_poison];0;0",
				"minecraft:skeleton_horse;[(s, 0.25), (ddd_necrotic, 0.75), (ddd_radiant, -1)];[ddd_poison];0;0",
				"minecraft:slime;[(s, -0.25), (b, 0.25), (ddd_thunder, -0.5), (ddd_acid, 1)];[b, ddd_poison, ddd_psychic, ddd_thunder, ddd_acid];0;0",
				"minecraft:spider;[(p, 0.25), (b, -0.25)];[ddd_poison];0.3;0.25",
				"minecraft:squid;[];[];0;0",
				"minecraft:stray;[(s, 0.25), (ddd_necrotic, 0.75), (ddd_radiant, -0.5)];[ddd_poison, ddd_cold];0;0",
				"minecraft:vex;[];[ddd_psychic, ddd_force];0;0",
				"minecraft:villager;[];[];0;0",
				"minecraft:vindication_illager;[];[];0;0",
				"minecraft:witch;[(ddd_acid, 0.2), (ddd_poison, 0.3)];[];0.1;0.25",
				"minecraft:wither_skeleton;[(s, 0.25), (ddd_necrotic, 1), (ddd_radiant, -1)];[ddd_poison, ddd_fire, ddd_necrotic];0;0",
				"minecraft:wolf;[];[];0;0",
				"minecraft:zombie;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_poison, 0.8), (ddd_radiant, -0.5)];[];0;0",
				"minecraft:zombie_horse;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_poison, 0.8), (ddd_radiant, -0.5)];[];0;0",
				"minecraft:zombie_pigman;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_poison, 0.8), (ddd_radiant, -0.5)];[ddd_fire];0;0",
				"minecraft:zombie_villager;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_poison, 0.8), (ddd_radiant, -0.5)];[];0;0",
				"minecraft:iron_golem;[(s, 0.5), (p, 0.75), (b, 1.0), (ddd_acid, -0.25), (ddd_force, 0.5), (ddd_thunder, 0.65)];[ddd_poison, ddd_psychic];0;0",
				"minecraft:wither;[(s, 0.25), (ddd_necrotic, 1.25), (ddd_poison, 0.8), (ddd_radiant, -0.25)];[ddd_necrotic];0;0",
				"tconstruct:blueslime;[(s, -0.25), (b, 0.25), (ddd_thunder, -0.5), (ddd_acid, 1)];[b, ddd_poison, ddd_psychic, ddd_thunder, ddd_acid];0;0",
				"thermalfoundation:blizz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_cold, 1), (ddd_poison, 0.5), (ddd_fire, -1)];[ddd_cold];0;0",
				"thermalfoundation:basalz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_force, 1), (ddd_poison, 0.5), (ddd_thunder, -1)];[ddd_force];0;0",
				"thermalfoundation:blitz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_thunder, 1), (ddd_poison, 0.5), (ddd_force, -1)];[ddd_thunder];0;0"};

		@Name("Player Base Resistance")
		@Comment({"Set the base resistance values for the player",
				  "This is likely only applicable to new worlds! Old worlds may not reflect this change!",
				  "The format for this is the same as Mob Base Resistance/Weakness, minus the leading id part. All properties are usuable (immunities, adaptability), except adaptability chance, which will be set to 1 if adaptability amount is set to a non zero value"})
		@RequiresMcRestart
		public String playerResists = "[];[];0;0";
		
		@Name("Shield Effectiveness")
		@Comment({
				"Modify how shields block damage.",
				"Each entry is of the form id;s;p;b;[(t,a)] where:",
				"   id is the namespaced id of the item (e.g. minecraft:shield)",
				"   [(t,a)] is a list of comma separated tuples of damage types this shield blocks.",
				"      t is the type this shield blocks. Requires the 'ddd_' prefix.",
				"      a is the effectiveness the shield has against that damage type.",
				"Shields not listed here will act as normal shields (will block all damage they can interact with).",
				"Shield effectiveness determines how much physical damage a shield can block. A shield with 0.3 slashing effectiveness can only block 30% of incoming slashing damage. The remaining 70% goes through the shield and damages the player, following regular damage calculation.",
				"Blocking damage will still knock the attacker back, but the knockback strength is a percentage of the original vanilla knockback; that percentage comes from the amount of damage actually reduced (a shield that only blocks 33% of the incoming damage will knock the attacker back by about 33% of the vanilla amount).",
				"Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] shieldResist = {
				"minecraft:shield;[(s, 0.8), (p, 0.5), (b, 0.2)]"};

		@Name("Armor Resistance")
		@Comment({
				"Modify the base resistance effectiveness of armor",
				"Each entry is of the form id;s;p;b;[(t,a)] where:",
				"   id is the namespaced id of the item (e.g. minecraft:diamond_chestplate)",
				"   s is the base slashing effectiveness of this armor.",
				"   p is the base piercing effectiveness of this armor.",
				"   b is the base bludgeoning effectiveness of this armor.",
				"   [(t,a)] is a list of comma separated tuples of custom damage types this armor resists (if enabled).",
				"      t is the damage type this armor resists. Requires the 'ddd_' prefix.",
				"      a is the armor's effectiveness against that damage type.",
				"      You can omit this list if the armor resists no custom damage.",
				"Armors that aren't listed here will have no effectiveness (this doesn't mean the armor does nothing).",
				"Resistances effectiveness determines how armor points are distributed. That is, an value of 0.5 means that armor only uses 50% of its usual armor points when defending against that type",
				"Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] armorResist = {
				"minecraft:leather_helmet;[(s, 0.3), (p, 0.05), (b, 1.0)]",
				"minecraft:leather_chestplate;[(s, 0.3), (p, 0.05), (b, 1.0)]",
				"minecraft:leather_leggings;[(s, 0.3), (p, 0.05), (b, 1.0)]",
				"minecraft:leather_boots;[(s, 0.3), (p, 0.05), (b, 1.0)]",
				"minecraft:chainmail_helmet;[(s, 0.6), (b, 0.8)]",
				"minecraft:chainmail_chestplate;[(s, 0.6), (b, 0.8)]",
				"minecraft:chainmail_leggings;[(s, 0.6), (b, 0.8)]",
				"minecraft:chainmail_boots;[(s, 0.6), (b, 0.8)]",
				"minecraft:iron_helmet;[(s, 1.0), (p, 0.7), (b, 0.3)]",
				"minecraft:iron_chestplate;[(s, 1.0), (p, 0.7), (b, 0.3)]",
				"minecraft:iron_leggings;[(s, 1.0), (p, 0.7), (b, 0.3)]",
				"minecraft:iron_boots;[(s, 1.0), (p, 0.7), (b, 0.3)]",
				"minecraft:golden_helmet;[(s, 1.0), (p, 0.6), (b, 0.25)]",
				"minecraft:golden_chestplate;[(s, 1.0), (p, 0.6), (b, 0.25)]",
				"minecraft:golden_leggings;[(s, 1.0), (p, 0.6), (b, 0.25)]",
				"minecraft:golden_boots;[(s, 1.0), (p, 0.6), (b, 0.25)]",
				"minecraft:diamond_helmet;[(s, 0.15), (p, 1.0), (b, 0.7)]",
				"minecraft:diamond_chestplate;[(s, 0.15), (p, 1.0), (b, 0.7)]",
				"minecraft:diamond_leggings;[(s, 0.15), (p, 1.0), (b, 0.7)]",
				"minecraft:diamond_boots;[(s, 0.15), (p, 1.0), (b, 0.7)]"};

		@Name("Use Creature Types")
		@Comment({
				"If true, DistinctDamageDescriptions will load custom creature types from JSON located in config/distinctdamagedescriptions/creatureTypes.",
				"These JSON files can be used to apply potion/critical hit immunities to large swaths of mobs at once. Also usuable in CraftTweaker."})
		@RequiresMcRestart
		public boolean useCreatureTypes = false;

		@Name("Enable Adaptive Weakness")
		@Comment({"Enable Adaptive Weakness",
			      "Adaptive Weakness kicks in when a mob that is adaptive is hit by type(s) they are weak to.",
			      "Their adaptive amount is set to a percentage of the base amount, that percentage being equal to exp(avg), where: ",
			      "   exp being the exponential function",
			      "   avg is the average of all of the mobs weakness values that were hit (which is negative)"})
		public boolean enableAdaptiveWeakness = false;
	}

	public static class ClientCategory {
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
		@Comment({"If true, Distinct Dasmage Descriptions will show mob damage in spawn egg tooltips when holding <SHIFT>.",
			      "Regular item damage distribution information will not be shown so long as the spawn eggs are not configured to have a particular distribution.",
			      "It is recommended to not set a distribution for spawn eggs if this is set to true, as this will make the tooltip cleaner."})
		public boolean showMobDamage = false;

		@Name("Use Numerical Values When Possible")
		@Comment({
				"If true, Distinct Damage Descriptions will try to show numerical values when possible instead of percent values.",
				"This works for armor and items only. Projectiles and resistances don't have numerical values to use, so the config doesn't apply here."})
		public boolean showNumberValuesWhenPossible = false;

	}

	@Mod.EventBusSubscriber(modid = ModConsts.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been
		 * changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(ModConsts.MODID)) {
				ConfigManager.sync(ModConsts.MODID, Config.Type.INSTANCE);
				DebugLib.updateStatus();
				DDDExplosionDist.update();
				TooltipMaker.updateFormatters();
				HwylaTooltipMaker.updateFormatters();
			}
		}

		@SubscribeEvent
		public static void onWorldLoad(final WorldEvent.Load evt) {
			if(ModConfig.dmg.useCustomDeathMessages) {
				evt.getWorld().getGameRules().setOrCreateGameRule("showDeathMessages", "false");
			}
			else {
				evt.getWorld().getGameRules().setOrCreateGameRule("showDeathMessages", "true");
			}
		}

		@SubscribeEvent
		public static void onWorldSave(@SuppressWarnings("unused") final WorldEvent.Save evt) {
			if(generateStats && ConfigGenerator.hasUpdated()) {
				DistinctDamageDescriptions.debug("Adding new config values...");
				Configuration config = DistinctDamageDescriptions.getConfiguration();
				Property itemDmg = config.get("general.damage", "Weapon Base Damage", dmg.itemBaseDamage);
				Property mobDmg = config.get("general.damage", "Mob Base Damage", dmg.mobBaseDmg);
				Property projDmg = config.get("general.damage", "Projectile Damage Type", dmg.projectileDamageTypes);
				Property armorResist = config.get("general.resistance", "Armor Resistance", resist.armorResist);
				Property mobResists = config.get("general.resistance", "Mob Base Resistance/Weakness", resist.mobBaseResist);
				Property shieldDists = config.get("general.resistance", "Shield Effectiveness", resist.shieldResist);

				itemDmg.set(YLib.merge(dmg.itemBaseDamage, ConfigGenerator.getNewWeaponConfigValues()));
				mobDmg.set(YLib.merge(dmg.mobBaseDmg, ConfigGenerator.getNewMobDamageConfigValues()));
				projDmg.set(YLib.merge(dmg.projectileDamageTypes, ConfigGenerator.getNewProjectileConfigValues()));
				armorResist.set(YLib.merge(resist.armorResist, ConfigGenerator.getNewArmorConfigValues()));
				mobResists.set(YLib.merge(resist.mobBaseResist, ConfigGenerator.getNewMobResistanceConfigValues()));
				shieldDists.set(YLib.merge(resist.shieldResist, ConfigGenerator.getNewShieldConfigValues()));
				config.save();
				ConfigGenerator.markUpdated();
			}
		}
	}
}
