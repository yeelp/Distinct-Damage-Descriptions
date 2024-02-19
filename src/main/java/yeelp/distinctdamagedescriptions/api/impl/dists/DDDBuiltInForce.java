package yeelp.distinctdamagedescriptions.api.impl.dists;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public abstract class DDDBuiltInForce extends AbstractSingleTypeDist {
	public static final DDDBuiltInForce GUARDIAN_DIST = new SourceBasedForceDist(() -> ModConfig.dmg.extraDamage.enableGuardianDamage, EntityGuardian.class, "guardian");
	public static final DDDBuiltInForce EVOKER_FANGS_DIST = new SourceBasedForceDist(() -> ModConfig.dmg.extraDamage.enableEvokerFangsDamage, EntityEvokerFangs.class, "evokerFangs");

	DDDBuiltInForce(String name, Supplier<Boolean> config) {
		super(name, Source.BUILTIN, config);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.FORCE;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source.isMagicDamage() && classifyForce(source, target);
	}

	protected abstract boolean classifyForce(DamageSource source, EntityLivingBase target);

	public static final class ThornsDist extends DDDBuiltInForce {
		public ThornsDist() {
			super("thorns", () -> ModConfig.dmg.extraDamage.enableThornsDamage);
		}

		@Override
		protected boolean classifyForce(DamageSource source, EntityLivingBase target) {
			return source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage() && !(source.getTrueSource() instanceof EntityGuardian);
		}
	}

	public static final class SourceBasedForceDist extends DDDBuiltInForce {
		private final Class<? extends Entity> clazz;

		SourceBasedForceDist(Supplier<Boolean> config, Class<? extends Entity> clazz, String name) {
			super(name, config);
			this.clazz = clazz;
		}

		@Override
		protected boolean classifyForce(DamageSource source, EntityLivingBase target) {
			return this.clazz.isInstance(source.getImmediateSource());
		}
	}
}
