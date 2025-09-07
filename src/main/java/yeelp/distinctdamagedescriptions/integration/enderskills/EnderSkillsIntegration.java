package yeelp.distinctdamagedescriptions.integration.enderskills;

import java.util.Arrays;

import com.google.common.collect.ImmutableList;

import arekkuusu.enderskills.api.event.SkillDamageEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.enderskills.client.EnderSkillsDeveloperModeCallbacks;
import yeelp.distinctdamagedescriptions.integration.enderskills.dist.EnderSkillsDistribution;
import yeelp.distinctdamagedescriptions.integration.enderskills.dist.EnderSkillsPredefinedDistribution;
import yeelp.distinctdamagedescriptions.integration.enderskills.dist.EnderSkillsThornyDistribution;
import yeelp.distinctdamagedescriptions.integration.enderskills.handler.EnderSkillsHandler;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;

public final class EnderSkillsIntegration implements IModIntegration {

	@Override
	public String getModTitle() {
		return IntegrationTitles.ENDER_SKILLS_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.ENDER_SKILLS_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new EnderSkillsHandler());
	}
	
	@Override
	public boolean preInit(FMLPreInitializationEvent evt) {
		Arrays.stream(EnderSkillsDeveloperModeCallbacks.values()).forEach((callback) -> DeveloperModeKernel.registerCallback(SkillDamageEvent.class, callback));
		DDDRegistries.distributions.registerAll(new EnderSkillsThornyDistribution(), new EnderSkillsDistribution());
		EnderSkillsPredefinedDistribution.getDists().forEach(DDDRegistries.distributions::register);
		return IModIntegration.super.preInit(evt);
	}
	
	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		return IModIntegration.super.postInit(evt);
	}

}
