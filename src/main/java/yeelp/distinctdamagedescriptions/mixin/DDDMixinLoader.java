package yeelp.distinctdamagedescriptions.mixin;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("DistinctDamageDescriptions-MixinLoader")
@IFMLLoadingPlugin.SortingIndex(-5000)
public final class DDDMixinLoader implements IFMLLoadingPlugin {

	public DDDMixinLoader() {
		MixinBootstrap.init();
		Mixins.addConfigurations("mixin.ddd.json");
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		return;
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
