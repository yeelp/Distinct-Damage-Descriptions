package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.COLD;
import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.FIRE;
import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.FORCE;
import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.NECROTIC;
import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.PSYCHIC;
import static yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType.RADIANT;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.block.BlockFireBase;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class LycanitesFireDistribution extends DDDAbstractPredefinedDistribution {
	public static final LycanitesFireDistribution SCORCHFIRE = new LycanitesFireDistribution("scorchfire", DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableScorchFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.5f), new Tuple<DDDDamageType, Float>(FORCE, 0.5f));
	public static final LycanitesFireDistribution DOOMFIRE = new LycanitesFireDistribution("doomfire", DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableDoomFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.5f), new Tuple<DDDDamageType, Float>(NECROTIC, 0.5f));
	public static final LycanitesFireDistribution HELLFIRE = new LycanitesFireDistribution("hellfire", DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableHellFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.3f), new Tuple<DDDDamageType, Float>(NECROTIC, 0.7f));
	public static final LycanitesFireDistribution FROSTFIRE = new LycanitesFireDistribution("frostfire", DamageSource.MAGIC, () -> ModConfig.compat.lycanites.enableFrostFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.3f), new Tuple<DDDDamageType, Float>(COLD, 0.7f));
	public static final LycanitesFireDistribution ICEFIRE = new LycanitesFireDistribution("icefire", DamageSource.MAGIC, () -> ModConfig.compat.lycanites.enableIceFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.5f), new Tuple<DDDDamageType, Float>(COLD, 0.5f));
	public static final LycanitesFireDistribution SHADOWFIRE = new LycanitesFireDistribution("shadowfire", DamageSource.WITHER, () -> ModConfig.compat.lycanites.enableShadowFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.2f), new Tuple<DDDDamageType, Float>(PSYCHIC, 0.4f), new Tuple<DDDDamageType, Float>(NECROTIC, 0.4f));
	public static final LycanitesFireDistribution SMITEFIRE = new LycanitesFireDistribution("smitefire", DamageSource.IN_FIRE, () -> ModConfig.compat.lycanites.enableSmiteFireDistribution, new Tuple<DDDDamageType, Float>(FIRE, 0.3f), new Tuple<DDDDamageType, Float>(RADIANT, 0.7f));
	public static final LycanitesFireDistribution PRIMEFIRE = new LycanitesFireDistribution("primefire", DamageSource.IN_FIRE, () -> ModConfig.dmg.extraDamage.enableFireDamage, new Tuple<DDDDamageType, Float>(FIRE, 1.0f));

	private final BlockFireBase fire;
	private final Supplier<Boolean> config;
	private final IDamageDistribution dist;
	private final DamageSource src;

	@SafeVarargs
	private LycanitesFireDistribution(String key, DamageSource src, Supplier<Boolean> config, Tuple<DDDDamageType, Float>... weights) {
		super(key, Source.BUILTIN);
		this.config = config;
		this.src = src;
		this.dist = new DamageDistribution(weights);
		Block block = ObjectManager.getBlock(key);
		if(block instanceof BlockFireBase) {
			this.fire = (BlockFireBase) block;
		}
		else {
			this.fire = null;
		}
	}

	@Override
	public boolean enabled() {
		return this.config.get() && this.fire != null;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? this.getTypes() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return this.isApplicable(src, target) ? Optional.of(this.getDamageDistribution()) : Optional.empty();
	}

	@Override
	public int priority() {
		return 2;
	}

	private Set<DDDDamageType> getTypes() {
		return this.getDamageDistribution().getCategories();
	}

	private IDamageDistribution getDamageDistribution() {
		return this.dist;
	}

	private boolean isApplicable(DamageSource src, EntityLivingBase target) {
		return this.enabled() && src == this.src && this.isInsideFire(target);
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

}
