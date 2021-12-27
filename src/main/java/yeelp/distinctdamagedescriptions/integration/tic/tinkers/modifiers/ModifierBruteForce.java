package yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;

public class ModifierBruteForce extends ModifierTrait {

	private static final int max = 5, countPerLevel = 10;

	public ModifierBruteForce() {
		super("bruteforce", 0xff4800, max, countPerLevel);
		this.addAspects(ModifierAspect.weaponOnly);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment) {
		return enchantment != DDDEnchantments.slyStrike;
	}

	@Override
	public boolean canApplyTogether(IToolMod otherModifier) {
		return !(otherModifier instanceof ModifierSlyStrike);
	}

	@Override
	public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
		for(int level = ToolBuilder.getEnchantmentLevel(rootCompound, DDDEnchantments.bruteForce); level++ < ModifierNBT.readInteger(modifierTag).current / 10; ToolBuilder.addEnchantment(rootCompound, DDDEnchantments.bruteForce));
	}
}
