package yeelp.distinctdamagedescriptions.mixin;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import yeelp.distinctdamagedescriptions.integration.OptionalMixinKernel;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("DistinctDamageDescriptions-MixinLoader")
@IFMLLoadingPlugin.SortingIndex(-5000)
public final class DDDMixinLoader implements IFMLLoadingPlugin {
	
	private static final String MIXIN_FILE = "mixin.ddd.json";

	public DDDMixinLoader() {
		MixinBootstrap.init();
		MixinExtrasBootstrap.init();
		try {
			//Try Fermium first. If it exists, load optional mixins with it. Otherwise, fallback to loading mixins normally.
			//We do it this way because we should be able to catch the NoClassDefFoundError as an indication that Fermium isn't present
			//Can't wrap regular Mixin.addConfiguration call in try block because it won't throw an exception we can catch if it fails.
			fermiumbooter.FermiumRegistryAPI.enqueueMixin(false, MIXIN_FILE);
			OptionalMixinKernel.enqueueMixins();
		}
		catch(NoClassDefFoundError e) {
			//Fermium not here, try fallback
			Mixins.addConfiguration(MIXIN_FILE);			
		}
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
