package yeelp.distinctdamagedescriptions.capability.providers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

public class ShieldDistributionProvider {
	@CapabilityInject(ShieldDistribution.class)
	public static Capability<ShieldDistribution> shieldDist = null;

	ShieldDistribution instance = shieldDist.getDefaultInstance();

	public static ShieldDistribution getShieldDistribution(ItemStack stack) {
		return stack.getCapability(shieldDist, null);
	}
}
