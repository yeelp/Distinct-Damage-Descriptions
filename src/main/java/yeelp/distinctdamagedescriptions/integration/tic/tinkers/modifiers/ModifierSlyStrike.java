package yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;

public class ModifierSlyStrike extends ToolModifier {

	public ModifierSlyStrike() {
		super("slystrike", 0x707070);
		this.addAspects(ModifierAspect.weaponOnly);
	}

	@Override
	public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
		if(ToolBuilder.getEnchantmentLevel(rootCompound, DDDEnchantments.slyStrike) == 0) {
			ToolBuilder.addEnchantment(rootCompound, DDDEnchantments.slyStrike);
		}
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment) {
		return enchantment != DDDEnchantments.bruteForce;
	}

	@Override
	public boolean canApplyTogether(IToolMod otherModifier) {
		return !(otherModifier instanceof ModifierBruteForce);
	}
}
