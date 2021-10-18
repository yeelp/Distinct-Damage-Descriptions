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
import slimeknights.tconstruct.tools.melee.TinkerMeleeWeapons;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.distributors.ModCompatCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.TinkerBlankShieldDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.TinkersCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.impl.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.TinkerToolDistribution.Shield;
import yeelp.distinctdamagedescriptions.capability.impl.TinkerToolDistribution.Tool;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.DDDTinkersBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.TinkerProjectileDamageFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.TinkerShieldFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.TinkerToolDamageFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.TinkerToolPartFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierBruteForce;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierSlyStrike;

public class DDDTinkersIntegration extends DDDTiCIntegration {
	public static final Modifier slyStrike = new ModifierSlyStrike(), bruteForce = new ModifierBruteForce();
	
	@Override
	public boolean doSpecificInit(FMLInitializationEvent evt) {
		slyStrike.addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.COMPASS), new ItemStack(Items.ENDER_EYE)));
		bruteForce.addItem(Items.FIREWORK_CHARGE);
		return true;
	}
	

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		DDDConfigurations.shields.put(ForgeRegistries.ITEMS.getKey(TinkerMeleeWeapons.battleSign).toString(), new ShieldDistribution());
		for(Item i : ImmutableList.of(TinkerRangedWeapons.arrow, TinkerRangedWeapons.bolt, TinkerRangedWeapons.shuriken)) {
			String s = ForgeRegistries.ITEMS.getKey(i).toString();
			DDDConfigurations.projectiles.registerItemProjectilePair(s, s);
			DDDConfigurations.projectiles.put(s, new DamageDistribution());
		}
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
		return ImmutableList.of(new TinkerProjectileHandler());
	}

	@Override
	protected Iterable<ModCompatCapabilityDistributor<ItemStack, ? extends AbstractTinkersDistribution<? extends IDistribution, ?>>> getItemDistributors() {
		return ImmutableList.of(TinkersCapabilityDistributor.Tool.Damage.getInstance(), TinkersCapabilityDistributor.Tool.Shield.getInstance());
	}

	@Override
	protected void addOtherDistributors() {
		DDDCapabilityDistributors.addItemDistributor(TinkerBlankShieldDistributionCapabilityDistributor.getInstance());
	}


	@Override
	protected Iterable<IModCompatTooltipFormatter<ItemStack>> getFormatters() {
		return ImmutableList.of(TinkerToolDamageFormatter.getInstance(), TinkerProjectileDamageFormatter.getInstance(), TinkerShieldFormatter.getInstance(), TinkerToolPartFormatter.getInstance());
	}


	@Override
	protected void registerCapabilities() {
		Tool.register();
		Shield.register();
	}
}
