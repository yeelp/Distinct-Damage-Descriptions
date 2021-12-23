package yeelp.distinctdamagedescriptions.integration.lycanites.client;

import java.util.Optional;

import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.item.ItemCustomSpawnEgg;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

final class LycanitesFormatterUtilities {
	
	private LycanitesFormatterUtilities() {
		throw new RuntimeException("Not to be instatiated.");
	}
	
	static Optional<ResourceLocation> getCreatureResourceLocation(ItemStack stack) {
		return Optional.ofNullable(((ItemCustomSpawnEgg) stack.getItem()).getCreatureInfo(stack)).map(CreatureInfo::getResourceLocation);
	}
}
