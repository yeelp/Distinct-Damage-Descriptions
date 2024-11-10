package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDSingleStringConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public abstract class LycanitesPredefinedDistribution extends DDDAbstractPredefinedDistribution {

	private final class ConfigReader extends DDDSingleStringConfigReader {

		public ConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) throws IllegalArgumentException {
			super(name, configSup, fallbackSup);
		}
		
		@Override
		protected boolean validEntry(String entry) {
			return entry.matches(ConfigReaderUtilities.DIST_REGEX);
		}

		@SuppressWarnings("synthetic-access")
		@Override
		protected void parseEntry(String entry) {
			try {
				LycanitesPredefinedDistribution.this.dist = new DamageDistribution(ConfigReaderUtilities.parseMap(this, entry, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> 0.0f));
				DebugLib.outputFormattedDebug("%s Distribution is: %s", this.getName(), LycanitesPredefinedDistribution.this.dist.toString());
			}
			catch (ConfigParsingException e) {
				//Much like explosion dist, if this happens, something really bad happened.
				e.printStackTrace();
			}
		}
	}
	
	private static final Collection<LycanitesPredefinedDistribution> DISTS = ImmutableSet.of(LycanitesFireDistribution.DOOMFIRE, LycanitesFireDistribution.FROSTFIRE, LycanitesFireDistribution.HELLFIRE, LycanitesFireDistribution.ICEFIRE, LycanitesFireDistribution.PRIMEFIRE, LycanitesFireDistribution.SCORCHFIRE, LycanitesFireDistribution.SHADOWFIRE, LycanitesFireDistribution.SMITEFIRE, LycanitesFluidDistribution.ACID, LycanitesFluidDistribution.OOZE);
	
	private IDamageDistribution dist;
	private final Supplier<Boolean> config;
	protected DamageSource src;
	private final ConfigReader reader;
	
	public LycanitesPredefinedDistribution(String key, Supplier<Boolean> config, Supplier<String> configEntry, Supplier<String> fallback) {
		super(key, Source.BUILTIN);
		this.config = config;
		this.reader = this.new ConfigReader(key, configEntry, fallback);
	}
	
	@Override
	public boolean enabled() {
		return this.config.get();
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? this.getDamageDistribution().getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? Optional.of(this.getDamageDistribution()) : Optional.empty();
	}
	
	private IDamageDistribution getDamageDistribution() {
		return this.dist;
	}
	
	protected abstract boolean isApplicable(DamageSource src, EntityLivingBase target);
	
	public abstract void loadModSpecificData();

	public static void update() {
		DISTS.forEach((dist) -> {
			if(dist.enabled()) {
				dist.reader.read();
			}
		});
	}
	
	public static Iterable<DDDConfigReader> getReaders() {
		return DISTS.stream().map((lfd) -> lfd.reader).collect(Collectors.toList());
	}
	
	public static Iterable<LycanitesPredefinedDistribution> getDists() {
		return DISTS;
	}
	
}
