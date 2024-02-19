package yeelp.distinctdamagedescriptions.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.config.client.ClientCategory;
import yeelp.distinctdamagedescriptions.config.compat.CompatCategory;
import yeelp.distinctdamagedescriptions.config.damage.DamageCategory;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory;
import yeelp.distinctdamagedescriptions.config.enchant.EnchantCategory;
import yeelp.distinctdamagedescriptions.config.resists.ResistanceCategory;
import yeelp.distinctdamagedescriptions.handlers.DaylightTracker;
import yeelp.distinctdamagedescriptions.integration.hwyla.client.HwylaTooltipMaker;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipMaker;

@Config(modid = ModConsts.MODID)
public class ModConfig {
	@Name("Core")
	@Comment("Alter core behaviour about Distinct Damage Descriptions")
	public static final GeneralCategory core = new GeneralCategory();

	@Name("Damage")
	@Comment("Alter the base damage of weapons and mobs")
	public static final DamageCategory dmg = new DamageCategory();

	@Name("Resistance")
	@Comment("Alter the base resistances of armor, shields and mobs")
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

	@Name("Compatibility/Integration")
	@Comment("Tweak DDD's behaviour with other mods")
	public static final CompatCategory compat = new CompatCategory();

	@Name("Development Tools")
	@Comment("Potentially useful troubleshooting/debugging tools for those configuring the mod - especially for modpacks")
	public static final DevelopmentCategory dev = new DevelopmentCategory();

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
				DaylightTracker.update();
				TooltipMaker.updateFormatters();
				HwylaTooltipMaker.updateFormatters();
			}
		}

		@SubscribeEvent
		public static void onWorldSave(@SuppressWarnings("unused") final WorldEvent.Save evt) {
			if(core.generateStats && ConfigGenerator.hasUpdated()) {
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
