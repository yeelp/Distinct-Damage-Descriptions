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
			  "Items that aren't listed here will inflict 1.0 bludgeoning damage, no matter the item.",
			  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] itemBaseDamage = {"minecraft:wooden_pickaxe;0;0.5;0.5", "minecraft:diamond_sword;1;0;0", "minecraft:iron_sword;0.8;0.2;0"};
		
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
			  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that armor blocks 50% of damage from that type BEFORE regular armor calculations, and a value of -0.5 means that armor increases damage of that type by 50% BEFORE regular armor calculations.",
	          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] armorResist = {"minecraft:diamond_chestplate;-0.3;0.8;0.5;2", "minecraft:iron_chestplate;0.4;0.4;0.3;0", "minecraft:chain_chestplate;0.3;-0.2;0.2;0"};
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
