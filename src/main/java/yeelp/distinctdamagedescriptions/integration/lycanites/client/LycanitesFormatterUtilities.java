package yeelp.distinctdamagedescriptions.integration.lycanites.client;

import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.item.ItemCustomSpawnEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

final class LycanitesFormatterUtilities {

	private LycanitesFormatterUtilities() {
		throw new RuntimeException("Not to be instantiated.");
	}

	static Optional<ResourceLocation> getCreatureResourceLocation(ItemStack stack) {
		return Optional.ofNullable(((ItemCustomSpawnEgg) stack.getItem()).getCreatureInfo(stack)).map(CreatureInfo::getResourceLocation);
	}
}
