package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import arekkuusu.enderskills.api.event.SkillDamageSource;
import arekkuusu.enderskills.common.skill.ability.BaseAbility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.enderskills.EnderSkillsConfigurations;
import yeelp.distinctdamagedescriptions.integration.enderskills.handler.EnderSkillsHandler;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public final class EnderSkillsDistribution extends DDDAbstractPredefinedDistribution {
	private enum SkillType {
		DIRECT(BaseAbility.DAMAGE_HIT_TYPE, EnderSkillsConfigurations.skillDist),
		INDIRECT(BaseAbility.DAMAGE_DOT_TYPE, EnderSkillsConfigurations.skillDotDist);
		
		private final String type;
		private final IDDDConfiguration<IDamageDistribution> config;
		
		private SkillType(String type, IDDDConfiguration<IDamageDistribution> config) {
			this.type = type;
			this.config = config;
		}
		
		private boolean isSkillType(DamageSource src) {
			return this.type.equals(src.damageType) && src instanceof SkillDamageSource;
		}
		
		IDDDConfiguration<IDamageDistribution> getConfig() {
			return this.config;
		}
		
		static SkillType getSkillType(DamageSource src) {
			for(SkillType type : SkillType.values()) {
				if(type.isSkillType(src)) {
					return type;
				}
			}
			return null;
		}
	}
	
	public EnderSkillsDistribution() {
		super("Ender Skills Skill Distribution", Source.BUILTIN);
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.getDamageDistribution(src, target).map(IDamageDistribution::getCategories).orElse(Collections.emptySet());
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		SkillType type = SkillType.getSkillType(src);
		EntityLivingBase owner = (EntityLivingBase) src.getTrueSource();
		if(type == null || owner == null) {
			return Optional.empty();
		}
		DebugLib.outputFormattedDebug("Skill Owner: %s", owner.getName());
		return EnderSkillsHandler.getLastKnownSkillUsed(src, owner).filter((sur) -> sur.didOwnerUseThisTick(owner)).map((sur) -> type.getConfig().getOrFallbackToDefault(sur.getSkillUsed()));
	}
}
