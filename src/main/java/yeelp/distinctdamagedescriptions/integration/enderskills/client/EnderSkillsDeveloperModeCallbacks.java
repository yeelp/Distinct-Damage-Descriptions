package yeelp.distinctdamagedescriptions.integration.enderskills.client;

import arekkuusu.enderskills.api.event.SkillDamageEvent;
import arekkuusu.enderskills.common.skill.ability.BaseAbility;
import net.minecraft.world.World;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.util.development.IDeveloperModeInfoCallback;

public enum EnderSkillsDeveloperModeCallbacks implements IDeveloperModeInfoCallback<SkillDamageEvent> {
	SKILL_NAME {
		@Override
		public DeveloperStatus getStatus() {
			return ModConfig.compat.enderskills.showSkillNameInfo;
		}
		
		@Override
		protected String getName() {
			return "SKILL NAME";
		}
		
		@Override
		protected String getInfo(SkillDamageEvent evt) {
			return evt.getSkill().getRegistryName().toString();
		}
	},
	SKILL_TYPE {
		@Override
		public DeveloperStatus getStatus() {
			return ModConfig.compat.enderskills.showSkillTypeInfo;
		}
		
		@Override
		protected String getName() {
			return "SKILL TYPE";
		}
		
		@Override
		protected String getInfo(SkillDamageEvent evt) {
			return evt.getSource().damageType.equals(BaseAbility.DAMAGE_HIT_TYPE) ? "Direct" : "DOT";
		}
	};
	
	@Override
	public String callback(SkillDamageEvent evt) {
		return String.format("%s: %s", this.getName(), this.getInfo(evt));
	}

	@Override
	public boolean shouldFire(SkillDamageEvent evt) {
		return true;
	}

	@Override
	public World getWorld(SkillDamageEvent evt) {
		return evt.getEntityLiving().getEntityWorld();
	}
	
	protected abstract String getName();
	
	protected abstract String getInfo(SkillDamageEvent evt);
}
