package yeelp.distinctdamagedescriptions.integration.tic.conarm;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;

import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.ConarmBlankArmorDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.distributors.ModCompatCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.TinkersCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.impl.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ConarmArmorDistribution;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.client.DDDConarmBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait.DamageHandler;

public final class DDDConarmIntegration extends DDDTiCIntegration {

	@Override
	public boolean doSpecificInit(FMLInitializationEvent evt) {
		String location;
		switch(ModConfig.compat.conarm.traitLocation) {
			case CORE:
				location = ArmorMaterialType.CORE;
				break;
			default:
				location  = ArmorMaterialType.PLATES;
				break;
		}
		Map<String, DDDImmunityTrait> traits = Arrays.stream(DDDBuiltInDamageType.BUILT_IN_TYPES).map(DDDImmunityTrait::new).collect(Collectors.toMap(Functions.compose(DDDDamageType::getTypeName, DDDImmunityTrait::getType), Function.identity()));
		Arrays.stream(ModConfig.compat.conarm.armorImmunities).map((s) -> s.split(";")).forEach((a) -> {
			Material mat = TinkerRegistry.getMaterial(a[0]);
			Arrays.stream(a[1].split(",")).map(Functions.compose(traits::get, String::trim)).forEach((t) -> ArmorMaterials.addArmorTrait(mat, t, location));
		});
		return true;
	}

	@Override
	public String getModID() {
		return ModConsts.CONARM_ID;
	}

	@Override
	protected DDDBookTransformer getBookTransformer() {
		return new DDDConarmBookTransformer();
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new DamageHandler());
	}

	@Override
	protected Iterable<ModCompatCapabilityDistributor<ItemStack, ? extends AbstractTinkersDistribution<? extends IDistribution, ?>>> getItemDistributors() {
		return ImmutableList.of(TinkersCapabilityDistributor.Armor.getInstance());
	}

	@Override
	protected void addOtherDistributors() {
		DDDCapabilityDistributors.addItemDistributor(ConarmBlankArmorDistributionCapabilityDistributor.getInstance());
	}

	@Override
	protected Iterable<IModCompatTooltipFormatter<ItemStack>> getFormatters() {
		return ImmutableList.of();
	}

	@Override
	protected void registerCapabilities() {
		ConarmArmorDistribution.register();
	}
}
