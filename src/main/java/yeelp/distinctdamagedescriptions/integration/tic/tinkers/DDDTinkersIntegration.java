package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatch.ItemCombination;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent.MeltingRegisterEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.distributors.TinkerProjectileCapabilityDistributor;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.distributors.TinkerToolCapabilityDistributor;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.DDDTinkersBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.TinkerToolPartFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierBruteForce;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierSlyStrike;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class DDDTinkersIntegration extends DDDTiCIntegration {
	private static final Field TINKER_STENCIL_REGISTRY;
	private static final Class<?> TINKER_REGISTRY;
	public static final Modifier slyStrike = new ModifierSlyStrike(), bruteForce = new ModifierBruteForce();
	
	protected static final class MeltingListener extends Handler {

		private static MeltingListener instance;
		private boolean isFixing = false;

		private final List<MeltingRecipe> recipesToFix;

		private MeltingListener() {
			this.recipesToFix = new LinkedList<MeltingRecipe>();
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onMeltingRegister(MeltingRegisterEvent evt) {
			// The only NBT-sensitive recipe we need to edit. Leave the others.
			if(!this.isFixing && evt.getRecipe().input instanceof ItemCombination) {
				this.recipesToFix.add(evt.getRecipe());
			}
		}

		public void fixRecipes() {
			this.isFixing = true;
			this.recipesToFix.forEach((mr) -> TinkerRegistry.registerMelting(new MeltingRecipe(new RecipeMatch.ItemCombination(mr.input.amountMatched, mr.input.getInputs().get(0).copy()), mr.output, mr.temperature)));
		}

		static MeltingListener getInstance() {
			return instance == null ? instance = new DDDTinkersIntegration.MeltingListener() : instance;
		}
	}

	static {
		try {
			TINKER_REGISTRY = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			TINKER_STENCIL_REGISTRY = TINKER_REGISTRY.getDeclaredField("stencilTableCrafting");
			TINKER_STENCIL_REGISTRY.setAccessible(true);
		}
		catch(ClassNotFoundException e) {
			DistinctDamageDescriptions.fatal("Could not find TinkerRegistry target class!");
			throw new RuntimeException(e);
		}
		catch(NoSuchFieldException e) {
			DistinctDamageDescriptions.fatal("Could not find stencilTableCrafting target field!");
			throw new RuntimeException(e);
		}
		catch(SecurityException e) {
			DistinctDamageDescriptions.fatal("Some security problem occured initializing Tinker's integration.");
			throw new RuntimeException(e);
		}
		MeltingListener.getInstance().register();
	}

	
	@Override
	protected boolean doSpecificPreInit(FMLPreInitializationEvent evt) {
		return true;
	}

	@Override
	public boolean doSpecificInit(FMLInitializationEvent evt) {
		slyStrike.addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.COMPASS), new ItemStack(Items.ENDER_EYE)));
		bruteForce.addItem(Items.FIREWORK_CHARGE);
		DDDCapabilityDistributors.addProjCap(TinkerProjectileCapabilityDistributor.getInstance());
		return true;
	}

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		if(TConstruct.pulseManager.isPulseLoaded(TinkerRangedWeapons.PulseId)) {
			for(Item i : ImmutableList.of(TinkerRangedWeapons.arrow, TinkerRangedWeapons.bolt, TinkerRangedWeapons.shuriken)) {
				String s = ForgeRegistries.ITEMS.getKey(i).toString();
				DDDConfigurations.projectiles.registerItemProjectilePair(s, s);
				DDDConfigurations.projectiles.put(s, new DamageDistribution());
			}
		}

		// Add DDD specific information to Tinker's registry.
		try {
			@SuppressWarnings("unchecked")
			List<ItemStack> stencilRegistry = (List<ItemStack>) TINKER_STENCIL_REGISTRY.get(null);
			stencilRegistry.replaceAll(ItemStack::copy);
		}
		catch(IllegalAccessException | IllegalArgumentException e) {
			DistinctDamageDescriptions.fatal("Could not get underlying stencil registry!");
			throw new RuntimeException(e);
		}
		MeltingListener.getInstance().fixRecipes();
		DDDRegistries.trackers.register(new BattleSignTracker());
		DDDRegistries.distributions.register(new BattleSignCounterAttackDistribution());
		return true;
	}

	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.TCONSTUCT_TITLE;
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.TCONSTRUCT_ID;
	}

	@Override
	protected DDDBookTransformer getBookTransformer() {
		return new DDDTinkersBookTransformer();
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistribution>> getItemDistributors() {
		return ImmutableList.of(TinkerToolCapabilityDistributor.getInstance());
	}

	@Override
	protected Iterable<IModCompatTooltipFormatter<ItemStack>> getFormatters() {
		return ImmutableList.of(TinkerToolPartFormatter.getInstance());
	}

	@Override
	protected void registerCapabilities() {
		TinkerDamageDistribution.register();
	}
}
