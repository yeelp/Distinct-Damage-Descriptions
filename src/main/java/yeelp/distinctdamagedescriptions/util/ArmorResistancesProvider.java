package yeelp.distinctdamagedescriptions.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ArmorResistancesProvider
{
	@CapabilityInject(IArmorResistances.class)
	public static Capability<IArmorResistances> armorResist = null;
	
	private IArmorResistances instance = armorResist.getDefaultInstance();
	
	@Nullable
	public static IArmorResistances getArmorResistances(ItemStack stack)
	{
		return stack.getCapability(armorResist, null);
	}
}
