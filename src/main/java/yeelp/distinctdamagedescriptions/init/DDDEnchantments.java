package yeelp.distinctdamagedescriptions.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.enchantments.EnchantmentBruteForce;
import yeelp.distinctdamagedescriptions.enchantments.EnchantmentSlyStrike;

public class DDDEnchantments
{
	public static Enchantment slyStrike;
	public static Enchantment bruteForce;
	
	public static void init()
	{
		slyStrike = new EnchantmentSlyStrike();
		bruteForce = new EnchantmentBruteForce();
		
		ForgeRegistries.ENCHANTMENTS.registerAll(slyStrike, bruteForce);
	}
}
