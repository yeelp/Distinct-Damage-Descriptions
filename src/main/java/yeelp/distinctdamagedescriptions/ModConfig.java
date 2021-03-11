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

@Config(modid = ModConsts.MODID)
public class ModConfig
{
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
	
	@Name("Generate Configs")
	@Comment("If set to true, and Distinct Damage Description will try to generate appropriate config values for weapons, mobs, armor and projectiles.")
	public static boolean generateStats = false;
	
	@Name("Generate JSON")
	@Comment("If set, DistinctDamageDescriptions will generate example JSON files for custom damage types and creature types.")
	public static boolean generateJSON = true;
	
	public static class CompatCategory
	{
		@Name("Tool Material Bias")
		@Comment("Tools that use materials, like TiC tools, will use these distributions to influence the tool distribution.")
		public String[] toolMatBias = {};
		
		public String[] armorMatBias = {};
	}
	
	public static class EnchantCategory
	{
		@Name("Enable Brute Force")
		@Comment("If false, the Brute Force enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
		@RequiresMcRestart
		public boolean enableBruteForce = true;
		
		@Name("Enable Sly Strike")
		@Comment("If false, the Sly Strike enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
		@RequiresMcRestart
		public boolean enableSlyStrike = true;
	}
	
	public static class DamageCategory
	{
		@Name("Mob Base Damage")
		@Comment({"Modify the base damage type distribution of mobs.",
				  "Each entry is of the form id;[(t,a)] where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of each damage type a mob does.",
				  "      t is the type of damage. Requires the 'ddd_' prefix. can use s, p, b, instead for slashing, piercing or bludegeoning",
				  "      a is the percent of this damage this mob does. if a = 0, it is ignored.",
				  "      If custom damage is disabled, any custom damage here will be distributed amongst all non-zero physical damages (Or just bludgeoning if none are non zero)",
				  "   All percents MUST add to 1",
				  "Mobs that aren't listed here will inflict full bludgeoning damage.",
				  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseDmg = 
		{
				"minecraft:cave_spider;[(p, 0,75), (ddd_poison, 0.25)]",
				"minecraft:polar_bear;[(s, 0.25), (b, 0.75)]",
				"minecraft:spider;[(p, 1)]",
				"minecraft:wolf;[(p, 1)]",
				"minecraft:zombie;[(b, 0.75), (ddd_necrotic, 0.25)]"
		};
		
		@Name("Weapon Base Damage")
		@Comment({"Modify the base damage type distribution of weapons/items.",
			  "Each entry is of the form id;s;p;b;[(t,a)] where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_sword)",
			  "   s is the base percent of slashing damage this item does.",
			  "   p is the base percent of piercing damage this item does.",
			  "   b is the base percent of bludgeoning damage this item does.",
			  "   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of custom damage a weapon does if custom damage is enabled.",
			  "      t is the type of custom damage. Requires the 'ddd_' prefix.",
			  "      a is the percent of this custom damage this mob does.",
			  "      If this list is present but custom damage is disabled, the percentages here will be distributed amongst all non-zero physical damages.",
			  "      This list may be omitted if the weapon inflicts no custom damage, including the semicolon.",
			  "   All percents ('s', 'p', 'b', and the value 'a' of every tuple) MUST add to 1",
			  "Items that aren't listed here will inflict 100% bludgeoning damage, no matter the item.",
			  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] itemBaseDamage = 
		{
			"minecraft:arrow;0;1;0",
			"minecraft:tipped_arrow;0;1;0",
			"minecraft:spectral_arrow;0;1;0",
			"minecraft:wooden_sword;0.5;0;0.5",
			"minecraft:wooden_axe;0.3;0;0.7",
			"minecraft:wooden_pickaxe;0;0.5;0.5",
			"minecraft:wooden_shovel;0;0;1",
			"minecraft:wooden_hoe;0;0.5;0.5",
			"minecraft:stone_sword;0;0;1",
			"minecraft:stone_axe;0;0;1",
			"minecraft:stone_pickaxe;0;0.2;0.8",
			"minecraft:stone_shovel;0;0;1",
			"minecraft:stone_hoe;0;0.2;0.8",
			"minecraft:iron_sword;0.8;0.2;0",
			"minecraft:iron_axe;0.6;0;0.4",
			"minecraft:iron_pickaxe;0;0.9;0.1",
			"minecraft:iron_shovel;0;0.1;0.9",
			"minecraft:iron_hoe;0;1;0",
			"minecraft:golden_sword;1;0;0",
			"minecraft:golden_axe;1;0;0",
			"minecraft:golden_pickaxe;0;1;0",
			"minecraft:golden_shovel;0;0;1",
			"minecraft:golden_hoe;0;1;0",
			"minecraft:diamond_sword;1;0;0",
			"minecraft:diamond_axe;0.8;0;0.2",
			"minecraft:diamond_pickaxe;0;1;0",
			"minecraft:diamond_shovel;0;0;1",
			"minecraft:diamond_hoe;0;1;0"
		};
		
		@Name("Projectile Damage Type")
		@Comment({"Modify the damage type of projectiles",
				  "Each entry is of the form id;s;p;b;[(t,a)];items where:",
				  "   id is the namespaced id of the projectile ENTITY (e.g. minecraft:arrow)",
				  "   s is the base percent of slashing damage this projectile does.",
				  "   p is the base percent of piercing damage this projectile does.",
				  "   b is the base percent of bludgeoning damage this projectile does.",
				  "   [(t,a)] is a list of tuples (t,a), separated by commas, that lists the percent of custom damage a projectile does if custom damage is enabled.",
				  "      t is the type of custom damage. Requires the 'ddd_' prefix",
				  "      a is the percent of this custom damage this projectile does.",
				  "      If this list is present but custom damage is disabled, the percentages here will be distributed amongst all non-zero physical damages.",
				  "      This list may be omitted if the projectile inflicts no custom damage.",
				  "   All percents ('s', 'p', 'b', and the value 'a' of every tuple) MUST add to 1",
				  "   items are the item ids associated with this projectile, separated by a comma (For example, arrow entities are associated with the item ids minecraft:arrow and minecraft:tipped_arrow). This is used for tooltips.",
				  "      If the projectile has no item form, omit this part, including the semicolon.",
				  "Projectiles that aren't listed here will inflict piercing damage, no matter the projectile.",
				  "Malformed entries in this list will be ignored."})
	    @RequiresMcRestart
	    public String[] projectileDamageTypes = 
		{
				"minecraft:arrow;0;1;0;minecraft:arrow,minecraft:tipped_arrow",
				"minecraft:spectral_arrow;0;1;0;minecraft:spectral_arrow",
				"minecraft:llama_spit;0;0;1"
		};
		
		@Name("Use Custom Damage Types")
		@Comment("If true, Distinct Damage Descriptions will load and enable custom damage types from JSON found in config/distinctdamagedescriptions/damageTypes")
		@RequiresMcRestart
		public boolean useCustomDamageTypes = false;
		
		@Name("Use Custom Death Messages")
		@Comment({"If custom damage types are enabled and this is turned on, the JSON specified death messages will be used.", 
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
		public static class ExtraDamageDistsCategory
		{
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
		}
	}
	
	public static class ResistanceCategory
	{
		@Name("Adapt to Custom Damage")
		@Comment("If true, mobs with adaptive resistance will adapt to custom damage as well, if custom damage is enabled. If false, they will only adapt to the built in slashing, piercing, bludgeoning")
		public boolean adaptToCustom = false;
		
		@Name("Mob Base Resistance/Weakness")
		@Comment({"Modify the base resistance/weakness of mobs. This overrides any resistances provided by creature types JSON files.",
				  "Each entry is of the form id;s;p;b;immunities;adaptive;amount;[(t,a)];[custom_immunities] where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the base slashing resistance percent of this mob.",
				  "   p is the base piercing resistance percent of this mob.",
				  "   b is the base bludgeoning resistance percent of this mob.",
				  "   immunities is a string with the only the characters \"b\", \"s\", or \"p\" indicating what damage types (bludgeoning, slashing, piercing respectively) this mob is immune to. Multiple damage types, or no damage types can be listed. Order doesn't matter.",
				  "   adaptive is a decimal in the range [0,1] indicating the percent chance that this mob has adaptive immunity, with 0 being never, and 1 being always.",
				  "   amount is the amount resistances change for this mob when adaptability triggers",
				  "   [(t,a)] is a list of comma separated tuples (t,a), of custom damage types this mob resists (if enabled).",
				  "      t is the damage type this mob resists. Requires the 'ddd_' prefix.",
				  "      a is the base percent of resistance this mob has to that custom damage type.",
				  "   [custom_immunities] is a comma separated list of custom damage types that this mob resists (if enabled). Requires the 'ddd_' prefix for each damage type.",
				  "   Both [(t,a)] and [custom_immunities] must be present if either or both are to be used. If the mob has no custom immunities or damage resistances, just leave that list empty (put [])",
				  "   You can omit both lists if the mob has no custom resistances AND no custom immunities.",
				  "Mobs that aren't listed here will have no resistances. Positive values indicate a resistance, negative values indicate a weakness.",
				  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that mob takes 50% less damage from that type, and a value of -0.5 means that mob takes 50% more damage from that type",
		          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseResist = 
		{
				"minecraft:bat;0;0;-0.5;;0;0",
				"minecraft:blaze;0;0;0;;0;0",
				"minecraft:cave_spider;0;0.25;-0.25;;0.3;0.25",
				"minecraft:chicken;0;0;-0.25;;0;0",
				"minecraft:cow;0;0;0;;0;0",
				"minecraft:creeper;0;0;0;;0;0",
				"minecraft:donkey;0;0;0;;0;0",
				"minecraft:elder_guardian;0.25;0.25;0.25;;1.0;0.75",
				"minecraft:enderman;0;0;0;;0.7;0.5",
				"minecraft:endermite;0;0;0;;0.9;0.75",
				"minecraft:evoker;0;0;0;;0;0",
				"minecraft:ghast;0;0;0.5;;0;0",
				"minecraft:guardian;0;0;0;;0.25;0.25",
				"minecraft:horse;0;0;0;;0;0",
				"minecraft:husk;0;0;0.25;;0;0",
				"minecraft:llama;0;0;0;;0;0",
				"minecraft:magma_cube;0.25;0.25;0.25;b;0;0",
				"minecraft:mooshroom;0;0;0;;0;0",
				"minecraft:mule;0;0;0;;0;0",
				"minecraft:ocelot;0;0;0;;0;0",
				"minecraft:parrot;0;0;-0.5;;0;0",
				"minecraft:pig;0;0;0;;0;0",
				"minecraft:polar_bear;0;0;0.25;;0;0",
				"minecraft:rabbit;0;0;0;;0;0",
				"minecraft:sheep;0;0;0;;0;0",
				"minecraft:shulker;0.5;-0.5;0.75;;0.25;0.5",
				"minecraft:silverfish;0;0;-0.25;;0.95;1.0",
				"minecraft:skeleton;0.25;0;0;;0;0",
				"minecraft:skeleton_horse;0.25;0;0;;0;0",
				"minecraft:slime;-0.25;0;0.25;b;0;0",
				"minecraft:spider;0;0.25;-0.25;;0.3;0.25",
				"minecraft:squid;0;0;0;;0;0",
				"minecraft:stray;0.25;0;0;;0;0",
				"minecraft:vex;0;0;0;;0;0",
				"minecraft:villager;0;0;0;;0;0",
				"minecraft:vindicator;0;0;0;;0;0",
				"minecraft:witch;0;0;0;;0.1;0.25",
				"minecraft:wither_skeleton;0.25;0;0;;0;0",
				"minecraft:wolf;0;0;0;;0;0",
				"minecraft:zombie;0;0;0.25;;0;0",
				"minecraft:zombie_horse;0;0;0.25;;0;0",
				"minecraft:zombie_pigman;0;0;0.25;;0;0",
				"minecraft:zombie_villager;0;0;0.25;;0;0",
				"minecraft:iron_golem;0.5;0.75;1.0;;0;0",
				"minecraft:wither;0.25;0;0;;0;0"
		};
		
		@Name("Shield Effectiveness")
		@Comment({"Modify how shields block damage.",
			  "Each entry is of the form id;s;p;b;[(t,a)] where:",
			  "   id is the namespaced id of the item (e.g. minecraft:shield)",
			  "   s is the slashing effectiveness of the shield.",
			  "   p is the piercing effectiveness of the shield.",
			  "   b is the bludgeoning effectiveness of the shield.",
			  "   [(t,a)] is a list of comma separated tuples of custom damage types this shield blocks (if enabled).",
			  "      t is the type this shield blocks. Requires the 'ddd_' prefix.",
			  "      a is the effectiveness the shield has against that damage type.",
			  "      This list may be omitted if the shield blocks no custom damage.",
			  "Shields not listed here will act as normal shields (will block all damage they can interact with).",
			  "Shield effectiveness determines how much physical damage a shield can block. A shield with 0.3 slashing effectiveness can only block 30% of incoming slashing damage. The remaining 70% goes through the shield and damages the player, following regular damage calculation.",
			  "Blocking damage will still knock the attacker back, but the knockback strength is a percentage of the original vanilla knockback; that percentage comes from the amount of damage actually reduced (a shield that only blocks 33% of the incoming damage will knock the attacker back by about 33% of the vanilla amount).",
			  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] shieldResist = {"minecraft:shield;0.8;0.5;0.2"};
		
		@Name("Armor Resistance")
		@Comment({"Modify the base resistance effectiveness of armor",
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
		public String[] armorResist = 
		{
			"minecraft:leather_helmet;0.3;0.05;1.0",
			"minecraft:leather_chestplate;0.3;0.05;1.0",
			"minecraft:leather_leggings;0.3;0.05;1.0",
			"minecraft:leather_boots;0.3;0.05;1.0",
			"minecraft:chainmail_helmet;0.6;0;0.8",
			"minecraft:chainmail_chestplate;0.6;0;0.8",
			"minecraft:chainmail_leggings;0.6;0;0.8",
			"minecraft:chainmail_boots;0.6;0;0.8",
			"minecraft:iron_helmet;1.0;0.7;0.3",
			"minecraft:iron_chestplate;1.0;0.7;0.30",
			"minecraft:iron_leggings;1.0;0.7;0.3",
			"minecraft:iron_boots;1.0;0.7;0.3",
			"minecraft:golden_helmet;1.0;0.6;0.25",
			"minecraft:golden_chestplate;1.0;0.6;0.25",
			"minecraft:golden_leggings;1.0;0.6;0.25",
			"minecraft:golden_boots;1.0;0.6;0.25",
			"minecraft:diamond_helmet;0.15;1.0;0.7",
			"minecraft:diamond_chestplate;0.15;1.0;0.7",
			"minecraft:diamond_leggings;0.15;1.0;0.7",
			"minecraft:diamond_boots;0.15;1.0;0.7"
		};
		
		@Name("Use Creature Types")
		@Comment({"If true, DistinctDamageDescriptions will load custom creature types from JSON located in config/distinctdamagedescriptions/creatureTypes.",
				  "These JSON files can be used to apply potion/critical hit immunities to large swaths of mobs at once. Also usuable in CraftTweaker."})
		@RequiresMcRestart
		public boolean useCreatureTypes = false;
	}
	
	public static class ClientCategory
	{
		@Name("Show Damage Distribution Tooltip for all items")
		@Comment("If true, the Damage Distribution tooltip appears for all items. If false, it only appears for items with a damage distribution manually set.")
		public boolean alwaysShowDamageDistTooltip = true;
		
		@Name("Use Damage Type Icons")
		@Comment({"If true, Distinct Damage Descriptions will use icons for built in damage types (slashing, piercing, bludgeoning).",
				 "These icons will appear in place of those names everwhere except under the \"Starting Immunities\" in spawn egg tooltips."})
		public boolean useIcons = false;
		
	}
	
	@Mod.EventBusSubscriber(modid = ModConsts.MODID)
	private static class EventHandler 
	{
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) 
		{
			if (event.getModID().equals(ModConsts.MODID)) 
			{
				ConfigManager.sync(ModConsts.MODID, Config.Type.INSTANCE);
				DebugLib.updateStatus();
				DDDExplosionDist.update();
			}
		}
		
		@SubscribeEvent
		public static void onWorldLoad(final WorldEvent.Load evt)
		{
			if(ModConfig.dmg.useCustomDeathMessages)
			{
				evt.getWorld().getGameRules().setOrCreateGameRule("showDeathMessages", "false");
			}
			else
			{
				evt.getWorld().getGameRules().setOrCreateGameRule("showDeathMessages", "true");
			}
		}
		
		@SubscribeEvent
		public static void onWorldSave(final WorldEvent.Save evt)
		{
			if(generateStats && ConfigGenerator.hasUpdated())
			{
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
