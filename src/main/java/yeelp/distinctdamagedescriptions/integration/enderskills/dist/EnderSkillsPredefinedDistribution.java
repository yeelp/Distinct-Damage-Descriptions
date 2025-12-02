package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

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
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDSingleDamageDistributionConfigReader;

public abstract class EnderSkillsPredefinedDistribution extends DDDAbstractPredefinedDistribution {

	protected final class ConfigReader extends DDDSingleDamageDistributionConfigReader {

		public ConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) throws IllegalArgumentException {
			super(name, configSup, fallbackSup);
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		protected void setDistribution(IDamageDistribution dist) {
			EnderSkillsPredefinedDistribution.this.dist = dist;
		}
		
	}
	
	private static final Collection<EnderSkillsPredefinedDistribution> DISTS = ImmutableSet.of(new EnderSkillsShadowDistribution(), new SimpleEnderSkillsPredefinedDistribution("smash", () -> ModConfig.compat.enderskills.smashDist, () -> DefaultValues.SMASH_DISTRIBUTION), new EnderSkillsSyphonDistribution());
	
	private IDamageDistribution dist;
	private DDDConfigReader reader;
	
	protected EnderSkillsPredefinedDistribution(String name, Supplier<String> configSup, Supplier<String> fallbackSup) {
		super(name, Source.BUILTIN);
		this.reader = new ConfigReader(name, configSup, fallbackSup);
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? this.dist.getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? Optional.of(this.dist) : Optional.empty();
	}
	
	protected abstract boolean isApplicable(DamageSource src, EntityLivingBase target);
	
	public static final void update() {
		DISTS.forEach(EnderSkillsPredefinedDistribution::readFromConfig);
	}
	
	protected void readFromConfig() {
		this.reader.read();
	}
	
	public static final Iterable<DDDConfigReader> getReaders() {
		return DISTS.stream().map((espd) -> espd.reader).collect(Collectors.toList());
	}
	
	public static final Iterable<EnderSkillsPredefinedDistribution> getDists() {
		return DISTS;
	}

}
