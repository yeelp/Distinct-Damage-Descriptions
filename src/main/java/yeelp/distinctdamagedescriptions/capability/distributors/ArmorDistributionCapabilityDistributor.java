package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;

public final class ArmorDistributionCapabilityDistributor extends AbstractCapabilityDistributorGeneratable<ItemStack, IArmorDistribution, IArmorDistribution> {
	public static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "armorResists");
	private static ArmorDistributionCapabilityDistributor instance;

	private ArmorDistributionCapabilityDistributor() {
		super(LOC, () -> ModConfig.compat.definedItemsOnly);
	}

	@Override
	protected IArmorDistribution generateCapability(ItemStack t, ResourceLocation key) {
		return ConfigGenerator.getOrGenerateArmorResistances((ItemArmor) t.getItem(), t);
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ItemArmor;
	}

	@Override
	protected IDDDConfiguration<IArmorDistribution> getConfig() {
		return DDDConfigurations.armors;
	}

	@Override
	protected IArmorDistribution createCapability(IArmorDistribution configResult) {
		return configResult;
	}

	public static final ArmorDistributionCapabilityDistributor getInstance() {
		return instance == null ? instance = new ArmorDistributionCapabilityDistributor() : instance;
	}
}
