package yeelp.distinctdamagedescriptions.integration.tic.dists;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import slimeknights.tconstruct.tools.traits.TraitSharp;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDSingleDamageDistributionConfigReader;

public final class TiCBleedDistribution extends DDDAbstractPredefinedDistribution {

	private static final class ConfigReader extends DDDSingleDamageDistributionConfigReader {
		public ConfigReader() throws IllegalArgumentException {
			super("TiC Bleed", () -> ModConfig.compat.tinkers.bleedDist, () -> DefaultValues.TIC_BLEED_DIST);
		}
		
		@Override
		protected void setDistribution(IDamageDistribution dist) {
			TiCBleedDistribution.dist = dist; 
		}
	}
	
	protected static IDamageDistribution dist;
	private static final DDDConfigReader READER = new ConfigReader();
	
	public TiCBleedDistribution() {
		super("TiC Bleed", Source.BUILTIN);
	}
	
	@Override
	public boolean enabled() {
		return ModConfig.compat.tinkers.useBleedDistribution;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return isBleeding(src, target) ? dist.getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return isBleeding(src, target) ? Optional.of(dist) : Optional.empty();
	}
	
	public static void update() {
		if(ModConfig.compat.tinkers.useBleedDistribution) {
			READER.read();			
		}
	}
	
	public static DDDConfigReader getConfigReader() {
		return READER;
	}

	private static boolean isBleeding(DamageSource src, EntityLivingBase target) {
		return src.damageType.equals("bleed") && target.isPotionActive(TraitSharp.DOT);
	}
}
