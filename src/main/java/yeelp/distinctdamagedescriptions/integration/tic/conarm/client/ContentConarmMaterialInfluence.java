package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
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
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.tic.ContentMaterialInfluence;
import yeelp.distinctdamagedescriptions.integration.tic.TiCBookTranslator;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionNumberFormat;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;

public final class ContentConarmMaterialInfluence extends ContentMaterialInfluence {

	public ContentConarmMaterialInfluence() {
		super();
	}

	public ContentConarmMaterialInfluence(PageData parent, Material material) {
		super(parent, material);
	}

	@Override
	protected BasicTranslator getTranslator() {
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
		return dist.getCategories().stream().filter(Predicates.and(Predicates.not(DDDDamageType::isHidden), (type) -> getPredicate().test(dist.getWeight(type)))).collect(DDDBaseMap.typesToDDDBaseMap(() -> dist.getWeight(DDDBuiltInDamageType.UNKNOWN), dist::getWeight));
	}

	@Override
	protected ObjectFormatter<Float> getAppropriateFormatter() {
		return ModConfig.client.armorFormat == ArmorDistributionNumberFormat.PLAIN ? ArmorDistributionNumberFormat.PERCENT : ModConfig.client.armorFormat;
	}
	
	private static Predicate<Float> getPredicate() {
		return ModConfig.client.armorFormat;
	}

	@Override
	protected DDDTooltipColourScheme getTooltipScheme() {
		return ModConfig.client.armorColourScheme;
	}

}
