package yeelp.distinctdamagedescriptions.config;

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
	String[] MOB_BASE_DAMAGE = {
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
			"minecraft:shulker_bullet;[(ddd_psychic, 1)]",
			"tconstruct:blueslime;[(b, 0.4), (ddd_acid, 0.6)]",
			"thermalfoundation:blizz;[(b, 0.5), (ddd_cold, 0.5)]",
			"thermalfoundation:blizz_bolt;[(ddd_cold, 1)]",
			"thermalfoundation:basalz;[(b, 0.5), (ddd_force, 0.5)]",
			"thermalfoundation:basalz_bolt;[(b, 1)]",
			"thermalfoundation:blitz;[(b, 0.5), (ddd_thunder, 0.5)]",
			"thermalfoundation:blitz_bolt;[(ddd_thunder, 1)]",
			"lycanitesmobs:wendigo;[(b, 1)]",
			"lycanitesmobs:amalgalich;[(ddd_force, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:geist;[(b, 0.4), (ddd_necrotic, 0.6)]",
			"lycanitesmobs:necrovore;[(p, 0.7), (ddd_necrotic, 0.3)]",
			"lycanitesmobs:ghoul;[(b, 0.6), (ddd_necrotic, 0.4)]",
			"lycanitesmobs:cryptkeeper;[(b, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:reaper;[(b, 1)]",
			"lycanitesmobs:gnekk;[(s, 0.3), (p, 0.2), (ddd_poison, 0.5)]",
			"lycanitesmobs:pixen;[(b, 1)]",
			"lycanitesmobs:clink;[(b, 1)]",
			"lycanitesmobs:arix;[(b, 1)]",
			"lycanitesmobs:afrit;[(b, 1)]",
			"lycanitesmobs:jabberwock;[(s, 0.8), (p, 0.2)]",
			"lycanitesmobs:arisaur;[(b, 1)]",
			"lycanitesmobs:wraamon;[(p, 0.5), (ddd_force, 0.4), (ddd_psychic, 0.1)]",
			"lycanitesmobs:manticore;[(p, 0.9), (ddd_poison, 0.1)]",
			"lycanitesmobs:barghest;[(p, 1.0)]",
			"lycanitesmobs:conba;[(b, 1)]",
			"lycanitesmobs:warg;[(p, 1.0)]",
			"lycanitesmobs:kobold;[(s, 0.2), (p, 0.8)]",
			"lycanitesmobs:makaalpha;[(b, 1)]",
			"lycanitesmobs:maka;[(b, 1)]",
			"lycanitesmobs:feradon;[(s, 0.5), (p, 0.5)]",
			"lycanitesmobs:salamander;[(p, 0.4), (ddd_fire, 0.6)]",
			"lycanitesmobs:chupacabra;[(s, 0.4), (p, 0.4), (b, 0.2)]",
			"lycanitesmobs:geken;[(s, 0.3), (p, 0.3), (ddd_poison, 0.4)]",
			"lycanitesmobs:maug;[(s, 0.5), (b, 0.5)]",
			"lycanitesmobs:aspid;[(b, 1)]",
			"lycanitesmobs:dawon;[(s, 0.6), (p, 0.4)]",
			"lycanitesmobs:khalk;[(p, 0.2), (b, 0.8)]",
			"lycanitesmobs:quillbeast;[(b, 1)]",
			"lycanitesmobs:bobeko;[(b, 1)]",
			"lycanitesmobs:yeti;[(b, 1)]",
			"lycanitesmobs:yale;[(b, 1)]",
			"lycanitesmobs:epion;[(b, 1)]",
			"lycanitesmobs:balayang;[(p, 1)]",
			"lycanitesmobs:uvaraptor;[(p, 1.0)]",
			"lycanitesmobs:roc;[(p, 1.0)]",
			"lycanitesmobs:raiko;[(p, 1.0)]",
			"lycanitesmobs:ventoraptor;[(s, 0.25), (p, 0.75)]",
			"lycanitesmobs:lobber;[(b, 0.5), (ddd_fire, 0.5)]",
			"lycanitesmobs:wildkin;[(s, 0.5), (b, 0.5)]",
			"lycanitesmobs:troll;[(b, 1.0)]",
			"lycanitesmobs:ettin;[(b, 1.0)]",
			"lycanitesmobs:lacedon;[(b, 1.0)]",
			"lycanitesmobs:roa;[(p, 1.0)]",
			"lycanitesmobs:silex;[(b, 1)]",
			"lycanitesmobs:abtu;[(p, 1.0)]",
			"lycanitesmobs:strider;[(p, 1.0)]",
			"lycanitesmobs:skylus;[(p, 1.0)]",
			"lycanitesmobs:thresher;[(p, 0.5), (b, 0.5)]",
			"lycanitesmobs:aglebemu;[(b, 0.2), (ddd_poison, 0.8)]",
			"lycanitesmobs:cephignis;[(b, 1)]",
			"lycanitesmobs:abaia;[(ddd_lightning, 1.0)]",
			"lycanitesmobs:ika;[(b, 1)]",
			"lycanitesmobs:dweller;[(s, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:ioray;[(b, 1)]",
			"lycanitesmobs:quetzodracl;[(s, 0.5), (p, 0.5)]",
			"lycanitesmobs:cockatrice;[(s, 0.5), (p, 0.5)]",
			"lycanitesmobs:morock;[(p, 1.0)]",
			"lycanitesmobs:ignibus;[(b, 1)]",
			"lycanitesmobs:zoataur;[(b, 1.0)]",
			"lycanitesmobs:remobra;[(p, 0.4), (ddd_poison, 0.6)]",
			"lycanitesmobs:spriggan;[(b, 1)]",
			"lycanitesmobs:tremor;[(b, 0.5), (ddd_thunder, 0.5)]",
			"lycanitesmobs:vapula;[(b, 1.0)]",
			"lycanitesmobs:geonach;[(p, 0.5), (b, 0.5)]",
			"lycanitesmobs:cinder;[(b, 1)]",
			"lycanitesmobs:reiver;[(b, 1)]",
			"lycanitesmobs:jengu;[(b, 1)]",
			"lycanitesmobs:spectre;[(ddd_force, 1.0)]",
			"lycanitesmobs:banshee;[(ddd_psychic, 1.0)]",
			"lycanitesmobs:grue;[(s, 0.5), (ddd_necrotic, 0.25), (ddd_psychic, 0.25)]",
			"lycanitesmobs:aegis;[(s, 0.5), (ddd_force, 0.5)]",
			"lycanitesmobs:zephyr;[(ddd_lightning, 1.0)]",
			"lycanitesmobs:djinn;[(b, 1)]",
			"lycanitesmobs:eechetik;[(ddd_poison, 1.0)]",
			"lycanitesmobs:wraith;[(ddd_necrotic, 1.0)]",
			"lycanitesmobs:sylph;[(b, 1)]",
			"lycanitesmobs:volcan;[(ddd_fire, 1.0)]",
			"lycanitesmobs:wisp;[(b, 1)]",
			"lycanitesmobs:argus;[(b, 1)]",
			"lycanitesmobs:nymph;[(b, 1)]",
			"lycanitesmobs:xaphan;[(b, 1)]",
			"lycanitesmobs:herma;[(p, 1.0)]",
			"lycanitesmobs:sutiramu;[(p, 0.6), (ddd_poison, 0.4)]",
			"lycanitesmobs:vespidqueen;[(p, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:vespid;[(p, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:joustalpha;[(p, 1.0)]",
			"lycanitesmobs:joust;[(p, 1.0)]",
			"lycanitesmobs:tarantula;[(p, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:calpod;[(p, 1.0)]",
			"lycanitesmobs:erepede;[(b, 1)]",
			"lycanitesmobs:concapedesegment;[(p, 1.0)]",
			"lycanitesmobs:concapede;[(p, 1.0)]",
			"lycanitesmobs:frostweaver;[(p, 0.5), (ddd_cold, 0.5)]",
			"lycanitesmobs:eyewig;[(p, 0.5), (ddd_acid, 0.25), (ddd_poison, 0.25)]",
			"lycanitesmobs:gorgomite;[(p, 1.0)]",
			"lycanitesmobs:darkling;[(p, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:lurker;[(p, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:triffid;[(s, 0.25), (p, 0.25), (ddd_poison, 0.5)]",
			"lycanitesmobs:treant;[(b, 1.0)]",
			"lycanitesmobs:ent;[(b, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:tpumpkyn;[(b, 1)]",
			"lycanitesmobs:shambler;[(s, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:beholder;[(b, 1)]",
			"lycanitesmobs:grell;[(b, 1)]",
			"lycanitesmobs:grigori;[(p, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:vorach;[(p, 1)]",
			"lycanitesmobs:trite;[(p, 0.5), (ddd_necrotic, 0.5)]",
			"lycanitesmobs:shade;[(s, 0.8), (ddd_psychic, 0.2)]",
			"lycanitesmobs:asmodeus;[(b, 1)]",
			"lycanitesmobs:astaroth;[(b, 1)]",
			"lycanitesmobs:krake;[(s, 0.25), (p, 0.25), (ddd_thunder, 0.5)]",
			"lycanitesmobs:crusk;[(p, 1.0)]",
			"lycanitesmobs:serpix;[(p, 0.8), (ddd_cold, 0.2)]",
			"lycanitesmobs:gorger;[(p, 0.8), (ddd_fire, 0.2)]",
			"lycanitesmobs:behemoth;[(b, 1)]",
			"lycanitesmobs:belph;[(b, 1)]",
			"lycanitesmobs:pinky;[(s, 0.5), (p, 0.5)]",
			"lycanitesmobs:archvile;[(b, 1)]",
			"lycanitesmobs:cacodemon;[(b, 1)]",
			"lycanitesmobs:rahovart;[(ddd_fire, 0.5), (ddd_necrotic, 0.5)]"};

	/**
	 * {@link ModConfig.DamageCategory#itemBaseDamage}
	 */
	String[] WEAPON_BASE_DAMAGE = {
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
			"tconstruct:shuriken;[(s, 1)]",
			"lycanitesmobs:grueclaw;[(s, 0.5), (ddd_necrotic, 0.25), (ddd_psychic, 0.25)]",
			"lycanitesmobs:geonachspear;[(p, 1)]",
			"lycanitesmobs:remobrawing;[(s, 0.5), (ddd_poison, 0.5)]",
			"lycanitesmobs:zephyrblade;[(s, 0.2), (ddd_lightning, 0.8)]",
			"lycanitesmobs:raidrablade;[(s, 0.2), (ddd_lightning, 0.8)]",
			"lycanitesmobs:clinkscythe;[(s, 0.7), (p, 0.3)]",
			"lycanitesmobs:geonachfist;[(b, 1)]",
			"lycanitesmobs:cinderblade;[(ddd_fire, 1)]",
			"lycanitesmobs:vapulacrystal;[(p, 0.8), (b, 0.2)]",
			"lycanitesmobs:reiverhorns;[(p, 0.2), (ddd_cold, 0.8)]",
			"lycanitesmobs:woodenpaxel;[(s, 0.5), (b, 0.5)]",
			"lycanitesmobs:ironpaxel;[(s, 0.8), (p, 0.2)]",
			"lycanitesmobs:sprigganheart;[(b, 1)]",
			"lycanitesmobs:gammasphere;[(ddd_radiant, 1)]",
			"ebwizardry:spectral_sword;[(s, 0.5), (ddd_force, 0.5)]",
			"ebwizardry:spectral_pickaxe;[(p, 0.5), (ddd_force, 0.5)]",
			"ebwizardry:flaming_axe;[(s, 0.5), (ddd_fire, 0.5)]",
			"ebwizardry:frost_axe;[(s, 0.5), (ddd_cold, 0.5)]"};

	/**
	 * {@link ModConfig.DamageCategory#projectileDamageTypes}
	 */
	String[] PROJECTILE_BASE_DAMAGE = {
			"minecraft:arrow;[(p, 1)];minecraft:arrow,minecraft:tipped_arrow",
			"minecraft:spectral_arrow;[(p, 1)];minecraft:spectral_arrow",
			"minecraft:llama_spit;[(b, 1)]",
			"lycanitesmobs:whirlwind;[(ddd_cold, 0.3), (ddd_thunder, 0.7)];lycanitesmobs:whirlwindcharge",
			"lycanitesmobs:magma;[(ddd_fire, 0.6), (b, 0.4)];lycanitesmobs:magmacharge,lycanitesmobs:magmascepter",
			"lycanitesmobs:frostbolt;[(ddd_cold, 1)];lycanitesmobs:frostboltcharge,lycanitesmobs:frostboltscepter",
			"lycanitesmobs:spectralbolt;[(ddd_psychic, 0.5), (ddd_force, 0.5)];lycanitesmobs:spectralboltcharge,lycanitesmobs:spectralboltscepter",
			"lycanitesmobs:venomshot;[(ddd_poison, 1)];lycanitesmobs:venomshotcharge",
			"lycanitesmobs:boulderblast;[(b, 1)];lycanitesmobs:boulderblastcharge,lycanitesmobs:boulderblastscepter",
			"lycanitesmobs:icefireball;[(ddd_cold, 1)];lycanitesmobs:icefireballcharge,lycanitesmobs:icefirescepter",
			"lycanitesmobs:hellfireball;[(ddd_fire, 0.5), (ddd_necrotic, 0.5)];lycanitesmobs:hellfireballcharge,lycanitesmobs:hellfirescepter",
			"lycanitesmobs:quill;[(p, 1)];lycanitesmobs:quillcharge,lycanitesmobs:quillscepter",
			"lycanitesmobs:scorchfireball;[(ddd_fire, 0.5), (ddd_acid, 0.5)];lycanitesmobs:scorchfireballcharge,lycanitesmobs:scorchfirescepter",
			"lycanitesmobs:throwingscythe;[(p, 1)];lycanitesmobs:throwingscythecharge",
			"lycanitesmobs:acidsplash;[(ddd_acid, 1)];lycanitesmobs:acidsplashcharge",
			"lycanitesmobs:lobdarklings;[(b, 1)];lycanitesmobs:lobdarklingscharge",
			"lycanitesmobs:faebolt;[(ddd_necrotic, 0.5), (ddd_radiant, 0.5)];lycanitesmobs:faeboltcharge",
			"lycanitesmobs:ember;[(ddd_fire, 1)];lycanitesmobs:embercharge",
			"lycanitesmobs:poop;[(b, 0.3), (ddd_poison, 0.7)];lycanitesmobs:poopcharge,lycanitesmobs:poopscepter",
			"lycanitesmobs:lifedrain;[(ddd_necrotic, 1)];lycanitesmobs:lifedraincharge,lycanitesmobs:lifedrainscepter",
			"lycanitesmobs:aquapulse;[(b, 1)];lycanitesmobs:aquapulsecharge,lycanitesmobs:aquapulsescepter",
			"lycanitesmobs:arcanelaserstorm;[(b, 0.25), (ddd_lightning, 0.25), (ddd_thunder, 0.25), (ddd_force, 0.25)];lycanitesmobs:arcanelaserstormcharge,lycanitesmobs:arcanelaserstormscepter",
			"lycanitesmobs:aetherwave;[(ddd_radiant, 1)];lycanitesmobs:aetherwavecharge",
			"lycanitesmobs:lightball;[(ddd_radiant, 1)];lycanitesmobs:lightballcharge",
			"lycanitesmobs:mudshot;[(b, 1)];lycanitesmobs:mudshotcharge,lycanitesmobs:mudshotscepter",
			"lycanitesmobs:crystalshard;[(p, 0.8), (b, 0.2)];lycanitesmobs:crystalshardcharge",
			"lycanitesmobs:tundra;[(ddd_cold, 0.5), (b, 0.5)];lycanitesmobs:tundracharge,lycanitesmobs:tundrascepter",
			"lycanitesmobs:tricksterflare;[(ddd_force, 1)];lycanitesmobs:tricksterflarecharge",
			"lycanitesmobs:demonicblast;[(ddd_lightning, 0.8), (ddd_necrotic, 0.2)];lycanitesmobs:demonicblastcharge,lycanitesmobs:demoniclightningscepter",
			"lycanitesmobs:bloodleech;[(ddd_necrotic, 1)];lycanitesmobs:bloodleechcharge,lycanitesmobs:bloodleechscepter",
			"lycanitesmobs:frostweb;[(ddd_cold, 1)];lycanitesmobs:frostwebcharge,lycanitesmobs:frostwebscepter",
			"lycanitesmobs:chaosorb;[(ddd_force, 1)];lycanitesmobs:chaosorbcharge",
			"lycanitesmobs:poisonray;[(ddd_poison, 0.5), (ddd_necrotic, 0.5)];lycanitesmobs:poisonraycharge,lycanitesmobs:poisonrayscepter",
			"lycanitesmobs:blizzard;[(ddd_cold, 1)];lycanitesmobs:blizzardcharge,lycanitesmobs:blizzardscepter",
			"lycanitesmobs:doomfireball;[(ddd_fire, 0.5), (ddd_force, 0.5)];lycanitesmobs:doomfireballcharge,lycanitesmobs:doomfirescepter",
			"lycanitesmobs:waterjet;[(b, 0.5), (ddd_acid, 0.5)];lycanitesmobs:waterjetcharge,lycanitesmobs:waterjetscepter",
			"lycanitesmobs:devilstar;[(ddd_necrotic, 0.5), (ddd_force, 0.5)];lycanitesmobs:devilstarcharge",
			"lycanitesmobs:acidglob;[(ddd_acid, 0.5), (b, 0.5)];lycanitesmobs:acidglobcharge",
			"lycanitesmobs:primeember;[(ddd_fire, 1)];lycanitesmobs:primeembercharge",
			"lycanitesmobs:smitefireball;[(ddd_radiant, 0.8), (ddd_fire, 0.2)];lycanitesmobs:smitefireballcharge",
			"spartanweaponry:arrow_wood;[(p,1)];spartanweaponry:arrow_wood,spartanweaponry:arrow_wood_tipped",
			"spartanweaponry:arrow_iron;[(p,1)];spartanweaponry:arrow_iron,spartanweaponry:arrow_iron_tipped",
			"spartanweaponry:arrow_diamond;[(p,1)];spartanweaponry:arrow_diamond,spartanweaponry:arrow_diamond_tipped",
			"spartanweaponry:bolt;[(p,1)];spartanweaponry:bolt,spartanweaponry:bolt_tipped,spartanweaponry:bolt_spectral",
			"spartanweaponry:arrow_explosive;[(b,1)];spartanweaponry:arrow_explosive",
			"ebwizardry:conjured_arrow;[(p, 0.5), (ddd_force, 0.5)];ebwizardry:spectral_bow"};

	/***************
	 * RESISTANCES *
	 ***************/

	/**
	 * {@link ModConfig.ResistanceCategory#armorResist}
	 */
	String[] ARMOR_BASE_RESISTS = {
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
			"minecraft:diamond_boots;[(s, 0.15), (p, 1.0), (b, 0.7)]",
			"ebwizardry:spectral_helmet;[(s, 1), (p, 1), (b, 1), (ddd_force, 1)]",
			"ebwizardry:spectral_chestplate;[(s, 1), (p, 1), (b, 1), (ddd_force, 1)]",
			"ebwizardry:spectral_leggings;[(s, 1), (p, 1), (b, 1), (ddd_force, 1)]",
			"ebwizardry:spectral_boots;[(s, 1), (p, 1), (b, 1), (ddd_force, 1)]"};

	/**
	 * {@link ModConfig.ResistanceCategory#mobBaseResist}
	 */
	String[] MOB_BASE_RESISTS = {
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
			"minecraft:villager_golem;[(s, 0.5), (p, 0.75), (b, 1.0), (ddd_acid, -0.25), (ddd_force, 0.5), (ddd_thunder, 0.65), (ddd_fire, -0.25)];[ddd_poison, ddd_psychic, ddd_necrotic];0;0",
			"minecraft:snowman;[(b, 0.3), (ddd_force, 0.5), (ddd_thunder, 0.65), (ddd_fire, -0.5)];[ddd_cold, ddd_poison, ddd_psychic, ddd_necrotic];0;0",
			"minecraft:wither;[(s, 0.25), (ddd_necrotic, 1.25), (ddd_poison, 0.8), (ddd_radiant, -0.25)];[ddd_necrotic];0;0",
			"tconstruct:blueslime;[(s, -0.25), (b, 0.25), (ddd_thunder, -0.5), (ddd_acid, 1)];[b, ddd_poison, ddd_psychic, ddd_thunder, ddd_acid];0;0",
			"thermalfoundation:blizz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_cold, 1), (ddd_poison, 0.5), (ddd_fire, -1)];[ddd_cold];0;0",
			"thermalfoundation:basalz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_force, 1), (ddd_poison, 0.5), (ddd_thunder, -1)];[ddd_force];0;0",
			"thermalfoundation:blitz;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_thunder, 1), (ddd_poison, 0.5), (ddd_force, -1)];[ddd_thunder];0;0",
			"lycanitesmobs:wendigo;[(s, 0.15), (b, 0.15), (ddd_necrotic, 0.6), (ddd_radiant, -0.6)];[ddd_cold, ddd_poison];0;0",
			"lycanitesmobs:amalgalich;[(ddd_cold, 0.4), (ddd_lightning, 0.3), (ddd_radiant, -0.5)];[ddd_slashing, ddd_piercing, ddd_bludgeoning, ddd_poison, ddd_necrotic];0;0",
			"lycanitesmobs:geist;[(b, 0.3), (ddd_acid, 0.35), (ddd_fire, 0.45), (ddd_lightning, 0.4), (ddd_radiant, -0.7), (ddd_thunder, 0.9)];[ddd_necrotic, ddd_cold, ddd_poison];0.35;0.2",
			"lycanitesmobs:necrovore;[(ddd_radiant, -0.5)];[ddd_necrotic, ddd_poison];0;0",
			"lycanitesmobs:ghoul;[(b, 0.25), (ddd_necrotic, 0.75), (ddd_radiant, -0.5)];[ddd_poison];0;0",
			"lycanitesmobs:cryptkeeper;[(ddd_fire, 0.2), (ddd_necrotic, 0.6), (ddd_psychic, 0.1), (ddd_radiant, -0.5), (ddd_thunder, 0.15)];[ddd_poison];0;0",
			"lycanitesmobs:reaper;[(s, 0.4), (p, 0.4), (b, 0.2), (ddd_force, 1.0), (ddd_necrotic, 0.7), (ddd_radiant, -0.8), (ddd_thunder, 0.5)];[ddd_psychic, ddd_necrotic, ddd_poison, ddd_force];0;0",
			"lycanitesmobs:gnekk;[(b, 0.2), (ddd_acid, 0.3)];[ddd_poison];0;0",
			"lycanitesmobs:pixen;[];[ddd_psychic];0.4;0.4",
			"lycanitesmobs:clink;[(s, 0.3), (p, 0.1), (b, 0.5), (ddd_acid, -0.3), (ddd_fire, -0.2), (ddd_lightning, -0.15)];[];0;0",
			"lycanitesmobs:arix;[(s, 0.2), (p, 0.1), (b, 0.3), (ddd_cold, 1.0), (ddd_fire, -1.0)];[ddd_cold];0;0",
			"lycanitesmobs:afrit;[(s, 0.2), (p, 0.1), (b, 0.3), (ddd_cold, -1.0), (ddd_fire, 1.0)];[ddd_fire];0;0",
			"lycanitesmobs:jabberwock;[(s, -0.3), (p, -0.1), (b, 0.2), (ddd_cold, 0.1)];[];0;0",
			"lycanitesmobs:arisaur;[(s, 0.1), (b, 0.5), (ddd_acid, -0.3), (ddd_fire, -0.8), (ddd_necrotic, -0.8), (ddd_poison, -0.5)];[];0;0",
			"lycanitesmobs:wraamon;[(ddd_force, 0.6), (ddd_psychic, 0.4)];[];0.5;0.3",
			"lycanitesmobs:manticore;[(s, 0.4), (b, -0.25), (ddd_poison, 0.1)];[];0;0",
			"lycanitesmobs:barghest;[(b, 0.25)];[];0;0",
			"lycanitesmobs:conba;[(b, 0.1), (ddd_poison, 0.1)];[];0;0",
			"lycanitesmobs:warg;[(s, 0.2), (b, 0.25)];[];0;0",
			"lycanitesmobs:kobold;[(b, -0.2), (ddd_poison, 0.1)];[];0.8;0.3",
			"lycanitesmobs:makaalpha;[(p, 0.1), (b, 0.2), (ddd_cold, 0.1), (ddd_thunder, 0.2)];[];0;0",
			"lycanitesmobs:maka;[(p, 0.1), (b, 0.2), (ddd_cold, 0.1), (ddd_thunder, 0.2)];[];0;0",
			"lycanitesmobs:feradon;[(b, 0.2)];[];0;0",
			"lycanitesmobs:salamander;[(ddd_acid, 0.2), (ddd_cold, -0.8)];[ddd_fire];0;0",
			"lycanitesmobs:chupacabra;[(s, 0.15), (b, 0.2), (ddd_necrotic, 0.2), (ddd_poison, 0.2), (ddd_psychic, -0.25)];[];0;0",
			"lycanitesmobs:geken;[(s, 0.15), (b, 0.3), (ddd_necrotic, -0.2), (ddd_poison, 0.6)];[];0;0",
			"lycanitesmobs:maug;[(b, 0.2)];[ddd_cold];0;0",
			"lycanitesmobs:aspid;[(b, 0.2)];[ddd_poison];0;0",
			"lycanitesmobs:dawon;[(s, 0.3), (p, 0.2), (b, 0.2), (ddd_thunder, -0.25)];[];0;0",
			"lycanitesmobs:khalk;[(s, 0.8), (p, 0.8), (b, 0.8), (ddd_acid, 0.4), (ddd_cold, -0.8), (ddd_fire, 1.0), (ddd_thunder, -0.25)];[ddd_fire];0;0",
			"lycanitesmobs:quillbeast;[(b, 0.2)];[];0;0",
			"lycanitesmobs:yeti;[(b, 0.2), (ddd_fire, -0.8)];[ddd_cold];0;0",
			"lycanitesmobs:bobeko;[(b, 0.2), (ddd_fire, -0.8)];[ddd_cold];0;0",
			"lycanitesmobs:yale;[(s, 0.1), (b, 0.2), (ddd_cold, 0.5), (ddd_fire, 0.5), (ddd_lightning, 0.1)];[];0;0",
			"lycanitesmobs:epion;[(ddd_lightning, -0.25), (ddd_necrotic, 0.5), (ddd_psychic, 0.5), (ddd_radiant, -0.5)];[];0;0",
			"lycanitesmobs:balayang;[(ddd_lightning, -0.25), (ddd_poison, 0.25)];[];0;0",
			"lycanitesmobs:uvaraptor;[(b, -0.25), (ddd_lightning, -0.25)];[];0;0",
			"lycanitesmobs:roc;[(b, -0.25), (ddd_lightning, -0.25)];[];0;0",
			"lycanitesmobs:raiko;[(b, -0.25), (ddd_lightning, -0.25)];[];0;0",
			"lycanitesmobs:ventoraptor;[(b, -0.25), (ddd_lightning, -0.25)];[];0;0",
			"lycanitesmobs:lobber;[(s, 0.5), (b, 0.5), (ddd_cold, -0.5), (ddd_psychic, -0.25)];[ddd_fire];0;0",
			"lycanitesmobs:wildkin;[(s, 0.2), (b, 0.5), (ddd_cold, 0.25), (ddd_fire, 0.25), (ddd_lightning, 0.1), (ddd_psychic, -0.25)];[];0;0",
			"lycanitesmobs:troll;[(s, 0.5), (p, 0.5), (b, 0.5), (ddd_acid, 0.2), (ddd_cold, 0.2), (ddd_fire, 0.2), (ddd_psychic, -0.25), (ddd_thunder, -0.2)];[];1;0.25",
			"lycanitesmobs:ettin;[(s, 0.15), (b, 0.3), (ddd_psychic, -0.25)];[];0;0",
			"lycanitesmobs:lacedon;[(ddd_lightning, -0.25)];[];0.5;0.5",
			"lycanitesmobs:roa;[(ddd_acid, -0.25), (ddd_lightning, -0.5)];[];0;0",
			"lycanitesmobs:silex;[(ddd_acid, -0.25), (ddd_lightning, -1.0)];[];0;0",
			"lycanitesmobs:abtu;[(ddd_acid, -0.25), (ddd_lightning, -0.75)];[];0;0",
			"lycanitesmobs:strider;[(b, 0.2), (ddd_lightning, -0.5)];[];0;0",
			"lycanitesmobs:skylus;[(s, 0.3), (p, -0.25), (b, 0.5), (ddd_acid, -0.15), (ddd_lightning, -0.5), (ddd_thunder, -0.2)];[];0;0",
			"lycanitesmobs:thresher;[(s, 0.2), (b, 0.2), (ddd_lightning, -1.0)];[];0;0",
			"lycanitesmobs:aglebemu;[(ddd_lightning, -0.5)];[];0.25;0.25",
			"lycanitesmobs:cephignis;[(ddd_cold, -0.5), (ddd_lightning, -0.5)];[ddd_fire];1;0.35",
			"lycanitesmobs:abaia;[(ddd_acid, -0.15), (ddd_lightning, 1.0), (ddd_thunder, 0.5)];[ddd_lightning];0;0",
			"lycanitesmobs:ika;[(s, 0.25), (p, -0.25), (b, 0.3), (ddd_acid, -0.15), (ddd_lightning, -0.8), (ddd_thunder, 0.5)];[];0;0",
			"lycanitesmobs:dweller;[(s, 0.2), (ddd_acid, -0.15), (ddd_cold, 0.1), (ddd_fire, 0.1), (ddd_lightning, -0.8), (ddd_poison, 0.5)];[];0;0",
			"lycanitesmobs:ioray;[(ddd_lightning, -1.0)];[];0;0",
			"lycanitesmobs:quetzodracl;[(ddd_acid, 0.2), (ddd_cold, 0.2), (ddd_fire, 0.2), (ddd_lightning, -0.2)];[];0;0",
			"lycanitesmobs:cockatrice;[(ddd_acid, 0.2), (ddd_cold, 0.2), (ddd_fire, 0.2), (ddd_lightning, -0.5)];[];0;0",
			"lycanitesmobs:morock;[(ddd_acid, 0.2), (ddd_cold, 0.2), (ddd_fire, 0.2)];[];0;0",
			"lycanitesmobs:ignibus;[(s, 0.2), (p, 0.2), (b, 0.2), (ddd_acid, 0.2), (ddd_cold, -0.3), (ddd_fire, 1.0), (ddd_necrotic, 0.2)];[ddd_fire];0;0",
			"lycanitesmobs:zoataur;[(s, 0.6), (p, 0.6), (b, 0.6), (ddd_acid, 0.2), (ddd_cold, 0.2), (ddd_fire, 0.2), (ddd_force, 0.3), (ddd_necrotic, 0.2), (ddd_thunder, 0.2)];[];0;0",
			"lycanitesmobs:remobra;[(ddd_acid, 0.5), (ddd_cold, 0.2), (ddd_fire, 0.2), (ddd_lightning, -0.25), (ddd_poison, 1.0)];[];0;0",
			"lycanitesmobs:spriggan;[(ddd_acid, -0.25), (ddd_fire, -0.75), (ddd_necrotic, -0.25), (ddd_poison, -0.5)];[];0;0",
			"lycanitesmobs:tremor;[(ddd_force, 1.0), (ddd_psychic, -0.25), (ddd_thunder, 1.0)];[ddd_thunder];0;0",
			"lycanitesmobs:vapula;[(s, 0.15), (p, 1.0), (b, 0.8), (ddd_acid, 0.2), (ddd_force, 0.25), (ddd_necrotic, 1.0), (ddd_poison, 1.0), (ddd_psychic, 0.5), (ddd_thunder, -1.0)];[ddd_necrotic, ddd_poison];0;0",
			"lycanitesmobs:geonach;[(s, 0.75), (p, -0.25), (b, 1.0), (ddd_acid, -0.25), (ddd_necrotic, 1.0), (ddd_poison, 1.0), (ddd_thunder, -1.0)];[ddd_bludgeoning, ddd_necrotic, ddd_poison];0;0",
			"lycanitesmobs:cinder;[(ddd_cold, -1.0), (ddd_fire, 1.0)];[ddd_fire];0;0",
			"lycanitesmobs:reiver;[(ddd_cold, 1.0), (ddd_fire, -1.0)];[ddd_cold];0;0",
			"lycanitesmobs:jengu;[(ddd_acid, -1.0), (ddd_cold, 0.25), (ddd_fire, 1.0), (ddd_lightning, -1.0), (ddd_poison, -0.5), (ddd_thunder, 0.25)];[ddd_fire];0;0",
			"lycanitesmobs:spectre;[(ddd_force, 1.0), (ddd_psychic, 1.0)];[ddd_psychic];0;0",
			"lycanitesmobs:banshee;[(ddd_force, 1.0), (ddd_necrotic, 0.5), (ddd_poison, 0.5), (ddd_psychic, 1.0), (ddd_thunder, 1.0)];[ddd_force, ddd_psychic];0;0",
			"lycanitesmobs:grue;[(ddd_force, 1.0), (ddd_necrotic, 0.5), (ddd_psychic, 0.25), (ddd_radiant, -1.0)];[ddd_force];0;0",
			"lycanitesmobs:aegis;[(s, 0.25), (b, 0.5), (ddd_force, -0.25), (ddd_necrotic, 0.25), (ddd_poison, 1.0), (ddd_psychic, 1.0), (ddd_thunder, 1.0)];[];0;0",
			"lycanitesmobs:zephyr;[(ddd_lightning, 1.0)];[ddd_lightning, ddd_thunder];0;0",
			"lycanitesmobs:djinn;[(ddd_cold, 0.25), (ddd_fire, 0.25), (ddd_lightning, -1.0), (ddd_thunder, 1.0)];[ddd_thunder];0;0",
			"lycanitesmobs:eechetik;[(ddd_acid, 0.25), (ddd_necrotic, 0.25), (ddd_poison, 1.0), (ddd_psychic, -1.0)];[ddd_poison];0;0",
			"lycanitesmobs:wraith;[(ddd_fire, 1.0), (ddd_force, 0.25), (ddd_necrotic, 1.0), (ddd_poison, 1.0), (ddd_radiant, -1.0), (ddd_thunder, 1.0)];[ddd_fire, ddd_necrotic, ddd_poison, ddd_thunder];0;0",
			"lycanitesmobs:sylph;[(ddd_necrotic, 0.25), (ddd_poison, -0.25), (ddd_psychic, 0.15), (ddd_radiant, 1.0)];[ddd_radiant];0;0",
			"lycanitesmobs:volcan;[(ddd_acid, 0.25), (ddd_cold, -0.5), (ddd_fire, 1.0)];[ddd_fire];0;0",
			"lycanitesmobs:wisp;[(ddd_fire, 0.25), (ddd_lightning, 0.25), (ddd_radiant, 0.75), (ddd_thunder, -0.25)];[];0;0",
			"lycanitesmobs:argus;[(ddd_force, -0.5), (ddd_psychic, 1.0)];[];0;0",
			"lycanitesmobs:nymph;[(ddd_necrotic, 1.0), (ddd_poison, -1.0), (ddd_psychic, 0.5), (ddd_radiant, 1.0)];[ddd_radiant, ddd_necrotic];0;0",
			"lycanitesmobs:xaphan;[(ddd_acid, 1.0), (ddd_poison, 0.5), (ddd_psychic, -0.5)];[ddd_acid];0;0",
			"lycanitesmobs:herma;[(s, 0.5), (p, 0.5), (b, 0.3), (ddd_acid, 0.2), (ddd_lightning, -0.5)];[];0;0",
			"lycanitesmobs:sutiramu;[(s, 0.2), (p, 0.2), (ddd_fire, -0.5), (ddd_poison, 1.0)];[ddd_poison];0;0",
			"lycanitesmobs:vespidqueen;[(p, 0.15), (b, -0.25), (ddd_acid, -0.25), (ddd_fire, -1.0), (ddd_force, 0.25), (ddd_lightning, -0.25), (ddd_necrotic, -0.25), (ddd_poison, 1.25), (ddd_psychic, 0.25), (ddd_thunder, 0.25)];[ddd_poison];1;0.5",
			"lycanitesmobs:vespid;[(p, 0.15), (b, -0.5), (ddd_acid, -0.5), (ddd_fire, -1.0), (ddd_lightning, -0.5), (ddd_necrotic, -0.25), (ddd_poison, 1.0)];[ddd_poison];0.3;0.25",
			"lycanitesmobs:joustalpha;[(p, 0.8), (b, 0.2), (ddd_acid, -0.25), (ddd_necrotic, -0.25), (ddd_poison, 0.5)];[];0.6;0.3",
			"lycanitesmobs:joust;[(p, 0.8), (b, 0.2), (ddd_acid, -0.25), (ddd_necrotic, -0.25), (ddd_poison, 0.5)];[];0.5;0.3",
			"lycanitesmobs:tarantula;[(p, 0.3), (b, -0.3), (ddd_fire, -0.5)];[ddd_poison];0.3;0.25",
			"lycanitesmobs:calpod;[(s, 0.15), (b, -0.25), (ddd_acid, -0.25), (ddd_fire, -1.0), (ddd_lightning, -0.25), (ddd_necrotic, -0.25), (ddd_poison, 0.5)];[];0.7;0.6",
			"lycanitesmobs:erepede;[(p, 0.5), (ddd_fire, -1.0), (ddd_poison, 0.3)];[];0;0",
			"lycanitesmobs:concapedesegment;[(b, -0.15), (ddd_fire, -1.0), (ddd_poison, 0.3)];[ddd_poison];0.5;0.5",
			"lycanitesmobs:concapede;[(b, -0.15), (ddd_fire, -1.0), (ddd_poison, 0.3)];[ddd_poison];0.5;0.5",
			"lycanitesmobs:frostweaver;[(p, 0.25), (b, -0.25), (ddd_cold, 1.0), (ddd_fire, -1.5)];[];0.3;0.25",
			"lycanitesmobs:eyewig;[(ddd_acid, 0.5), (ddd_poison, 0.5)];[];0.25;0.15",
			"lycanitesmobs:gorgomite;[(p, 0.2), (b, -0.25), (ddd_fire, -1.0), (ddd_poison, 0.25)];[];0.9;0.8",
			"lycanitesmobs:darkling;[(p, 0.2), (b, -0.25), (ddd_fire, -1.0), (ddd_necrotic, 0.8), (ddd_poison, 0.25), (ddd_psychic, -0.25), (ddd_radiant, -0.5)];[];0.25;0.25",
			"lycanitesmobs:lurker;[(p, 0.2), (b, -0.25), (ddd_fire, -1.0), (ddd_poison, 1.0)];[ddd_poison];0.25;0.25",
			"lycanitesmobs:triffid;[(ddd_acid, -1.0), (ddd_fire, -1.0), (ddd_lightning, -0.25), (ddd_necrotic, -1.0), (ddd_poison, 1.0), (ddd_psychic, 0.6), (ddd_thunder, 1.0)];[ddd_thunder, ddd_poison];0;0",
			"lycanitesmobs:treant;[(s, -0.15), (b, 1.0), (ddd_acid, -0.5), (ddd_fire, -1.0), (ddd_necrotic, -0.25), (ddd_poison, -0.25)];[];0;0",
			"lycanitesmobs:ent;[(s, -0.3), (b, 1.0), (ddd_acid, -1.0), (ddd_fire, -1.0), (ddd_poison, -0.25)];[];0;0",
			"lycanitesmobs:tpumpkyn;[(s, -0.3), (b, 0.5), (ddd_acid, -0.5), (ddd_fire, -1.0), (ddd_poison, -0.25)];[];0;0",
			"lycanitesmobs:shambler;[(s, -0.25), (p, 1.0), (b, 0.5), (ddd_acid, -1.0), (ddd_fire, -1.0), (ddd_lightning, -0.25), (ddd_necrotic, -0.5), (ddd_poison, 1.0), (ddd_psychic, 0.25), (ddd_thunder, 0.25)];[ddd_piercing, ddd_poison];0;0",
			"lycanitesmobs:beholder;[];[];0.8;1",
			"lycanitesmobs:grell;[(ddd_necrotic, 0.3)];[ddd_acid, ddd_fire];0;0",
			"lycanitesmobs:grigori;[(ddd_necrotic, 0.3)];[ddd_fire];0;0",
			"lycanitesmobs:vorach;[(ddd_necrotic, 0.75)];[ddd_fire];0;0",
			"lycanitesmobs:trite;[(b, -0.25), (ddd_necrotic, 0.25), (ddd_poison, 0.25), (ddd_psychic, 0.25), (ddd_radiant, -0.5)];[];0.75;0.75",
			"lycanitesmobs:shade;[(s, 0.25), (p, 0.25), (b, 0.25), (ddd_psychic, 1.0), (ddd_radiant, 0.5)];[ddd_psychic];0;0",
			"lycanitesmobs:asmodeus;[(s, 0.25), (p, 0.25), (b, 0.25), (ddd_acid, -0.25), (ddd_force, 0.5), (ddd_lightning, -0.5), (ddd_necrotic, 1.0), (ddd_poison, 1.0), (ddd_psychic, 1.0), (ddd_radiant, -0.5)];[ddd_necrotic, ddd_psychic];0;0",
			"lycanitesmobs:astaroth;[(s, 0.15), (p, 0.15), (b, 0.15), (ddd_acid, -0.5), (ddd_lightning, -0.5), (ddd_necrotic, 1.0), (ddd_poison, 0.75), (ddd_psychic, 0.75), (ddd_radiant, -0.75)];[ddd_necrotic];0;0",
			"lycanitesmobs:krake;[(s, 0.15), (ddd_fire, -0.5), (ddd_force, 0.5), (ddd_poison, -0.75)];[ddd_thunder];0.5;0.5",
			"lycanitesmobs:crusk;[(s, 0.15), (p, 0.15), (b, 0.15)];[];0;0",
			"lycanitesmobs:serpix;[(s, 0.15), (p, 0.15), (b, 0.15), (ddd_cold, 1.0), (ddd_fire, -1.0)];[ddd_cold];0;0",
			"lycanitesmobs:gorger;[(s, 0.15), (p, 0.15), (b, 0.15), (ddd_cold, -1.0), (ddd_fire, 1.0)];[ddd_fire];0;0",
			"lycanitesmobs:behemoth;[(ddd_fire, 0.9), (ddd_necrotic, 0.8), (ddd_psychic, 0.5), (ddd_radiant, -1.0)];[ddd_necrotic, ddd_fire];0;0",
			"lycanitesmobs:belph;[(ddd_fire, 0.5), (ddd_necrotic, 0.4), (ddd_psychic, 0.5), (ddd_radiant, -1.0)];[ddd_necrotic, ddd_fire];0.1;0.25",
			"lycanitesmobs:pinky;[(ddd_fire, 0.5), (ddd_necrotic, 0.5), (ddd_psychic, 0.5), (ddd_radiant, -0.8)];[ddd_necrotic, ddd_fire];0;0",
			"lycanitesmobs:archvile;[(ddd_fire, 1.0), (ddd_necrotic, 1.0), (ddd_psychic, 1.0), (ddd_radiant, -1.0)];[ddd_necrotic, ddd_fire];0;0",
			"lycanitesmobs:cacodemon;[(ddd_fire, 1.0), (ddd_force, 0.2), (ddd_lightning, 1.0), (ddd_necrotic, 1.0), (ddd_psychic, 0.6), (ddd_radiant, -1.0)];[ddd_necrotic, ddd_fire];0;0",
			"lycanitesmobs:rahovart;[(s, 0.1), (p, 0.1), (b, 0.1), (ddd_fire, 1.0), (ddd_force, 0.6), (ddd_necrotic, 1.0), (ddd_poison, 0.5), (ddd_psychic, 1.0), (ddd_radiant, -1.0)];[ddd_necrotic, ddd_fire];0;0"};

	/**
	 * {@link ModConfig.ResistanceCategory#shieldResist}
	 */
	String[] SHIELD_BASE_RESISTS = {
			"minecraft:shield;[(s, 0.8), (p, 0.5), (b, 0.2)]",
			"thebetweeenlands:weedwood_shield;[(s, 0.7), (p, 0.5), (b, 0.4)]",
			"thebetweenlands:living_weedwood_shield;[(s, 1), (p, 1), (b, 0.4), (ddd_psychic, 0.4)]",
			"thebetweenlands:lurker_skin_shield;[(s, 0.4), (p, 0.4), (b, 0.9), (ddd_cold, 0.3)]",
			"thebetweenlands:dentrothyst_shield_green;[(s, 0.3), (p, 0.3), (b, 0.3), (ddd_force, 0.7), (ddd_necrotic, 0.7), (ddd_radiant, 0.7), (ddd_poison, 0.7)]",
			"thebetweenlands:dentrothyst_shield_green_polished;[(s, 0.3), (p, 0.3), (b, 0.3), (ddd_force, 0.7), (ddd_necrotic, 0.7), (ddd_radiant, 0.7), (ddd_poison, 0.7)]",
			"thebetweenlands:dentrothyst_shield_orange;[(s, 0.3), (p, 0.3), (b, 0.3), (ddd_force, 0.7), (ddd_necrotic, 0.7), (ddd_radiant, 0.7), (ddd_poison, 0.7)]",
			"thebetweenlands:dentrothyst_shield_orange_polished;[(s, 0.3), (p, 0.3), (b, 0.3), (ddd_force, 0.7), (ddd_necrotic, 0.7), (ddd_radiant, 0.7), (ddd_poison, 0.7)]",
			"thebetweenlands:bone_shield;[(s, 1), (p, 0.6), (b, 0.6)]",
			"thebetweenlands:syrmorite_shield;[(s, 0.5), (p, 0.5), (b, 0.5)]",
			"thebetweenlands:valonite_shield;[(s, 0.6), (p, 0.6), (b, 0.7)]",
			"thebetweenlands:octine_shield;[(s, 0.8), (p, 0.8), (b, 0.6), (ddd_fire, 0.8)]"};

	/**
	 * {@link ModConfig.DamageCategory.ExtraDamageDistsCategory#daylightWhitelist}
	 */
	String[] ENTITIES_BURN_IN_DAYLIGHT = {
			"minecraft:zombie",
			"minecraft:zombie_villager",
			"minecraft:zombie_horse",
			"minecraft:skeleton",
			"minecraft:skeleton_horse",
			"minecraft:stray",
			"lycanitesmobs:ghoul",
			"lycanitesmobs:geist",
			"lycanitesmobs:necrovore"};

	/**
	 * {@link Modconfig.CompatCategory#shieldClasses}
	 */
	String[] SHIELD_CLASSES = {
			"ItemShieldCore"};

	/******************
	 * SINGLE STRINGS *
	 ******************/

	/**
	 * {@link ModConfig.DamageCategory.ExtraDamageDistsCategory#explosionDist}
	 */
	String EXPLOSION_DIST = "[(b, 1)]";

	/**
	 * {@link ModConfig.ResistanceCategory#playerResists}
	 */
	String PLAYER_BASE_RESISTS = "[];[];0";

	/***********************
	 * TCONSTRUCT & CONARM *
	 ***********************/
	
	/**
	 * {@link ModConfig.CompatCategory.TinkersCategory#bleedDist}
	 */
	String TIC_BLEED_DIST = "[(ddd_necrotic, 1)]";

	/**
	 * {@link ModConfig.CompatCategory.TinkersCategory#matBias}
	 */
	String[] MATERIAL_BIAS = {
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
			"manyullyn;[(ddd_force, 0.7), (s, 0.3)];0.7",
			"copper;[(p, 0.9), (b, 0.1)];0.1",
			"bronze;[(p, 0.5), (s, 0.5)];0.1",
			"lead;[(s, 0.8), (ddd_poison, 0.2)];0.6",
			"silver;[(p, 0.8), (ddd_radiant, 0.2)];0.6",
			"electrum;[(s, 0.6), (ddd_lightning, 0.4)];0.6",
			"steel;[(s, 1)];0.1"};

	/**
	 * {@link ModConfig.CompatCategory.TinkersCategory#toolBias}
	 */
	String[] TOOL_BIAS = {
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
			"shuriken;0"};

	/**
	 * {@link ModConfig.CompatCategory.ConarmCategory#armorMatDist}
	 */
	String[] ARMOR_MATERIAL_DISTRIBUTION = {
			"wood;[(s, 0.5), (p, 0.3), (b, 1)]",
			"stone;[(s, 1), (p, 1), (b, 1), (ddd_thunder, 0.5)]",
			"flint;[(s, 0.7), (p, 0.4), (b, 1), (ddd_thunder, 0.3)]",
			"cactus;[(s, 0.5), (p, 0.4), (b, 1)]",
			"bone;[(s, 1), (p, 0.5), (b, 0.6), (ddd_poison, 0.8)]",
			"obsidian;[(s, 0.6), (ddd_force, 0.8), (ddd_fire, 1), (ddd_acid, 0.4)]",
			"prismarine;[(s, 1), (p, 0.7), (b, 1), (ddd_cold, 0.5)]",
			"endstone;[(s, 1), (p, 1), (b, 1), (ddd_psychic, 0.7), (ddd_force, 0.3)]",
			"paper;[(b, 0.5)]",
			"sponge;[(s, 0.6), (b, 1), (ddd_lightning, 1)]",
			"firewood;[(s, 0.5), (p, 0.3), (b, 1), (ddd_cold, 1)]",
			"iron;[(s, 1), (p, 0.7), (b, 0.3)]",
			"pigiron;[(s, 1), (p, 0.7), (b, 0.3)]",
			"knightslime;[(s, 0.7), (p, 0.7), (b, 1), (ddd_acid, 1), (ddd_poison, 1), (ddd_psychic, 0.6)]",
			"slime;[(b, 1), (ddd_acid, 1), (ddd_poison, 1), (ddd_force, 0.5)]",
			"blueslime;[(b, 1), (ddd_acid, 1), (ddd_poison, 1), (ddd_force, 0.5)]",
			"magmaslime;[(b, 1), (ddd_fire, 1), (ddd_acid, 1), (ddd_poison, 1), (ddd_cold, 0.4)]",
			"netherrack;[(s, 0.25), (p, 0.25), (b, 1), (ddd_necrotic, 1)]",
			"cobalt;[(s, 0.1), (p, 1), (b, 0.7)]",
			"ardite;[(s, 0.6), (p, 0.6), (b, 0.6), (ddd_necrotic, 1), (ddd_psychic, 1)]",
			"manyullyn;[(s, 0.7), (p, 1), (b, 0.6), (ddd_force, 1), (ddd_thunder, 1)]",
			"copper;[(s, 1), (p, 0.7), (b, 0.3)]",
			"bronze;[(s, 1), (p, 0.7), (b, 0.3)]",
			"lead;[(s, 1), (p, 0.7), (b, 0.3), (ddd_acid, 0.3), (ddd_poison, 0.5), (ddd_necrotic, 0.4)]",
			"silver;[(s, 1), (p, 0.7), (b, 0.3), (ddd_radiant, 1), (ddd_necrotic, 0.3)]",
			"electrum;[(s, 1), (p, 0.7), (b, 0.3), (ddd_lightning, 1), (ddd_thunder, 1)]",
			"steel;[(s, 1), (p, 0.7), (b, 0.5), (ddd_thunder, 0.7)]"};

	/**
	 * {@link ModConfig.CompatCategory.ConarmCategory#armorImmunities}
	 */
	String[] ARMOR_IMMUNITIES = {
			"endstone;ddd_psychic",
			"netherrack;ddd_necrotic",
			"slime;ddd_acid",
			"blueslime;ddd_acid",
			"knightslime;ddd_piercing",
			"sponge;ddd_bludgeoning, ddd_thunder",
			"manyullyn;ddd_force",
			"obsidian;ddd_slashing",
			"electrum;ddd_lightning"};

	/*************
	 * LYCANITES *
	 *************/

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#creatureProjectiles}
	 */
	String[] ENEMY_PROJECTILE_MAP = {
			"acidsplash;xaphan",
			"aetherwave;slyph",
			"aquapulse;jengu",
			"arcanelaserstorm;beholder",
			"blizzard;serpix",
			"bloodleech;epion",
			"boulderblast;troll",
			"chaosorb;argus",
			"crystalshard;vapula",
			"demonicblast;cacodemon",
			"devilstar;astaroth",
			"doomfireball;archvile,belph",
			"ember;cinder",
			"faebolt;nymph",
			"frostweb;frostweaver",
			"frostbolt;reiver",
			"hellfireball;behemoth",
			"icefireball;arix",
			"lifedrain;spriggan",
			"lightball;wisp",
			"magma;lobber,gorger",
			"mudshot;erepede",
			"poisonray;eyewig",
			"poop;conba",
			"quill;quillbeast",
			"scorchfireball;afrit,ignibus",
			"spectralbolt;reaper",
			"throwingscythe;clink",
			"tricksterflare;pixen",
			"tundra;wendigo",
			"venomshot;remobra",
			"waterjet;ioray",
			"whirlwind;djinn",
			"acidglob;grell"};

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#scorchfireDistribution}
	 */
	String SCORCHFIRE_DIST = "[(ddd_fire, 0.5), (ddd_force, 0.5)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#doomfireDistribution}
	 */
	String DOOMFIRE_DIST = "[(ddd_fire, 0.5), (ddd_necrotic, 0.5)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#hellfireDistribution}
	 */
	String HELLFIRE_DIST = "[(ddd_fire, 0.3), (ddd_necrotic, 0.7)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#icefireDistribution}
	 */
	String ICEFIRE_DIST = "[(ddd_fire, 0.5), (ddd_cold, 0.5)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#frostfireDistribution}
	 */
	String FROSTFIRE_DIST = "[(ddd_fire, 0.3), (ddd_cold, 0.7)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#shadowfireDistribution}
	 */
	String SHADOWFIRE_DIST = "[(ddd_fire, 0.2), (ddd_psychic, 0.4), (ddd_necrotic, 0.4)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#smitefireDistribution}
	 */
	String SMITEFIRE_DIST = "[(ddd_fire, 0.3), (ddd_radiant, 0.7)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#primefireDistribution}
	 */
	String PRIMEFIRE_DIST = "[(ddd_fire, 1)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#acidDistribution}
	 */
	String ACID_DIST = "[(ddd_acid, 1)]";

	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#oozeDistribution}
	 */
	String OOZE_DIST = "[(ddd_cold, 1)]";
	
	/**
	 * {@link ModConfig.CompatCategory.LycanitesConfigCategory#bleedDistribution}
	 */
	String LYCANITES_BLEED_DIST = "[(ddd_necrotic, 1)]";

	/*********
	 * TETRA *
	 *********/

	/**
	 * {@link ModConfig.CompatCategory.TetraConfigCategory#toolPartDists}
	 */
	String[] TETRA_PART_BIAS = {
			"sword/heavy_blade;[(s, 0.5), (b, 0.5)];0.5",
			"sword/machete;[(s, 1)];1",
			"sword/basic_blade;[(s, 1)];0",
			"sword/shortblade;[(s, 0.5), (p, 0.5)];0.25",
			"duplex/adze;[(p, 1)];1",
			"duplex/claw;[(s, 0.5), (p, 0.5)];0.5",
			"duplex/basic_axe;[(s, 0.6), (b, 0.4)];0.3",
			"duplex/basic_hammer;[(b, 1)];1",
			"duplex/basic_pickaxe;[(p, 1)];0.5",
			"duplex/hoe;[(p, 1)];0.3",
			"duplex/sickle;[(s, 1)];0.4"};

	/**
	 * {@link ModConfig.CompatCategory.TetraConfigCategory#toolMatDists}
	 */
	String[] TETRA_MAT_BIAS = {
			"log;[(b, 1)];0.5",
			"acacia;[(b, 1)];0.5",
			"birch;[(b, 1)];0.5",
			"dark_oak;[(b, 1)];0.5",
			"jungle;[(b, 1)];0.5",
			"oak;[(b, 1)];0.5",
			"spruce;[(b, 1)];0.5",
			"stone;[(b, 1)];1",
			"cobblestone;[(b, 1)];1",
			"diorite;[(b, 1)];1",
			"granite;[(b, 1)];1",
			"andesite;[(b, 1)];1",
			"flint;[(b, 1)];0.3",
			"iron;[(s, 1)];0",
			"gold;[(b, 0.5), (p, 0.5)];0.4",
			"diamond;[(s, 1)];0.5",
			"obsidian;[(s, 0.8), (b, 0.2)];0.6"};
	
	/***********
	 * BAUBLES *
	 ***********/

	/**
	 * {@link ModConfig.CompatCategory.BaublesCategory#baubleMods}
	 */
	String[] BAUBLE_MODS = {
			"baubles:ring;0;[(p,0.2),(ddd_force,0.1)]",
			"baubles:ring;1;[(ddd_necrotic,0.5)]",
			"baubles:ring;2;[(b,0.5)]",
			"baubles:ring;3;[(ddd_fire,0.5)]",
			"baubles:ring;4;[(ddd_poison,0.8)]"};
	
	/**************************
	 * ELECTROBLOB'S WIZARDRY *
	 **************************/

	/**
	 * {@link ModConfig.CompatCategory.ElectroblobsWizardryCategory#minionCapabilities}
	 */
	String[] MINON_CAPABILITIES = {
			"zombie_minion;minecraft:zombie",
			"silverfish_minion;minecraft:silverfish",
			"spider_minion;minecraft:cave_spider",
			"wither_skeleton_minion;minecraft:wither_skeleton",
			"skeleton_minion;minecraft:skeleton",
			"stray_minion;minecraft:stray",
			"husk_minon;minecraft:husk",
			"blaze_minion;minecraft:blaze",
			"vex_minon;minecraft:vex",
			"magic_slime;minecraft:slime"};

	/**
	 * {@link ModConfig.CompatCategory.ElectroblobsWizardryCategory#spellDamageTypeDistributions}
	 */
	String[] SPELL_TYPE_DISTRIBUTIONS = {
			"magic;[(ddd_force, 1)]",
			"fire;[(ddd_fire, 1)]",
			"frost;[(ddd_cold, 1)]",
			"shock;[(ddd_lightning, 1)]",
			"wither;[(ddd_necrotic, 1)]",
			"poison;[(ddd_poison, 1)]",
			"force;[(ddd_force, 1)]",
			"blast;[(b, 0.5), (ddd_thunder, 0.5)]",
			"radiant;[(ddd_radiant, 1)]"};

	/**
	 * {@link ModConfig.CompatCategory.ElectroblobsWizardryCategory#linkedThrowables}
	 */
	String[] LINKED_THROWABLES = {
			"firebomb;fire",
			"poison_bomb;poison",
			"spark_bomb;shock",
			"flamecatcher;magic"};

	/**
	 * {@link ModConfig.CompatCategory.ElectroblobsWizardryCategory#spellDamageType}
	 */
	String[] SPELL_DAMAGE_TYPE = {
			"arc;shock",
			"black_hole;magic",
			"blizzard;frost",
			"boulder;magic",
			"bubble;magic",
			"celestial_smite;radiant",
			"chain_lightning;shock",
			"charge;shock",
			"darkness_orb;wither",
			"dart;magic",
			"decay;wither",
			"detonate;blast",
			"disintegration;fire",
			"earthquake;blast",
			"entrapment;magic",
			"fire_breath;fire",
			"fire_sigil;fire",
			"fireball;fire",
			"firebolt;fire",
			"firebomb;fire",
			"firestorm;fire",
			"flame_ray;fire",
			"force_arrow;force",
			"force_orb;blast",
			"forest_of_thorns;magic",
			"forests_curse;poison",
			"frost_ray;frost",
			"frost_sigil;frost",
			"guardian_beam;magic",
			"hailstorm;frost",
			"homing_spark;shock",
			"ice_charge;frost",
			"ice_lance;frost",
			"ice_shard;frost",
			"ice_spikes;frost",
			"iceball;frost",
			"life_drain;magic",
			"lightning_arrow;shock",
			"lightning_bolt;shock",
			"lightning_hammer;shock",
			"lightning_pulse;shock",
			"lightning_ray;shock",
			"lightning_sigil;shock",
			"lightning_web;shock",
			"magic_missile;magic",
			"paralysis;shock",
			"plague_of_darkness;wither",
			"poison_bomb;poison",
			"radiant_totem;radiant",
			"ray_of_purification;radiant",
			"ring_of_fire;fire",
			"shockwave;blast",
			"snare;magic",
			"spark_bomb;shock",
			"stormcloud;shock",
			"thunderbolt;shock",
			"thunderstorm;shock",
			"tornado;magic",
			"wither;wither",
			"withering_totem;wither"};
	
	/**************
	 * THAUMCRAFT *
	 **************/
	
	/**
	 * {@link ModConfig.CompatCategory.ThaumcraftCategory#aspectDistributions}
	 */
	String[] ASPECT_DIST = {
			"aer;[(b,1)]",
			"terra;[(b,1)]",
			"ignis;[(ddd_fire,1)]",
			"aqua;[(b,1)]",
			"ordo;[(ddd_psychic,0.5),(ddd_force,0.5)]",
			"perditio;[(ddd_force,1)]",
			"vacuos;[(ddd_force,0.5),(ddd_necrotic,0.5)]",
			"lux;[(ddd_psychic,0.5),(ddd_radiant,0.5)]",
			"motus;[(ddd_thunder,1)]",
			"gelum;[(ddd_cold,1)]",
			"vitreus;[(p,1)]",
			"metallum;[(s,1)]",
			"victus;[(ddd_radiant,1)]",
			"mortuus;[(ddd_necrotic,1)]",
			"potentia;[(ddd_lightning,1)]",
			"permutatio;[(ddd_lightning,0.5),(ddd_force,0.5)]",
			"praecantatio;[(ddd_force,1)]",
			"auram;[(ddd_force,1)]",
			"alkimia;[(ddd_acid,0.5),(ddd_fire,0.5)]",
			"vitium;[(ddd_force,0.4),(ddd_psychic,0.3),(ddd_necrotic,0.3)]",
			"tenebrae;[(ddd_psychic,0.5),(ddd_necrotic,0.5)]",
			"alienis;[(ddd_psychic,0.7),(ddd_necrotic,0.3)]",
			"volatus;[(b,1)]",
			"herba;[(p,0.5),(ddd_poison,0.5)]",
			"instrumentum;[(b,0.4),(s,0.3),(p,0.3)]",
			"fabrico;[(b,0.5),(p,0.5)]",
			"machina;[(s,0.3),(b,0.7)]",
			"vinculum;[(p,0.7),(s,0.3)]",
			"spiritus;[(ddd_psychic,0.7),(ddd_radiant,0.3)]",
			"cognitio;[(ddd_psychic,1)]",
			"sensus;[(ddd_psychic,1)]",
			"aversio;[(ddd_psychic,1)]",
			"praemunio;[(ddd_radiant,1)]",
			"desiderium;[(ddd_psychic,0.7),(ddd_necrotic,0.3)]",
			"exanimis;[(ddd_necrotic,1)]",
			"bestia;[(p,0.5),(s,0.5)]",
			"humanus;[(ddd_psychic,0.5),(b,0.5)]"
		};
	
	/**
	 * {@link ModConfig.CompatCategory.ThaumcraftCategory#taintDist}
	 */
	String TAINT_DISTRIBUTION = "[(ddd_force, 0.5),(ddd_psychic,0.5)]";
	
	/**
	 * {@link ModConfig.CompatCategory.ThaumcraftCategory#dissolveDist}
	 */
	String DISSOLVE_DISTRIBUTION = "[(ddd_necrotic,1)]";
	
	/****************
	 * ENDER SKILLS *
	 ****************/
	
	/**
	 * {@link ModConfig.CompatCategory.EnderSkillsCategory#skillDistribution}
	 */
	String[] SKILL_DISTRIBUTION = {
			"crush;[(b, 1)]",
			"slash;[(s, 1)]",
			"barrage_wisps;[(ddd_radiant, 1)]",
			"gleam_flash;[(ddd_radiant, 1)]",
			"lumen_wave;[(ddd_radiant, 1)]",
			"radiant_ray;[(ddd_radiant, 1)]",
			"solar_lance;[(ddd_radiant, 1)]",
			"contaminate;[(ddd_poison, 1)]",
			"shadow_jab;[(ddd_necrotic, 0.5), (ddd_psychic, 0.5)]",
			"gloom;[(ddd_necrotic, 0.5), (ddd_psychic, 0.5)]",
			"black_flame;[(ddd_fire, 0.5), (ddd_necrotic, 0.5)]",
			"flares;[(ddd_fire, 1)]"
	};
	
	/**
	 * {@link ModConfig.CompatCategory.EnderSkillsCategory#skillDotDistribution}
	 */
	String[] SKILL_DOT_DISTRIBUTION = {
			"final_flash;[(ddd_radiant, 1)]",
			"glowing;[(ddd_radiant, 1)]",
			"bleeding;[(ddd_necrotic, 1)]",
			"voided;[(ddd_force, 0.5), (ddd_psychic, 0.5)]",
			"drowning;[(ddd_force, 1)]"
	};
	
	/**
	 * {@link ModConfig.CompatCategory.EnderSkillsCategory#shadowDist}
	 */
	String SHADOW_DISTRIBUTION = "[(ddd_force, 0.5), (ddd_psychic, 0.5)]";
	
	/**
	 * {@link ModConfig.CompatCategory.EnderSkillsCategory#smashDist}
	 */
	String SMASH_DISTRIBUTION = "[(b, 1)]";
}
