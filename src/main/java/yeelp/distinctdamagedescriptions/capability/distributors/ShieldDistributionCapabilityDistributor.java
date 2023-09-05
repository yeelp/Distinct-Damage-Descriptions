package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;

public final class ShieldDistributionCapabilityDistributor extends AbstractCapabilityDistributorGeneratable<ItemStack, ShieldDistribution, ShieldDistribution> {
	public static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "shieldEffectiveness");
	private static ShieldDistributionCapabilityDistributor instance;

	private ShieldDistributionCapabilityDistributor() {
		super(LOC, () -> !ModConfig.compat.definedItemsOnly);
	}

	@Override
	protected ShieldDistribution generateCapability(ItemStack t, ResourceLocation key) {
		return ConfigGenerator.getOrGenerateShieldDistribution((ItemShield) t.getItem(), t);
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ItemShield;
	}

	@Override
	protected IDDDConfiguration<ShieldDistribution> getConfig() {
		return DDDConfigurations.shields;
	}

	@Override
	protected ShieldDistribution createCapability(ShieldDistribution configResult) {
		return configResult;
	}

	public static final ShieldDistributionCapabilityDistributor getInstance() {
		return instance == null ? instance = new ShieldDistributionCapabilityDistributor() : instance;
	}
}
