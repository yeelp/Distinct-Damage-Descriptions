package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import com.google.common.base.Functions;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.util.lib.MobResistanceCategories;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class TooltipFormatterUtilities {
	
	public static Optional<ResourceLocation> getResourceLocationFromSpawnEgg(ItemStack stack) {
		return Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(stack));
	}

	public static Optional<MobResistanceCategories> getMobResistancesIfConfigured(@Nonnull ResourceLocation loc) {
		return getCapabilityFromConfigurationIfExists(DDDConfigurations.mobResists, loc);
	}

	public static Optional<IDamageDistribution> getMobDamageIfConfigured(@Nonnull ResourceLocation loc) {
		return getCapabilityFromConfigurationIfExists(DDDConfigurations.mobDamage, loc);
	}

	private static <Cap> Optional<Cap> getCapabilityFromConfigurationIfExists(IDDDConfiguration<Cap> config, @Nonnull ResourceLocation loc) {
		return Optional.of(loc).map(Functions.toStringFunction()).filter(config::configured).map(config::get);
	}
}
