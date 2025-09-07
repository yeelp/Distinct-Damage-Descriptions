package yeelp.distinctdamagedescriptions.integration.enderskills.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Maps;

import arekkuusu.enderskills.api.event.SkillDamageEvent;
import arekkuusu.enderskills.api.registry.Skill;
import arekkuusu.enderskills.common.skill.ability.BaseAbility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.enderskills.dist.EnderSkillsPredefinedDistribution;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public final class EnderSkillsHandler extends Handler {
	
	public static final class SkillUseRecord {
		private final int tickTimestamp;
		private final String skillUsed;
		
		SkillUseRecord(EntityLivingBase owner, Skill skillUsed) {
			this.tickTimestamp = owner.ticksExisted;
			this.skillUsed = skillUsed.getRegistryName().toString();
		}
		
		public String getSkillUsed() {
			return this.skillUsed;
		}
		
		public boolean didOwnerUseThisTick(EntityLivingBase owner) {
			return this.tickTimestamp == owner.ticksExisted;
		}
		
		@Override
		public String toString() {
			return String.format("{Skill timestamp: %d, Skill Name: %s}", this.tickTimestamp, this.skillUsed);
		}
	}

	private static final Map<String, Map<UUID, SkillUseRecord>> USAGE_TRACKER = Maps.newHashMap();
	
	static {
		USAGE_TRACKER.put(BaseAbility.DAMAGE_HIT_TYPE, Maps.newHashMap());
		USAGE_TRACKER.put(BaseAbility.DAMAGE_DOT_TYPE, Maps.newHashMap());
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onSkillUse(SkillDamageEvent evt) {
		getTracker(evt.getSource()).ifPresent((m) -> {
			EntityLivingBase owner = evt.getEntityLiving();
			if(owner == null) {
				return;
			}
			m.put(owner.getUniqueID(), new SkillUseRecord(owner, evt.getSkill()));				
			DeveloperModeKernel.fireCallbacks(evt);
		});
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onConfigChange(ConfigChangedEvent evt) {
		if(evt.getModID().equals(ModConsts.MODID)) {
			EnderSkillsPredefinedDistribution.update();
		}
	}
	
	public static Optional<SkillUseRecord> getLastKnownSkillUsed(DamageSource src, EntityLivingBase user) {
		Optional<SkillUseRecord> skill = getTracker(src).map((m) -> m.get(user.getUniqueID()));
		skill.ifPresent((sur) -> DebugLib.outputFormattedDebug("User Tick Timestamp: %d, Skill Used: %s", user.ticksExisted, sur));
		return skill;
	}
	
	private static Optional<Map<UUID, SkillUseRecord>> getTracker(DamageSource src) {
		return Optional.ofNullable(USAGE_TRACKER.get(src.damageType));
	}
}
