package yeelp.distinctdamagedescriptions.integration;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.ThaumcraftMixinProvider;
import yeelp.distinctdamagedescriptions.integration.util.IOptionalMixinProvider;

public final class OptionalMixinKernel {

	private OptionalMixinKernel() {
		// nothing
	}

	private static final Map<String, Supplier<IOptionalMixinProvider>> PROVIDERS = Maps.newHashMap();
	private static final Set<String> MIXINS_ENQUEUED = Sets.newHashSet();
	private static boolean loaded = false;

	static {
		PROVIDERS.put(IntegrationIds.THAUMCRAFT_ID, () -> new ThaumcraftMixinProvider());
	}

	public static void enqueueMixins() {
		try {
			PROVIDERS.entrySet().stream().forEach((entry) -> {
				IOptionalMixinProvider provider = entry.getValue().get();
				fermiumbooter.FermiumRegistryAPI.enqueueMixin(true, String.format("mixin.ddd.%s.json", provider.getModID()), () -> Loader.instance().getModList().stream().map(ModContainer::getModId).filter(provider.getModID()::equals).findFirst().isPresent());
				MIXINS_ENQUEUED.add(provider.getModID());
			});
			loaded = true;
		}
		catch(NoClassDefFoundError e) {
			return;
		}
	}
	
	public static boolean wereMixinsEnqueued(String modid) {
		return MIXINS_ENQUEUED.contains(modid);
	}
	
	public static boolean wasFermiumBooterPresent() {
		return loaded;
	}
}
