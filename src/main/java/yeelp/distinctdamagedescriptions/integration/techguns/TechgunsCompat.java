package yeelp.distinctdamagedescriptions.integration.techguns;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;

public final class TechgunsCompat implements IModIntegration {
	
	interface TechgunsConsts {
		String TG_DAMAGESOURCE_CLASS = "techguns.damagesystem.TGDamageSource";
		String INPCTG_DAMAGESYSTEM_CLASS = "techguns.api.npc.INpcTGDamageSystem";
		String TG_KNOCKBACK = "tg_knockback";
		float DUMMY_KNOCKBACK_THRESHOLD = 0.01f;
	}

	private Handler handlerToRegister = null;

	@Override
	public String getModID() {
		return IntegrationIds.TECHGUNS_ID;
	}

	@Override
	public String getModTitle() {
		return IntegrationTitles.TECHGUNS_TITLE;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(this.handlerToRegister);
	}

	@Override
	public boolean initStart(FMLInitializationEvent evt) {
		try {
			TechgunsReflectionHelper.init();
			this.handlerToRegister = new TechgunsCacluationHandler();
			return IModIntegration.super.initStart(evt);
		}
		catch(ClassNotFoundException | SecurityException e) {
			//@formatter:off
			ImmutableList.of("DDD was unable to hook into Techguns for compat.",
					"DDD will continue loading as normal, but it is encouraged to report this error! DDD will inform on world load as well! See error below!",
					e.getClass().getSimpleName(),
					e.getLocalizedMessage()).forEach(DistinctDamageDescriptions::fatal);
			//@formatter:on
			DistinctDamageDescriptions.logStackTrace(e, DistinctDamageDescriptions::fatal);
			this.handlerToRegister = new Handler() {
				@SubscribeEvent
				public void onJoinWorld(PlayerLoggedInEvent evt) {
					if(evt.player.world.isRemote) {
						return;
					}
					evt.player.sendMessage(new TextComponentString("DDD's Techguns Compat failed to load successfully! Please check the logs for the error and report it!").setStyle(new Style().setColor(TextFormatting.RED)));
				}
			};
			return false;
		}
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		DDDCombatCalculations.registerCancelCalculationInjector((currentlyCanceled, phase, defender, src, amount) -> isDummyDamage(src, amount));
		return IModIntegration.super.init(evt);
	}
	
	static boolean isDummyDamage(DamageSource src, float amount) {
		return src.damageType.equals(TechgunsConsts.TG_KNOCKBACK) && amount <= TechgunsConsts.DUMMY_KNOCKBACK_THRESHOLD;
	}
	
	protected static final class TechgunsCacluationHandler extends Handler {
		@SuppressWarnings("static-method")
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onLivingAttack(LivingAttackEvent evt) {
			DamageSource src = evt.getSource();
			if(TechgunsReflectionHelper.getInstance().isTGDamageSource(src)) {
				DDDCombatCalculations.onAttack(evt);
			}
		}
		
		@SuppressWarnings("static-method")
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onLivingHurt(LivingHurtEvent evt) {
			if(TechgunsReflectionHelper.getInstance().isNpcTGDamageSystem(evt.getEntityLiving())) {
				DDDCombatCalculations.onHurt(evt);
			}
		}
	}
}
