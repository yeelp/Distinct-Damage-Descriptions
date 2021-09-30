package yeelp.distinctdamagedescriptions.config;

import yeelp.distinctdamagedescriptions.ModConfig;

/**
 * Where DDD's default config values are stored. <br>
 * Only String and String[] values are stored here. Primitives are stored in
 * {@link ModConfig}
 * 
 * @author Yeelp
 *
 */
public interface DefaultValues {

	/**********
	 * DAMAGE *
	 **********/

	/**
	 * {@link ModConfig.DamageCategory#mobBaseDmg}
	 */
	final String[] MOB_BASE_DAMAGE = {
			"minecraft:blaze;[(b, 0.5), (ddd_fire, 0.5)]",
			"minecraft:cave_spider;[(p, 0.75), (ddd_poison, 0.25)]",
			"minecraft:magma_cube;[(b, 0.8), (ddd_fire, 0.2)]",
			"minecraft:ocelot;[(p, 0.3), (s, 0.7)]",
			"minecraft:polar_bear;[(s, 0.25), (b, 0.75)]",
			"minecraft:slime;[(b, 0.4), (ddd_acid, 0.6)]",
			"minecraft:spider;[(p, 1)]",
			"minecraft:wolf;[(p, 1)]",
			"minecraft:zombie;[(b, 0.75), (ddd_necrotic, 0.25)]",
			"minecraft:zombie_villager;[(b, 0.75), (ddd_necrotic, 0.25)]",
			"minecraft:zombie_pigman;[(b, 0.75), (ddd_necrotic, 0.25)]",
			"minecraft:wither;[(b, 0.1), (ddd_necrotic, 0.9)]",
			"minecraft:blaze;[(b, 0.25), (ddd_fire, 0.75)]",
			"minecraft:vex;[(ddd_psychic, 1)]",
			"minecraft:silverfish;[(p, 1)]",
			"minecraft:endermite;[(p, 1)]",
			"tconstruct:blueslime;[(b, 0.4), (ddd_acid, 0.6)]",
			"thermalfoundation:blizz;[(b, 0.5), (ddd_cold, 0.5)]",
			"thermalfoundation:basalz;[(b, 0.5), (ddd_force, 0.5)]",
			"thermalfoundation:blitz;[(b, 0.5), (ddd_thunder, 0.5)]"};

	/**
	 * {@link ModConfig.DamageCategory#itemBaseDamage}
	 */
	final String[] WEAPON_BASE_DAMAGE = {
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
			"tconstruct:slimesling;[(b, 0.9), (ddd_acid, 0.1)]",
			"tconstruct:pickaxe;[(p, 1)]",
			"tconstruct:shovel;[(b, 1)]",
			"tconstruct:hatchet;[(s, 0.3), (b, 0.7)]",
			"tconstruct:mattock;[(s, 0.45), (b, 0.45), (p, 0.1)]",
			"tconstruct:kama;[(p, 0.8), (s, 0.2)]",
			"tconstruct:hammer;[(b, 1)]",
			"tconstruct:excavator;[(b, 1)]",
			"tconstruct:lumberaxe;[(s, 0.5), (b, 0.5)]",
			"tconstruct:scythe;[(s, 0.8), (p, 0.2)]",
			"tconstruct:broadsword;[(s, 1)]",
			"tconstruct:longsword;[(s, 1)]",
			"tconstruct:rapier;[(p, 1)]",
			"tconstruct:frypan;[(b, 1)]",
			"tconstruct:battlesign;[(b, 1)]",
			"tconstruct:cleaver;[(s, 0.9), (b, 0.1)]",
			"tconstruct:shortbow;[(b, 1)]",
			"tconstruct:longbow;[(b, 1)]",
			"tconstruct:crossbow;[(b, 1)]",
			"tconstruct:arrow;[(p, 1)]",
			"tconstruct:bolt;[(p, 1)]",
			"tconstruct:shuriken;[(s, 1)]"};

	/**
	 * {@link ModConfig.DamageCategory#projectileDamageTypes}
	 */
	final String[] PROJECTILE_BASE_DAMAGE = {
			"minecraft:arrow;[(p, 1)];minecraft:arrow,minecraft:tipped_arrow",
			"minecraft:spectral_arrow;[(p, 1)];minecraft:spectral_arrow",
			"minecraft:llama_spit;[(b, 1)]"};

	/***************
	 * RESISTANCES *
	 ***************/

	/**
	 * {@link ModConfig.ResistanceCategory#armorResist}
	 */
	final String[] ARMOR_BASE_RESISTS = {
			"minecraft:leather_helmet;[(s, 0.45), (p, 0.15), (b, 1.0)]",
			"minecraft:leather_chestplate;[(s, 0.45), (p, 0.15), (b, 1.0)]",
			"minecraft:leather_leggings;[(s, 0.45), (p, 0.15), (b, 1.0)]",
			"minecraft:leather_boots;[(s, 0.45), (p, 0.15), (b, 1.0)]",
			"minecraft:chainmail_helmet;[(s, 0.75), (b, 1)]",
			"minecraft:chainmail_chestplate;[(s, 0.75), (b, 1)]",
			"minecraft:chainmail_leggings;[(s, 0.75), (b, 1)]",
			"minecraft:chainmail_boots;[(s, 0.75), (b, 1)]",
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

	/**
	 * {@link ModConfig.ResistanceCategory#mobBaseResist}
	 */
	final String[] MOB_BASE_RESISTS = {
			"minecraft:bat;[(b, -0.5), (ddd_poison, 0.2)];[];0;0",
			"minecraft:blaze;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_fire, 1), (ddd_poison, 0.5), (ddd_cold, -1)];[ddd_fire];0;0",
			"minecraft:cave_spider;[(p, 0.25), (b, -0.25)];[ddd_poison];0.3;0.25",
			"minecraft:chicken;[(b, -0.25)];[];0;0",
			"minecraft:cow;[];[];0;0",
			"minecraft:creeper;[(ddd_lightning, 0.3)];[ddd_thunder];0;0",
			"minecraft:donkey;[];[];0;0",
			"minecraft:elder_guardian;[(s, 0.25), (p, 0.25), (b, 0.25), (ddd_lightning, -0.15)];[];1.0;0.75",
			"minecraft:ender_dragon;[];[ddd_psychic];0;0",
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
			"minecraft:silverfish;[(b, -0.25), (p, 0.25), (ddd_fire, -0.1), (ddd_poison, 0.5)];[];0.95;1.0",
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

	/**
	 * {@link ModConfig.ResistanceCategory#shieldResist}
	 */
	final String[] SHIELD_BASE_RESISTS = {
			"minecraft:shield;[(s, 0.8), (p, 0.5), (b, 0.2)]"};

	/******************
	 * SINGLE STRINGS *
	 ******************/
	
	/**
	 * {@link ModConfig.DamageCategory.ExtraDamageDistsCategory#explosionDist}
	 */
	final String EXPLOSION_DIST = "[(b, 1)]";
	
	/**
	 * {@link ModConfig.ResistanceCategory#playerResists}
	 */
	final String PLAYER_BASE_RESISTS = "[];[];0;0";
	
	/***********************
	 * TCONSTRUCT & CONARM *
	 ***********************/
	
	/**
	 * 
	 */
	final String[] MATERIAL_BIAS = {
			"wood;[(b, 1)];0.6",
			"stone;[(b, 1)];1",
			"flint;[(b, 1)];0.3",
			"cactus;[(p, 1)];0.7",
			"bone;[(b, 1)];0.3",
			"obsidian;[(s, 0.8), (b, 0.2)];0.6",
			"prismarine;[(s, 0.6), (p, 0.4)];0.8",
			"endstone;[(b, 0.85), (ddd_psychic, 0.15)];0.9",
			"paper;[(s, 1)];0",
			"sponge;[(b, 1)];2",
			"firewood;[(ddd_fire, 1)];1.8",
			"iron;[(p, 1)];0.1",
			"pigiron;[(s, 1)];0.1",
			"knightslime;[(s, 0.5), (b, 0.5)];0.3",
			"slime;[(ddd_acid, 1)];1",
			"blueslime;[(ddd_acid, 1)];1",
			"magmaslime;[(ddd_fire, 0.5), (ddd_acid, 0.5)];1",
			"netherrack;[(ddd_necrotic, 0.7), (b, 0.3)];0.7",
			"cobalt;[(s, 1)];0.2",
			"ardite;[(ddd_necrotic, 0.3), (s, 0.7)];0.6",
			"manyullyn;[(ddd_force, 0.7), (s, 0.3)];0.7"
	};
	
	/**
	 * {@link ModConfig.CompatCategory.TinkersCategory#toolBias}
	 */
	final String[] TOOL_BIAS = {
			"pickaxe;0.5",
			"shovel;0.75",
			"hatchet;0.35",
			"mattock;0.6",
			"kama;0.5",
			"scythe;0.3",
			"hammer;0.9",
			"excavator;0.8",
			"lumberaxe;0.45",
			"broadsword;0.2",
			"longsword;0.25",
			"rapier;0.5",
			"battlesign;0.6",
			"frypan;1",
			"cleaver;0",
			"arrow;0.8",
			"shortbow;2",
			"longbow;2",
			"bolt;0.8",
			"crossbow;2",
			"shuriken;0"
	};
	
	/**
	 * 
	 */
	final String[] ARMOR_MATERIAL_DISTRIBUTION = {};
	
	/**
	 * {@link ModConfig.CompatCategory.ConarmCategory#armorImmunities}
	 */
	final String[] ARMOR_IMMUNITIES = {
			"endstone;ddd_psychic",
			"netherrack;ddd_necrotic",
			"slime;ddd_acid",
			"blueslime;ddd_acid",
			"knightslime;ddd_slashing",
			"sponge;ddd_bludgeoning, ddd_thunder",
			"manyullyn;ddd_force",
			"obsidian;ddd_piercing",
			"electrum;ddd_lightning"
	};

}
