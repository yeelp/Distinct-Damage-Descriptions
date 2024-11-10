package yeelp.distinctdamagedescriptions.capability.distributors;

import java.util.Set;

import com.google.common.collect.Sets;

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
	private static final Set<String> SIMPLE_CLASSES = Sets.newHashSet();
	private static final Set<String> QUALIFIED_CLASSES = Sets.newHashSet();
	

	private ShieldDistributionCapabilityDistributor() {
		super(LOC, () -> !ModConfig.compat.definedItemsOnly);
	}

	@Override
	protected ShieldDistribution generateCapability(ItemStack t, ResourceLocation key) {
		return ConfigGenerator.getOrGenerateShieldDistribution(t.getItem(), t);
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		if(t.getItem() instanceof ItemShield) {
			return true;
		}
		//maybe not the most efficient way to store and check this, but this might be more flexible later if it needs to be changed.
		Class<?> clazz = t.getItem().getClass();
		if(SIMPLE_CLASSES.contains(clazz.getSimpleName())) {
			return true;
		}
		return QUALIFIED_CLASSES.contains(clazz.getName());
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
	
	public static final void addSimpleClassNameAsShield(String s) {
		SIMPLE_CLASSES.add(s);
	}
	
	public static final void addFullyQualifiedClassNameAsShield(String s) {
		QUALIFIED_CLASSES.add(s);
	}
}
