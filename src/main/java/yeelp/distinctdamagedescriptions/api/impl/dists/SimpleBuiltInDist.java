package yeelp.distinctdamagedescriptions.api.impl.dists;

import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class SimpleBuiltInDist extends AbstractSingleTypeDist {
	public static final SimpleBuiltInDist ANVIL = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableAnvilDamage, DamageSource.ANVIL, DDDBuiltInDamageType.BLUDGEONING);
	public static final SimpleBuiltInDist CACTUS = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableCactusDamage, DamageSource.CACTUS, DDDBuiltInDamageType.PIERCING);
	public static final SimpleBuiltInDist FALL = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableFallDamage, DamageSource.FALL, DDDBuiltInDamageType.BLUDGEONING);
	public static final SimpleBuiltInDist FALLING_BLOCK = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableFallingBlockDamage, DamageSource.FALLING_BLOCK, DDDBuiltInDamageType.BLUDGEONING);
	public static final SimpleBuiltInDist FLY_INTO_WALL = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableFlyIntoWallDamage, DamageSource.FLY_INTO_WALL, DDDBuiltInDamageType.BLUDGEONING);
	public static final SimpleBuiltInDist LIGHTNING = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableLightningDamage, DamageSource.LIGHTNING_BOLT, DDDBuiltInDamageType.LIGHTNING);
	public static final SimpleBuiltInDist WITHER = new SimpleBuiltInDist(() -> ModConfig.dmg.extraDamage.enableWitherDamage, DamageSource.WITHER, DDDBuiltInDamageType.NECROTIC);

	private final DamageSource src;
	private final DDDDamageType result;

	public SimpleBuiltInDist(Supplier<Boolean> config, DamageSource source, DDDDamageType result) {
		super(source.damageType, Source.BUILTIN, config);
		this.src = source;
		this.result = result;
	}

	@Override
	protected DDDDamageType getType() {
		return this.result;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return this.src == source;
	}
}
