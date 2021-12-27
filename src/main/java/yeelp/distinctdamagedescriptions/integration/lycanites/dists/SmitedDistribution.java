package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import java.util.Optional;

import com.lycanitesmobs.ObjectManager;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.impl.dists.AbstractSingleTypeDist;

public class SmitedDistribution extends AbstractSingleTypeDist {

	private static final Optional<Potion> SMITED_POTION;

	static {
		SMITED_POTION = Optional.ofNullable(ObjectManager.getEffect("smited"));
	}

	public SmitedDistribution() {
		super(() -> ModConfig.compat.lycanites.enableSmitedDist);
	}

	@Override
	public String getName() {
		return "lycanitesSmited";
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.RADIANT;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source == DamageSource.ON_FIRE && SMITED_POTION.map(target::isPotionActive).orElse(false);
	}

	@Override
	public int priority() {
		return 2;
	}
}
