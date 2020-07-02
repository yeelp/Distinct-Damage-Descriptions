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
		@Comment({"Modify the base damage and damage type mobs do.",
				  "Each entry is of the form id;s;p;b where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the base slashing damage this mob does.",
				  "   p is the base piercing damage this mob does.",
				  "   b is the base bludgeoning damage this mob does.",
				  "Mobs that aren't listed here will inflict full bludgeoning damage.",
				  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseDmg = {};
		
		@Name("Weapon Base Damage")
		@Comment({"Modify the base damage and damage type weapons/items do.",
			  "Each entry is of the form id;s;p;b where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_sword)",
			  "   s is the base slashing damage this item does.",
			  "   p is the base piercing damage this item does.",
			  "   b is the base bludgeoning damage this item does.",
			  "Items that aren't listed here will inflict 1.0 bludgeoning damage, no matter the item.",
			  "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] itemBaseDamage = {};
		
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
				  "Each entry is of the form id;s;p;b where:",
				  "   id is the namespaced id of the mob (e.g. minecraft:zombie)",
				  "   s is the base slashing resistance of this mob.",
				  "   p is the base piercing resistance of this mob.",
				  "   b is the base bludgeoning resistance of this mob.",
				  "Mobs that aren't listed here will have no resistances. Positive values indicate a resistance, negative values indicate a weakness.",
				  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that mob takes 50% less damage from that type, and a value of -0.5 means that mob takes 50% more damage from that type",
		          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] mobBaseResist = {};
		
		@Name("Armor Resistance/Weakness")
		@Comment({"Modify the base resistance/weakness of armor",
			  "Each entry is of the form id;s;p;b where:",
			  "   id is the namespaced id of the item (e.g. minecraft:diamond_chestplate)",
			  "   s is the base slashing resistance of this armor.",
			  "   p is the base piercing resistance of this armor.",
			  "   b is the base bludgeoning resistance of this armor.",
			  "armors that aren't listed here will have no resistances (this doesn't mean the armor does nothing). Positive values indicate a resistance, negative values indicate a weakness.",
			  "Resistances and weaknesses are percentage based. That is, an value of 0.5 means that armor blocks 50% of damage from that type BEFORE regular armor calculations, and a value of -0.5 means that armor increases damage of that type by 50% BEFORE regular armor calculations.",
	          "Malformed entries in this list will be ignored."})
		@RequiresMcRestart
		public String[] armorResist = {};
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
