package yeelp.distinctdamagedescriptions.util;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.google.common.base.Stopwatch;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.readers.DDDMultiEntryConfigReader;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.DDDBasicConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDMobResistancesConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDProjectileConfigReader;

/**
 * Read configuration entries from Config
 * 
 * @author Yeelp
 *
 */
public final class DDDConfig {

	private static class ConfigReader<T> implements Runnable {

		private final DDDMultiEntryConfigReader<T> reader;
		private final String name;

		<U extends T> ConfigReader(String[] configList, IDDDConfiguration<T> config, Constructor<U> constructor, float defaultVal, String name) {
			this.reader = new DDDBasicConfigReader<T>(configList, config, constructor, defaultVal);
			this.name = name;
		}

		ConfigReader(DDDMultiEntryConfigReader<T> reader, String name) {
			this.reader = reader;
			this.name = name;
		}

		@Override
		public void run() {
			Stopwatch timer = Stopwatch.createStarted();
			this.reader.read();
			timer.stop();
			DistinctDamageDescriptions.info(String.format("%s loaded in %s", this.name, timer.toString()));
		}
		
		Thread createThread() {
			return new Thread(this, this.name);
		}
	}

	public static void readConfig() {
		String[] resists = new String[ModConfig.resist.mobBaseResist.length + 1];
		System.arraycopy(ModConfig.resist.mobBaseResist, 0, resists, 1, ModConfig.resist.mobBaseResist.length);
		resists[0] = "player;" + ModConfig.resist.playerResists;
		
		Queue<Thread> readers = new LinkedList<Thread>();
		try {
			Constructor<DamageDistribution> damageDistConstructor = DamageDistribution.class.getConstructor(Map.class);
			readers.add(new ConfigReader<IDamageDistribution>(ModConfig.dmg.mobBaseDmg, DDDConfigurations.mobDamage, damageDistConstructor, 0.0f, "Mob Base Damage Distributions").createThread());
			readers.add(new ConfigReader<IDamageDistribution>(ModConfig.dmg.itemBaseDamage, DDDConfigurations.items, damageDistConstructor, 0.0f, "Item Damage Distributions").createThread());
			readers.add(new ConfigReader<IArmorDistribution>(ModConfig.resist.armorResist, DDDConfigurations.armors, ArmorDistribution.class.getConstructor(Map.class), 0.0f, "Armor Distributions").createThread());
			readers.add(new ConfigReader<ShieldDistribution>(ModConfig.resist.shieldResist, DDDConfigurations.shields, ShieldDistribution.class.getConstructor(Map.class), 0.0f, "Shield Distributions").createThread());
			readers.add(new ConfigReader<MobResistanceCategories>(new DDDMobResistancesConfigReader(resists), "Mob Resistances").createThread());
			readers.add(new ConfigReader<IDamageDistribution>(new DDDProjectileConfigReader(), "Projectile Damage Distributions").createThread());
		}
		catch(NoSuchMethodException | SecurityException e) {
			DistinctDamageDescriptions.fatal("Can't read config! Something has going terribly wrong!");
			throw new RuntimeException(e);
		}
		readers.forEach(Thread::start);
		try {
			for(Thread thread : readers) {
				thread.join();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		DDDMultiEntryConfigReader.displayErrors();
	}
}
