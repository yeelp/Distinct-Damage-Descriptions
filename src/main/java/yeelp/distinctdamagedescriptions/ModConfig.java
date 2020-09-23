package yeelp.distinctdamagedescriptions;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	
	@Name("Suppress Warnings")
	@Comment("If warning messages from Distinct Damage Descriptions are clogging the log, you can disable them here. This may be indicative of a real issue though, so make sure there's no real issue first!")
	public static boolean suppressWarnings = false;
	
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
				  "Each entry is of the form id;s;p;b where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the percent of slashing damage this mob does.",
				  "   p is the percent of piercing damage this mob does.",
				  "   b is the percent of bludgeoning damage this mob does.",
				  "Mobs that aren't listed here will inflict full bludgeoning damage.",
				  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseDmg = 
		{
				"minecraft:cave_spider;0;1;0",
				"minecraft:polar_bear;0.25;0;0.75",
				"minecraft:spider;0;1;0",
				"minecraft:wolf;0;1;0"
		};
		
		@Name("Weapon Base Damage")
		@Comment({"Modify the base damage type distribution of weapons/items.",
			  "Each entry is of the form id;s;p;b where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_sword)",
			  "   s is the base percent of slashing damage this item does.",
			  "   p is the base percent of piercing damage this item does.",
			  "   b is the base percent of bludgeoning damage this item does.",
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
				  "Each entry is of the form id;s;p;b;items where:",
				  "   id is the namespaced id of the projectile ENTITY (e.g. minecraft:arrow)",
				  "   s is the base percent of slashing damage this projectile does.",
				  "   p is the base percent of piercing damage this projectile does.",
				  "   b is the base percent of bludgeoning damage this projectile does.",
				  "   items are the item ids associated with this projectile, separated by a comma (For example, arrow entities are associated with the item ids minecraft:arrow and minecraft:tipped_arrow). This is used for tooltips.",
				  "       If the projectile has no item form, omit this part, including the semicolon (so the entry looks like id;s;p;b)",
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
		@Comment("If true, Distinct Damage Descriptions will load custom damage types from JSON found in config/distinctdamagedescriptions/damageTypes")
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
		}
	}
	
	public static class ResistanceCategory
	{
		@Name("Mob Base Resistance/Weakness")
		@Comment({"Modify the base resistance/weakness of mobs. This overrides any resistances provided by creature types JSON files.",
				  "Each entry is of the form id;s;p;b;immunities;adaptive where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the base slashing resistance percent of this mob.",
				  "   p is the base piercing resistance percent of this mob.",
				  "   b is the base bludgeoning resistance percent of this mob.",
				  "   immunities is a string with the only the characters \"b\", \"s\", or \"p\" indicating what damage types (bludgeoning, slashing, piercing respectively) this mob is immune to. Multiple damage types, or no damage types can be listed. Order doesn't matter.",
				  "   adaptive is a decimal in the range [0,1] indicating the percent chance that this mob has adaptive immunity, with 0 being never, and 1 being always.",
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
		
		@Name("Armor Resistance/Weakness")
		@Comment({"Modify the base resistance effectiveness of armor",
			  "Each entry is of the form id;s;p;b where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_chestplate)",
			  "   s is the base slashing effectiveness of this armor.",
			  "   p is the base piercing effectiveness of this armor.",
			  "   b is the base bludgeoning effectiveness of this armor.",
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
				  "These JSON files can be used to apply potion immunities, along with applying the same resistances to large amounts of mobs at once.",
				  "Remember that entries in the \"Mob Base Resistances/Weaknesses\" config will override any mob resistances provided by these JSON files.",
				  "This is done to allow end users to still have granular control over resistances while still providing potion immunities to large swaths of mobs at once."})
		@RequiresMcRestart
		public boolean useCreatureTypes = false;
	}
	
	public static class ClientCategory
	{
		@Name("Show Damage Distribution Tooltip for all items")
		@Comment("If true, the Damage Distribution tooltip appears for all items. If false, it only appears for items with a damage distribution manually set.")
		public boolean alwaysShowDamageDistTooltip = true;
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
			}
		}
		
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
	}
}
