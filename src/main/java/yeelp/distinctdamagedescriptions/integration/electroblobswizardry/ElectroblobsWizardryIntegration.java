package yeelp.distinctdamagedescriptions.integration.electroblobswizardry;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.util.MagicDamage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.capability.WizardryLinkedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client.DDDWizardryBookAddon;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client.SpellCastInfoCallback;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client.SpellDistributionItemFormatter;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client.SpellInfoCallback;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client.SpellcastingItemDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist.UndeathBurningDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist.WizardrySlimeDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist.WizardrySpellDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public class ElectroblobsWizardryIntegration implements IModIntegration {
	
	private static final Set<String> CACHED_INVALID_TYPES = Sets.newHashSet(); 

	@Override
	public String getModTitle() {
		return IntegrationTitles.WIZARDRY_NAME;
	}

	@Override
	public String getModID() {
		return IntegrationIds.WIZARDRY_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new Handler() {
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public void onSpellCast(SpellCastEvent.Post evt) {
				DeveloperModeKernel.fireCallbacks(evt);
			}
		});
	}
	
	@Override
	public boolean preInit(FMLPreInitializationEvent evt) {
		DeveloperModeKernel.registerCallback(LivingAttackEvent.class, new SpellInfoCallback());
		DeveloperModeKernel.<SpellCastEvent.Post>registerCallback(SpellCastEvent.Post.class, new SpellCastInfoCallback());
		return IModIntegration.super.preInit(evt);
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		if(evt.getSide().equals(Side.CLIENT)) {
			DDDWizardryBookAddon.init();
			//Much like lycanites integration, the cast here avoids BootstrapMethodError, which is important!
			Stream.of((IModCompatTooltipFormatter<ItemStack>) SpellDistributionItemFormatter.getInstance(), SpellcastingItemDamageDistributionFormatter.getInstance()).forEach(TooltipDistributor::registerModCompat);
		}
		DDDRegistries.trackers.register(new UndeathBurningDistribution.UndeathBurningTracker());
		Stream.of((DDDPredefinedDistribution) new WizardrySlimeDistribution(), new UndeathBurningDistribution()).forEach(DDDRegistries.distributions::register);
		return IModIntegration.super.init(evt);
	}
	
	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		WizardryConfigurations.minionCapabilityReference.forEach((entry) -> {
			copyCapability(DDDConfigurations.mobDamage, entry, "damage");
			copyCapability(DDDConfigurations.mobResists, entry, "resistances");
		});
		WizardryConfigurations.spellTypeDist.forEach((entry) -> getDamageType(entry.getKey()).ifPresent((type) -> DDDRegistries.distributions.register(new WizardrySpellDistribution(type, entry.getValue()))));
		WizardryConfigurations.linkedThrowables.forEach((entry) -> {
			String damageTypeName = entry.getValue();
			String id = entry.getKey();
			if(WizardryConfigurations.spellTypeDist.configured(damageTypeName.toLowerCase())) {
				getDamageType(damageTypeName).ifPresent((type) -> {
					DDDConfigurations.projectiles.registerItemProjectilePair(id, id);
					DDDConfigurations.projectiles.put(id, new WizardryLinkedDamageDistribution(type));					
				});
			}
			else {
				DistinctDamageDescriptions.warn(String.format("The Wizardry Damage Type %s has no associated Damage Distribution in the config! Ignoring!", damageTypeName));
			}
		});
		return IModIntegration.super.postInit(evt);
	}
	
	private static Optional<DamageType> getDamageType(String name) {
		if(CACHED_INVALID_TYPES.contains(name)) {
			return Optional.empty();
		}
		try {
			return Optional.of(DamageType.valueOf(name.toUpperCase()));
		}
		catch(IllegalArgumentException e) {
			DistinctDamageDescriptions.err("The Wizardry Damage Type: " + name + " doesn't exist! Check spelling!");
			return Optional.empty();
		}
	}
	
	private static <C> void copyCapability(IDDDConfiguration<C> config, IDDDConfiguration.ConfigEntry<String> entry, String type) {
		String key = entry.getKey();
		String value = entry.getValue();
		if(!config.configured(key)) {
			config.put(key, config.getOrFallbackToDefault(value));
			DebugLib.outputFormattedDebug("Minion %s copied %s of %s", key, type, value);
		}
	}

}
