package yeelp.distinctdamagedescriptions.capability.providers;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;

public class ArmorDistributionProvider {
	@CapabilityInject(IArmorDistribution.class)
	public static Capability<IArmorDistribution> armorResist = null;

	private IArmorDistribution instance = armorResist.getDefaultInstance();

	@Nullable
	public static IArmorDistribution getArmorResistances(ItemStack stack) {
		return stack.getCapability(armorResist, null);
	}
}
