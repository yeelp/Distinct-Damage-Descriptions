package yeelp.distinctdamagedescriptions.integration.tic.conarm;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;

import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.ArmorMaterials;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.client.DDDConarmBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait.DamageHandler;

public final class DDDConarmIntegration extends DDDTiCIntegration {

	@Override
	public boolean init(FMLInitializationEvent evt) {
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
}
