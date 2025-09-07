package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.block.BlockFireBase;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.lycanites.LycanitesConsts;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public class LycanitesFireDistribution extends LycanitesPredefinedDistribution {

	public static final LycanitesFireDistribution SCORCHFIRE = new LycanitesFireDistribution(LycanitesConsts.SCORCHFIRE, DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableScorchFireDistribution, () -> ModConfig.compat.lycanites.scorchFireDistribution, () -> DefaultValues.SCORCHFIRE_DIST);
	public static final LycanitesFireDistribution DOOMFIRE = new LycanitesFireDistribution(LycanitesConsts.DOOMFIRE, DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableDoomFireDistribution, () -> ModConfig.compat.lycanites.doomFireDistribution, () -> DefaultValues.DOOMFIRE_DIST);
	public static final LycanitesFireDistribution HELLFIRE = new LycanitesFireDistribution(LycanitesConsts.HELLFIRE, DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableHellFireDistribution, () -> ModConfig.compat.lycanites.hellFireDistribution, () -> DefaultValues.HELLFIRE_DIST);
	public static final LycanitesFireDistribution FROSTFIRE = new LycanitesColdFireDistribution(LycanitesConsts.FROSTFIRE, () -> ModConfig.compat.lycanites.enableFrostFireDistribution, () -> ModConfig.compat.lycanites.frostFireDistribution, () -> DefaultValues.FROSTFIRE_DIST);
	public static final LycanitesFireDistribution ICEFIRE = new LycanitesColdFireDistribution(LycanitesConsts.ICEFIRE, () -> ModConfig.compat.lycanites.enableIceFireDistribution, () -> ModConfig.compat.lycanites.iceFireDistribution, () -> DefaultValues.ICEFIRE_DIST);
	public static final LycanitesFireDistribution SHADOWFIRE = new LycanitesFireDistribution(LycanitesConsts.SHADOWFIRE, DamageSource.WITHER, () -> ModConfig.compat.lycanites.enableShadowFireDistribution, () -> ModConfig.compat.lycanites.shadowFireDistribution, () -> DefaultValues.SHADOWFIRE_DIST);
	public static final LycanitesFireDistribution SMITEFIRE = new LycanitesFireDistribution(LycanitesConsts.SMITEFIRE, DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableSmiteFireDistribution, () -> ModConfig.compat.lycanites.smiteFireDistribution, () -> DefaultValues.SMITEFIRE_DIST);
	public static final LycanitesFireDistribution PRIMEFIRE = new LycanitesFireDistribution(LycanitesConsts.PRIMEFIRE, DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enablePrimeFireDistribution, () -> ModConfig.compat.lycanites.primeFireDistribution, () -> DefaultValues.PRIMEFIRE_DIST);

	private static final class LycanitesColdFireDistribution extends LycanitesFireDistribution {
		private static final boolean FOUND_COLD_FIRE;
		private static final Predicate<DamageSource> IS_COLD_FIRE = (src) -> src.damageType.equals(LycanitesConsts.COLD_FIRE);
		private static final Predicate<DamageSource> IS_MAGIC = (src) -> src == DamageSource.MAGIC;
		private final Predicate<DamageSource> dmgSourceMatchPredicate;
		static {
			boolean b;
			try {
				BlockFireBase.class.getDeclaredField(LycanitesConsts.COLD_FIRE.toUpperCase());
				b = true;
			}
			catch (NoSuchFieldException | SecurityException e) {
				DistinctDamageDescriptions.warn("Could not find cold_fire DamageSource! Using magic for DamageSource for Icefire and Frostfire distributions!");
				b = false;
			}
			FOUND_COLD_FIRE = b;
		}
		
		LycanitesColdFireDistribution(String key, Supplier<Boolean> config, Supplier<String> configEntry, Supplier<String> fallback) {
			super(key, config, configEntry, fallback);
			if(FOUND_COLD_FIRE) {
				this.dmgSourceMatchPredicate = IS_COLD_FIRE;
			}
			else {
				this.dmgSourceMatchPredicate = IS_MAGIC;
			}
		}

		@Override
		protected boolean doesDamageSourceMatch(DamageSource src) {
			return this.dmgSourceMatchPredicate.test(src);
		}
	}

	private BlockFireBase fire;

	protected LycanitesFireDistribution(String key, Supplier<Boolean> config, Supplier<String> configEntry, Supplier<String> fallback) {
		super(key, config, configEntry, fallback);
	}

	private LycanitesFireDistribution(String key, DamageSource src, Supplier<Boolean> config, Supplier<String> configEntry, Supplier<String> fallback) {
		this(key, config, configEntry, fallback);
		this.src = src;
	}

	@Override
	public final boolean enabled() {
		return super.enabled() && this.fire != null;
	}

	@Override
	public final int priority() {
		return 2;
	}

	@Override
	protected boolean isApplicable(DamageSource src, EntityLivingBase target) {
		return this.doesDamageSourceMatch(src) && this.isInsideFire(target);
	}

	protected boolean doesDamageSourceMatch(DamageSource src) {
		return this.src == src;
	}

	private boolean isInsideFire(EntityLivingBase target) {
		// Adapt code from World.class isFlammableWithin
		AxisAlignedBB bb = target.getEntityBoundingBox().shrink(0.001);
		int j2 = MathHelper.floor(bb.minX);
		int k2 = MathHelper.ceil(bb.maxX);
		int l2 = MathHelper.floor(bb.minY);
		int i3 = MathHelper.ceil(bb.maxY);
		int j3 = MathHelper.floor(bb.minZ);
		int k3 = MathHelper.ceil(bb.maxZ);

		if(target.world.isAreaLoaded(new BlockPos(j2, l2, j3), new BlockPos(k2, i3, k3), true)) {
			BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain();

			for(int l3 = j2; l3 < k2; ++l3) {
				for(int i4 = l2; i4 < i3; ++i4) {
					for(int j4 = j3; j4 < k3; ++j4) {
						if(target.world.getBlockState(pos.setPos(l3, i4, j4)).getBlock() == this.fire) {
							pos.release();
							return true;
						}
					}
				}
			}
			pos.release();
		}
		return false;
	}

	@Override
	public void loadModSpecificData() {
		Block block = ObjectManager.getBlock(this.getName());
		if(block instanceof BlockFireBase) {
			this.fire = (BlockFireBase) block;
		}
		else {
			if(this == SMITEFIRE || this == PRIMEFIRE) {
				DistinctDamageDescriptions.warn(String.format("Could not find BlockFireBase for %s. However, if this version of Lycanite's Mobs doesn't have %s, this can be ignored.", this.getName(), YLib.capitalize(this.getName())));
			}
			else {
				DistinctDamageDescriptions.err(String.format("Could not find BlockFireBase for %s! DDD doesn't expect this fire block to not exist!"));
			}
			this.fire = null;
		}
	}

}
