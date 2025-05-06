package yeelp.distinctdamagedescriptions.integration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.base.Predicates;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.baubles.BaublesIntegration;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.BetterSurvivalCompat;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.events.CTEventHandler;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.DDDCoTIntegration;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.ElectroblobsWizardryIntegration;
import yeelp.distinctdamagedescriptions.integration.fermiumbooter.FermiumBooterIntegration;
import yeelp.distinctdamagedescriptions.integration.firstaid.FirstAidIntegration;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla;
import yeelp.distinctdamagedescriptions.integration.lycanites.LycanitesIntegration;
import yeelp.distinctdamagedescriptions.integration.qualitytools.QualityToolsIntegration;
import yeelp.distinctdamagedescriptions.integration.spartanweaponry.SpartanWeaponryCompat;
import yeelp.distinctdamagedescriptions.integration.techguns.TechgunsCompat;
import yeelp.distinctdamagedescriptions.integration.tetra.TetraIntegration;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.ThaumcraftIntegration;
import yeelp.distinctdamagedescriptions.integration.thebewteenlands.TheBetweenlandsCompat;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.DDDConarmIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.DDDTinkersIntegration;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public final class ModIntegrationKernel {
	/**
	 * Lists of mods that will try to load on DDD startup
	 */
	public static final Map<String, Supplier<IModIntegration>> integratableMods = Maps.newHashMap();
	private static final BiMap<String, String> ID_NAME_CONVERTER = HashBiMap.create();
	private static final Set<String> foundMods = Sets.newHashSet();
	private static final List<IModIntegration> loadedMods = Lists.newArrayList();
	private static final Set<String> integrationLoaded = Sets.newHashSet();
	static {
		// Would love to do function notation like ClassName::new for the Supplier but
		// that isn't actually the same as using lambdas and those minor differences CAN
		// cause problems here with optional dependencies so ONLY use lambdas here!!
		integratableMods.put(ModConsts.IntegrationIds.CRAFTTWEAKER_ID, () -> new CTEventHandler());
		integratableMods.put(ModConsts.IntegrationIds.CONTENTTWEAKER_ID, () -> new DDDCoTIntegration());
		integratableMods.put(ModConsts.IntegrationIds.HWYLA_ID, () -> new Hwyla());
		integratableMods.put(ModConsts.IntegrationIds.TCONSTRUCT_ID, () -> new DDDTinkersIntegration());
		integratableMods.put(ModConsts.IntegrationIds.CONARM_ID, () -> new DDDConarmIntegration());
		integratableMods.put(ModConsts.IntegrationIds.LYCANITES_ID, () -> new LycanitesIntegration());
		integratableMods.put(ModConsts.IntegrationIds.TETRA_ID, () -> new TetraIntegration());
		integratableMods.put(ModConsts.IntegrationIds.SPARTAN_WEAPONRY_ID, () -> new SpartanWeaponryCompat());
		integratableMods.put(ModConsts.IntegrationIds.BETWEENLANDS_ID, () -> new TheBetweenlandsCompat());
		integratableMods.put(ModConsts.IntegrationIds.QUALITY_TOOLS_ID, () -> new QualityToolsIntegration());
		integratableMods.put(ModConsts.IntegrationIds.FIRST_AID_ID, () -> new FirstAidIntegration());
		integratableMods.put(ModConsts.IntegrationIds.BAUBLES_ID, () -> new BaublesIntegration());
		integratableMods.put(ModConsts.IntegrationIds.TECHGUNS_ID, () -> new TechgunsCompat());
		integratableMods.put(ModConsts.IntegrationIds.WIZARDRY_ID, () -> new ElectroblobsWizardryIntegration());
		integratableMods.put(ModConsts.IntegrationIds.FERMIUM_ID, () -> FermiumBooterIntegration.getInstance());
		integratableMods.put(ModConsts.IntegrationIds.THAUMCRAFT_ID, () -> new ThaumcraftIntegration());
		integratableMods.put(ModConsts.IntegrationIds.BETTER_SURVIVAL_ID, () -> new BetterSurvivalCompat());
		
		Iterator<String> ids = getStaticFieldValuesSortedByFieldName(ModConsts.IntegrationIds.class).iterator();
		getStaticFieldValuesSortedByFieldName(ModConsts.IntegrationTitles.class).forEach((s) -> ID_NAME_CONVERTER.put(ids.next(), s));
	}
	
	public static final String getTitleFromId(String id) {
		return ID_NAME_CONVERTER.get(id);
	}
	
	public static final String getIdFromTitle(String title) {
		return ID_NAME_CONVERTER.inverse().get(title);
	}

	/**
	 * Load
	 */
	public static final void load() {
		integratableMods.entrySet().stream().filter(Predicates.compose(Loader::isModLoaded, Entry::getKey)).forEach((e) -> {
			String id = e.getKey();
			DistinctDamageDescriptions.info(String.format("Distinct Damage Descriptions found %s (%s)!", id, getTitleFromId(id)));
			foundMods.add(id);
			loadedMods.add(integratableMods.get(id).get());
		});
	}

	public static final void doPreInit(FMLPreInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::preInit, evt);
	}
	
	public static final void doInitStart(FMLInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::initStart, evt);
	}

	public static final void doInit(FMLInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::init, evt);
		loadedMods.stream().map(IModIntegration::getHandlers).forEach((i) -> i.forEach(Handler::register));
	}

	public static final void doPostInit(FMLPostInitializationEvent evt) {
		filterIfUnsuccessful(IModIntegration::postInit, evt);
		loadedMods.stream().map(IModIntegration::getModID).filter(foundMods::contains).forEach(integrationLoaded::add);
		if(foundMods.size() == loadedMods.size()) {
			DistinctDamageDescriptions.info("Mod integration loaded successfully! (Yay!)");
		}
		else {
			DistinctDamageDescriptions.warn("DDD failed to load integrations with the following mods:");
			YMath.setDifference(foundMods, integrationLoaded).stream().map((s) -> String.format("%s (%s)", getTitleFromId(s), s)).forEach(DistinctDamageDescriptions::warn);
		}
		loadedMods.stream().forEach(IModIntegration::registerCrossModCompat);
	}

	private static final <U> void filterIfUnsuccessful(BiPredicate<IModIntegration, U> p, U u) {
		loadedMods.removeIf(partialApplyPredicate(u, p).negate());
	}

	private static final <T, U> Predicate<T> partialApplyPredicate(U u, BiPredicate<T, U> p) {
		return (t) -> p.test(t, u);
	}
	
	private static final <C> Stream<String> getStaticFieldValuesSortedByFieldName(Class<C> clazz) {
		return Arrays.stream(clazz.getFields()).sorted(Comparator.comparing(Field::getName)).map((f) -> {
			try {
				return (String) f.get(null);
			}
			catch(IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
	}
	
	public static boolean wasIntegrationLoaded(String modid) {
		return integrationLoaded.contains(modid);
	}
}
