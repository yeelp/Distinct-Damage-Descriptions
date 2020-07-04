package yeelp.distinctdamagedescriptions.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ArmorResistancesProvider
{
	@CapabilityInject(IArmorResistances.class)
	public static Capability<IArmorResistances> armorResist = null;
	
	private IArmorResistances instance = armorResist.getDefaultInstance();
	
	public static IArmorResistances getArmorResiatances(ItemStack stack)
	{
		return stack.getCapability(armorResist, null);
	}
}
