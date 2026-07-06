package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import c4.conarm.lib.armor.ArmorPart;
import c4.conarm.lib.materials.ArmorMaterialType;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tinkering.IMaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ArmorDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;

import java.util.Optional;

public final class ConarmPlatesFormatter extends ArmorDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static ConarmPlatesFormatter instance;

	private ConarmPlatesFormatter() {
		super(ConarmPlatesFormatter::getArmorDistribution, "armorresistances");

	}

	public static ConarmPlatesFormatter getInstance() {
		return instance == null ? instance = new ConarmPlatesFormatter() : instance;
	}

	private static Optional<IArmorDistribution> getArmorDistribution(ItemStack stack) {
		if(stack.getItem() instanceof ArmorPart) {
			return Optional.of(TiCConfigurations.armorMaterialDist.getOrFallbackToDefault(((IMaterialItem) stack.getItem()).getMaterialID(stack)));
		}
		return Optional.empty();
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ArmorPart && ((IToolPart) t.getItem()).hasUseForStat(ArmorMaterialType.PLATES);
	}

	@Override
	public IconAggregator getIconAggregator() {
		return ConarmPlatesIconAggregator.getInstance();
	}
	
	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return super.getNumberFormattingStrategy().equals(DDDNumberFormatter.PLAIN) ? DDDNumberFormatter.PERCENT : super.getNumberFormattingStrategy();
	}
	
	protected static final class ConarmPlatesIconAggregator extends ArmorDistributionIconAggregator {

		private static ConarmPlatesIconAggregator instance;

		@SuppressWarnings("synthetic-access")
        private ConarmPlatesIconAggregator() {
			super(ConarmPlatesFormatter.getInstance(), ConarmPlatesFormatter::getArmorDistribution);
		}

		public static ConarmPlatesIconAggregator getInstance() {
			return instance == null ? instance = new ConarmPlatesFormatter.ConarmPlatesIconAggregator() : instance;
		}
		
		@Override
		protected boolean supportsAggregatingType(IconAggregatingType type) {
			return type != IconAggregatingType.PLAIN;
		}

	}
}
