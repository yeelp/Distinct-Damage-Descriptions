package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDSingleDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.LycanitesFireDistribution.LycanitesColdFireDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class LycanitesPredefinedDistribution extends DDDAbstractPredefinedDistribution {

	private final class ConfigReader extends DDDSingleDamageDistributionConfigReader {

		public ConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) {
			super(name, configSup, fallbackSup);
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		protected void setDistribution(IDamageDistribution dist) {
			LycanitesPredefinedDistribution.this.dist = dist;
			DebugLib.outputFormattedDebug("%s Distribution is: %s", this.getName(), LycanitesPredefinedDistribution.this.dist.toString());					
		}

	}
	
	@SuppressWarnings("StaticInitializerReferencesSubClass")
    private static final Collection<LycanitesPredefinedDistribution> DISTS = ImmutableSet.of(LycanitesFireDistribution.DOOMFIRE, LycanitesColdFireDistribution.FROSTFIRE, LycanitesFireDistribution.HELLFIRE, LycanitesColdFireDistribution.ICEFIRE, LycanitesFireDistribution.PRIMEFIRE, LycanitesFireDistribution.SCORCHFIRE, LycanitesFireDistribution.SHADOWFIRE, LycanitesFireDistribution.SMITEFIRE, LycanitesFluidDistribution.ACID, LycanitesFluidDistribution.OOZE, new LycanitesBleedDistribution());
	
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
	public Set<DDDDamageType> getTypes(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
		return this.isApplicable(src, target) ? this.getDamageDistribution().getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
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
		return DISTS.stream().map((lpd) -> lpd.reader).collect(Collectors.toList());
	}
	
	public static Iterable<LycanitesPredefinedDistribution> getDists() {
		return DISTS;
	}
	
}
