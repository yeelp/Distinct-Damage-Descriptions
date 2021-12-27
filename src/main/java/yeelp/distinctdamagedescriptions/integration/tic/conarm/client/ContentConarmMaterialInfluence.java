package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import c4.conarm.lib.ArmoryRegistry;
import c4.conarm.lib.materials.ArmorMaterialType;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.ContentMaterialInfluence;
import yeelp.distinctdamagedescriptions.integration.tic.TiCBookTranslator;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.Translations.Translator;

public final class ContentConarmMaterialInfluence extends ContentMaterialInfluence {

	public ContentConarmMaterialInfluence() {
		super();
	}

	public ContentConarmMaterialInfluence(PageData parent, Material material) {
		super(parent, material);
	}

	@Override
	protected Translator getTranslator() {
		return TiCBookTranslator.CONARM.getTranslator();
	}

	@Override
	protected Set<? extends IToolPart> getRegistry() {
		return ArmoryRegistry.armorParts;
	}

	@Override
	protected boolean isValidPart(IToolPart part) {
		return part.hasUseForStat(ArmorMaterialType.PLATES);
	}

	@Override
	protected Iterable<TextData> getAdditionalTextData() {
		return ImmutableList.of();
	}

	@Override
	protected Map<DDDDamageType, Float> getWeights() {
		IArmorDistribution dist = TiCConfigurations.armorMaterialDist.getOrFallbackToDefault(this.getMaterial().identifier);
		return dist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> dist.getWeight(DDDBuiltInDamageType.UNKNOWN), dist::getWeight));
	}

}
