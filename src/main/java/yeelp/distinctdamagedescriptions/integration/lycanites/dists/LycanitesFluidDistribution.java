package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import java.util.function.Supplier;

import com.lycanitesmobs.ObjectManager;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.lycanites.LycanitesConsts;

public final class LycanitesFluidDistribution extends LycanitesPredefinedDistribution {
	public static final LycanitesFluidDistribution OOZE = new LycanitesFluidDistribution(LycanitesConsts.OOZE, () -> ModConfig.compat.lycanites.enableOozeDistribution, () -> ModConfig.compat.lycanites.oozeDistirbution, () -> DefaultValues.OOZE_DIST);
	public static final LycanitesFluidDistribution ACID = new LycanitesFluidDistribution(LycanitesConsts.ACID, () -> ModConfig.compat.lycanites.enableAcidDistribution, () -> ModConfig.compat.lycanites.acidDistribution, () -> DefaultValues.ACID_DIST);
	
	public LycanitesFluidDistribution(String key, Supplier<Boolean> config, Supplier<String> configEntry, Supplier<String> fallback) {
		super(key, config, configEntry, fallback);
	}

	@Override
	protected boolean isApplicable(DamageSource src, EntityLivingBase target) {
		return this.src == src;
	}
	
	@Override
	public void loadModSpecificData() {
		this.src = ObjectManager.getDamageSource(this.getName());
	}
}
