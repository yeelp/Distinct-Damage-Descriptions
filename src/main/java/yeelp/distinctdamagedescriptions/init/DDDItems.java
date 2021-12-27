package yeelp.distinctdamagedescriptions.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.items.DDDDiscItem;

public class DDDItems {

	public static DDDDiscItem disc;

	public static void init() {
		disc = new DDDDiscItem();
		ForgeRegistries.ITEMS.register(disc);
	}

	public static void initRenders() {
		ModelLoader.setCustomModelResourceLocation(disc, 0, new ModelResourceLocation(disc.getRegistryName(), "inventory"));
	}
}
