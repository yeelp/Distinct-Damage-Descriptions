package yeelp.distinctdamagedescriptions.integration.thaumcraft.dist;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDSingleDamageDistributionConfigReader;

public final class ThaumcraftPredefinedDistribution extends DDDAbstractPredefinedDistribution {

	private final class ConfigReader extends DDDSingleDamageDistributionConfigReader {

		public ConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) throws IllegalArgumentException {
			super(name, configSup, fallbackSup);
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		protected void setDistribution(IDamageDistribution dist) {
			ThaumcraftPredefinedDistribution.this.dist = dist;
		}
		
	}
	
	private final DamageSource src;
	private IDamageDistribution dist;
	private final DDDConfigReader reader;
	private final Supplier<Boolean> enabled;
	
	//@formatter:off
	private static final Collection<ThaumcraftPredefinedDistribution> DISTS = ImmutableSet.of(
			new ThaumcraftPredefinedDistribution("taint", DamageSourceThaumcraft.taint, () -> ModConfig.compat.thaumcraft.useTaintDist, () -> ModConfig.compat.thaumcraft.taintDistribution, () -> DefaultValues.TAINT_DISTRIBUTION),
			new ThaumcraftPredefinedDistribution("liquid_death", DamageSourceThaumcraft.dissolve, () -> ModConfig.compat.thaumcraft.useLiquidDeathDist, () -> ModConfig.compat.thaumcraft.dissolveDistribution, () -> DefaultValues.DISSOLVE_DISTRIBUTION));
	//@formatter:on
	
	protected ThaumcraftPredefinedDistribution(String name, DamageSource src, Supplier<Boolean> enabled, Supplier<String> config, Supplier<String> fallback) {
		super("Thaumcraft: "+name, Source.BUILTIN);
		this.src = src;
		this.reader = this.new ConfigReader(this.getName(), config, fallback);
		this.enabled = enabled;
	}

	@Override
	public boolean enabled() {
		return this.enabled.get();
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return src.equals(this.src) ? this.dist.getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return src.equals(this.src) ? Optional.of(this.dist) : Optional.empty();
	}
	
	public static void update() {
		DISTS.forEach((td) -> {
			if(td.enabled()) {
				td.reader.read();
			}
		});
	}
	
	public static Iterable<DDDConfigReader> getReaders() {
		return DISTS.stream().map((td) -> td.reader).collect(Collectors.toList());
	}
	
	public static Iterable<ThaumcraftPredefinedDistribution> getDists() {
		return DISTS;
	}

}
