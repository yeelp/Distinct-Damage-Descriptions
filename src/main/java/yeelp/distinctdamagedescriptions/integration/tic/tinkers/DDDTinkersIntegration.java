package yeelp.distinctdamagedescriptions.integration.tic.tinkers;

import java.util.Collections;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.Modifier;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.client.DDDTinkersBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierBruteForce;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.modifiers.ModifierSlyStrike;

public class DDDTinkersIntegration extends DDDTiCIntegration {
	public static final Modifier slyStrike = new ModifierSlyStrike(), bruteForce = new ModifierBruteForce();
	@Override
	public boolean init(FMLInitializationEvent evt) {
		slyStrike.addRecipeMatch(new RecipeMatch.ItemCombination(1, new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.COMPASS), new ItemStack(Items.ENDER_EYE)));
		bruteForce.addItem(Items.FIREWORK_CHARGE);
		return true;
	}

	@Override
	public String getModID() {
		return ModConsts.TCONSTRUCT_ID;
	}

	@Override
	protected DDDBookTransformer getBookTransformer() {
		return new DDDTinkersBookTransformer();
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return Collections.emptyList();
	}
}
