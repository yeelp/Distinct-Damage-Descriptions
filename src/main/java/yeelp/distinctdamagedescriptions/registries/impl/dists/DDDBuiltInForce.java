package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

public abstract class DDDBuiltInForce extends AbstractSingleTypeDist
{
	public static final DDDBuiltInForce GUARDIAN_DIST = new SourceBasedForceDist(() -> ModConfig.dmg.extraDamage.enableGuardianDamage, EntityGuardian.class, "guardian");
	public static final DDDBuiltInForce EVOKER_FANGS_DIST = new SourceBasedForceDist(() -> ModConfig.dmg.extraDamage.enableEvokerFangsDamage, EntityEvokerFangs.class, "evokerFangs");
	
	DDDBuiltInForce(Supplier<Boolean> config)
	{
		super(config);
	}

	@Override
	protected DDDDamageType getType()
	{
		return DDDBuiltInDamageType.FORCE;
	}
	
	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target)
	{
		return source.isMagicDamage() && classifyForce(source, target);
	}
	
	protected abstract boolean classifyForce(DamageSource source, EntityLivingBase target);
	
	public static final class ThornsDist extends DDDBuiltInForce
	{
		public ThornsDist()
		{
			super(() -> ModConfig.dmg.extraDamage.enableThornsDamage);
		}

		@Override
		protected boolean classifyForce(DamageSource source, EntityLivingBase target)
		{
			return source.damageType.equals("thorns") && !(source.getTrueSource() instanceof EntityGuardian);
		}

		@Override
		public String getName()
		{
			return "thorns";
		}	
	}
	
	public static final class SourceBasedForceDist extends DDDBuiltInForce
	{
		private final Class<? extends Entity> clazz;
		private final String name;
		SourceBasedForceDist(Supplier<Boolean> config, Class<? extends Entity> clazz, String name)
		{
			super(config);
			this.clazz = clazz;
			this.name = name;
		}

		@Override
		protected boolean classifyForce(DamageSource source, EntityLivingBase target)
		{
			return clazz.isInstance(source.getImmediateSource());
		}

		@Override
		public String getName()
		{
			return name;
		}	
	}
}
