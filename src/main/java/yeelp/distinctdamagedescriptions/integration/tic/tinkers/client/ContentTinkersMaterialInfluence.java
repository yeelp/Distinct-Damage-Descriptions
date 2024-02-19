package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tools.IToolPart;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.tic.ContentMaterialInfluence;
import yeelp.distinctdamagedescriptions.integration.tic.TiCBookTranslator;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

public final class ContentTinkersMaterialInfluence extends ContentMaterialInfluence {

	public ContentTinkersMaterialInfluence() {
		super();
	}

	public ContentTinkersMaterialInfluence(PageData parent, Material material) {
		super(parent, material);
	}

	@Override
	protected BasicTranslator getTranslator() {
		return TiCBookTranslator.TINKERS.getTranslator();
	}

	@Override
	protected Set<? extends IToolPart> getRegistry() {
		return TinkerRegistry.getToolParts();
	}

	@Override
	protected boolean isValidPart(IToolPart part) {
		return part.hasUseForStat(MaterialTypes.HEAD);
	}

	@Override
	protected Iterable<TextData> getAdditionalTextData() {
		TextData bias = new TextData(this.getTranslator().translate("materialInfluenceBias", getFormattedBias(this.getMaterial())));
		bias.bold = true;
		bias.dropshadow = true;
		return ImmutableList.of(bias, TextData.LINEBREAK, TextData.LINEBREAK);
	}

	@Override
	protected Map<DDDDamageType, Float> getWeights() {
		return getBias(this.getMaterial()).getPreferredMapCopy();
	}

	private static final DistributionBias getBias(Material m) {
		return TiCConfigurations.toolMaterialBias.getOrFallbackToDefault(m.identifier);
	}

	private static final String getFormattedBias(Material m) {
		return FORMATTER.format(getBias(m).getBias() / 100).replace('%', ' ').trim();
	}

}
