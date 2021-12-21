package yeelp.distinctdamagedescriptions.integration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.base.Predicates;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.events.CTEventHandler;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla;
import yeelp.distinctdamagedescriptions.integration.lycanites.LycanitesIntegration;
import yeelp.distinctdamagedescriptions.integration.tetra.TetraIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.DDDConarmIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.DDDTinkersIntegration;

public final class ModIntegrationKernel {
	/**
	 * Lists of mods that will try to load on DDD startup
	 */
	public static final Map<String, Supplier<IModIntegration>> integratableMods = new HashMap<String, Supplier<IModIntegration>>();
	private static final Set<String> foundMods = new HashSet<String>();
	private static final List<IModIntegration> loadedMods = new LinkedList<IModIntegration>();

	static {
		// Would love to do function notation like ClassName::new for the Supplier but
		// that isn't actually the same as using lambdas and those minor differences CAN
		// cause problems here with optional dependencies so ONLY use lambdas here!!
		integratableMods.put(ModConsts.CRAFTTWEAKER_ID, () -> new CTEventHandler());
		integratableMods.put(ModConsts.HWYLA_ID, () -> new Hwyla());
		integratableMods.put(ModConsts.TCONSTRUCT_ID, () -> new DDDTinkersIntegration());
		integratableMods.put(ModConsts.CONARM_ID, () -> new DDDConarmIntegration());
		integratableMods.put(ModConsts.LYCANITES_ID, () -> new LycanitesIntegration());
		integratableMods.put(ModConsts.TETRA_ID, () -> new TetraIntegration());
	}

	/**
	 * Load
	 */
	public static final void load() {
		integratableMods.entrySet().stream().filter(Predicates.compose(Loader::isModLoaded, Entry::getKey)).forEach((e) -> {
			DistinctDamageDescriptions.info("Distinct Damage Descriptions found " + e.getKey() + "!");
			foundMods.add(e.getKey());
			loadedMods.add(e.getValue().get());
		});
	}

	public static final void doPreInit(FMLPreInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::preInit, evt);
	}

	public static final void doInit(FMLInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::init, evt);
		loadedMods.stream().map(IModIntegration::getHandlers).forEach((i) -> i.forEach(Handler::register));
	}

	public static final void doPostInit(FMLPostInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::postInit, evt);
		if(foundMods.size() == loadedMods.size()) {
			DistinctDamageDescriptions.info("Mod integration loaded successfully! (Yay!)");
		}
		else {
			DistinctDamageDescriptions.warn("DDD failed to load integrations with the following mods:");
			loadedMods.stream().map(IModIntegration::getModID).filter(Predicates.not(foundMods::contains)).forEach(DistinctDamageDescriptions::warn);			
		}
	}

	private static final <U> void filterIfUnsuccessful(BiPredicate<IModIntegration, U> p, U u) {
		loadedMods.removeIf(partialApplyPredicate(u, p).negate());
	}

	private static final <T, U> Predicate<T> partialApplyPredicate(U u, BiPredicate<T, U> p) {
		return (t) -> p.test(t, u);
	}
}
