package yeelp.distinctdamagedescriptions.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.enchantments.EnchantmentBruteForce;
import yeelp.distinctdamagedescriptions.enchantments.EnchantmentSlyStrike;

public class DDDEnchantments {
	public static Enchantment slyStrike;
	public static Enchantment bruteForce;

	public static void init() {
		slyStrike = new EnchantmentSlyStrike();
		bruteForce = new EnchantmentBruteForce();
		if(ModConfig.enchants.enableBruteForce) {
			ForgeRegistries.ENCHANTMENTS.register(bruteForce);
		}
		if(ModConfig.enchants.enableSlyStrike) {
			ForgeRegistries.ENCHANTMENTS.register(slyStrike);
		}
	}
}
