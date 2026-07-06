package yeelp.distinctdamagedescriptions.init;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class DDDInitialization {

	private DDDInitialization() {
		throw new UnsupportedOperationException("This class is not supposed to be initialized!");
	}

	private static final Map<String, Class<?>> loaderClasses = Maps.newHashMap();
	private static final Map<String, DDDLoader> loaders = Maps.newHashMap();
	private static final Set<String> loadedLoaders = Sets.newHashSet();

	public static void runLoaders(FMLPreInitializationEvent evt) {
		Set<Tuple<Class<?>, DDDLoader>> loaderData = evt.getAsmData().getAll(DDDLoader.class.getName()).stream().map(DDDInitialization::parseASM).collect(Collectors.toSet());
		loaderData.forEach((data) -> DDDInitialization.runLoader(data.getFirst(), data.getSecond()));
	}

	private static void runLoader(Class<?> clazz, DDDLoader loader) {
		try {
			if(!loadedLoaders.contains(loader.name()) && Loader.isModLoaded(loader.modid())) {
				for(String dependency : loader.requiredLoaders()) {
					if(!dependency.isEmpty() && !loadedLoaders.contains(dependency)) {
						runLoader(loaderClasses.get(dependency), loaders.get(dependency));
					}
				}
				for(Method m : clazz.getDeclaredMethods()) {
					if(m.isAnnotationPresent(DDDLoader.Initializer.class) && Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 0) {
						if(m.getAnnotation(DDDLoader.Initializer.class).shouldTime()) {
							Stopwatch timer = Stopwatch.createStarted();
							m.invoke(null);
							timer.stop();
							DistinctDamageDescriptions.info(String.format("%s finished loading in %s", loader.name(), timer));
						}
						else {
							m.invoke(null);
							DistinctDamageDescriptions.info(String.format("%s loaded!", loader.name()));
						}
					}
					loadedLoaders.add(loader.name());
				}
			}
		}
		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if(e.getCause() instanceof InvariantViolationException) {
				throw (InvariantViolationException) e.getCause();
			}
			//scream louder
			DistinctDamageDescriptions.logStackTrace(e, DistinctDamageDescriptions::fatal);
			throw new RuntimeException(e);
		}
	}

	private static Tuple<Class<?>, DDDLoader> parseASM(ASMData data) {
		Class<?> clazz;
		try {
			clazz = Class.forName(data.getClassName(), true, DistinctDamageDescriptions.class.getClassLoader());
			DDDLoader loader = clazz.getAnnotation(DDDLoader.class);
			loaderClasses.put(loader.name(), clazz);
			loaders.put(loader.name(), loader);
			return new Tuple<>(clazz, loader);
		}
		catch(ClassNotFoundException e) {
			DistinctDamageDescriptions.logStackTrace(e, DistinctDamageDescriptions::fatal);
			// We're in real trouble
			throw new RuntimeException(e);
		}
	}

}
