package yeelp.distinctdamagedescriptions;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
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
	
	@Name("Debug Mode")
	@Comment("Enable debug mode. Console will be filled with debug messages. The frequency/content of the messages may vary across versions. Only enable if troubleshooting or developing.")
	public static boolean showDotsOn = false;
	
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
		public String[] mobBaseDmg = {"minecraft:spider;0;1;0"};
		
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
				  "Each entry is of the form id;damage where:",
				  "   id is the namespaced id of the projectile ENTITY (e.g. minecraft:arrow)",
				  "   damage is a string with the only the characters \"b\", \"s\", or \"p\", indicating if the projectile does bludgeoning, slashing, or piercing damage respectively",
				  "          The order of the characters doesn't matter, and multiple damage types can be listed ",
				  "Projectiles that aren't listed here will inflict piercing damage, no matter the projectile.",
				  "Malformed entries in this list will be ignored."})
	    @RequiresMcRestart
	    public String[] projectileDamageTypes = {};
	}
	
	public static class ResistanceCategory
	{
		@Name("Mob Base Resistance/Weakness")
		@Comment({"Modify the base resistance/weakness of mobs",
				  "Each entry is of the form id;s;p;b;immunities;adaptive where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the base slashing resistance of this mob.",
				  "   p is the base piercing resistance of this mob.",
				  "   b is the base bludgeoning resistance of this mob.",
				  "   immunities is a string with the only the characters \"b\", \"s\", or \"p\" indicating what damage types (bludgeoning, slashing, piercing respectively) this mob is immune to. Multiple damage types, or no damage types can be listed. Order doesn't matter.",
				  "   adaptive is a decimal in the range [0,1] indicating the percent chance that this mob has adaptive immunity, with 0 being never, and 1 being always.",
				  "Mobs that aren't listed here will have no resistances. Positive values indicate a resistance, negative values indicate a weakness.",
				  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that mob takes 50% less damage from that type, and a value of -0.5 means that mob takes 50% more damage from that type",
		          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseResist = {"minecraft:spider;0;0;-0.25;;0.3", "minecraft:enderman;0;0;0;;0.7"};
		
		@Name("Armor Resistance/Weakness")
		@Comment({"Modify the base resistance/weakness of armor",
			  "Each entry is of the form id;s;p;b;t where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_chestplate)",
			  "   s is the base slashing resistance of this armor.",
			  "   p is the base piercing resistance of this armor.",
			  "   b is the base bludgeoning resistance of this armor.",
			  "   t is the toughness rating of the armor. Armors with higher toughness rating are more effective at reducing high damage attacks.",
			  "armors that aren't listed here will have no resistances (this doesn't mean the armor does nothing). Positive values indicate a resistance, negative values indicate a weakness.",
			  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that armor blocks 50% of damage from that type, and a value of -0.5 means that armor increases damage of that type by 50%.",
			  "These resistances happen IN PLACE of regular armor calculations, and resistances/weaknesses from multiple armor pieces worn at the same stack additively.",
	          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] armorResist = 
		{
			"minecraft:leather_helmet;0.05;-0.1;0.1;0",
			"minecraft:leather_chestplate;0.1;-0.15;0.1;0",
			"minecraft:leather_leggings;0.1;-0.1;0.1;0",
			"minecraft:leather_boots;0.05;-0.05;0;0",
			"minecraft:chainmail_helmet;0.15;-0.15;0.15;0",
			"minecraft:chainmail_chestplate;0.2;-0.2;0.2;0",
			"minecraft:chainmail_leggings;0.2;-0.15;0.15;0",
			"minecraft:chainmail_boots;0.15;-0.1;0.15;0",
			"minecraft:iron_helmet;0.15;0.25;0.15;1",
			"minecraft:iron_chestplate;0.2;0.35;0.3;1",
			"minecraft:iron_leggings;0.2;0.25;0.25;1",
			"minecraft:iron_boots;0.15;0.15;0.15;1",
			"minecraft:golden_helmet;0.1;0.1;-0.1;0.5",
			"minecraft:golden_chestplate;0.2;0.15;-0.15;0.5",
			"minecraft:golden_leggings;0.15;0.15;-0.1;0.5",
			"minecraft:golden_boots;0.1;0.1;-0.05;0.5",
			"minecraft:diamond_helmet;-0.15;0.25;0.15;2",
			"minecraft:diamond_chestplate;-0.3;0.4;0.3;2",
			"minecraft:diamond_leggings;-0.25;0.3;0.2;2",
			"minecraft:diamond_boots;-0.1;0.2;0.2;2"
		};
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
	}
}
