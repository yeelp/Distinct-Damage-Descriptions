package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import com.google.common.collect.ImmutableList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
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
	public static final Modifier slyStrike = new ModifierSlyStrike(), bruteForce = new ModifierBruteForce();
	
	@Override
	public boolean doSpecificInit(FMLInitializationEvent evt) {
		slyStrike.addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.COMPASS), new ItemStack(Items.ENDER_EYE)));
		bruteForce.addItem(Items.FIREWORK_CHARGE);
		DDDCapabilityDistributors.addProjCap(TinkerProjectileCapabilityDistributor.getInstance());
		return true;
	}
	

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		for(Item i : ImmutableList.of(TinkerRangedWeapons.arrow, TinkerRangedWeapons.bolt, TinkerRangedWeapons.shuriken)) {
			String s = ForgeRegistries.ITEMS.getKey(i).toString();
			DDDConfigurations.projectiles.registerItemProjectilePair(s, s);
			DDDConfigurations.projectiles.put(s, new DamageDistribution());
		}
		DDDRegistries.trackers.register(new BattleSignTracker());
		DDDRegistries.distributions.register(new BattleSignCounterAttackDistribution());
		return true;
	}

	@Override
	public String getModID() {
		return ModConsts.TCONSTRUCT_ID;
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
