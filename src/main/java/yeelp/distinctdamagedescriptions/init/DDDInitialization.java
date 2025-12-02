package yeelp.distinctdamagedescriptions.init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Stopwatch;

import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public final class DDDInitialization {

	private DDDInitialization() {
		throw new UnsupportedOperationException("This class is not supposed to be initialized!");
	}

	private static Map<String, Class<?>> loaderClasses = new HashMap<String, Class<?>>();
	private static Map<String, DDDLoader> loaders = new HashMap<String, DDDLoader>();
	private static Set<String> loadedLoaders = new HashSet<String>();

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
						String time = null;
						if(m.getAnnotation(DDDLoader.Initializer.class).shouldTime()) {
							Stopwatch timer = Stopwatch.createStarted();
							m.invoke(null);
							time = timer.stop().toString();
							DistinctDamageDescriptions.info(String.format("%s finished loading in %s", loader.name(), time));
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
			e.printStackTrace();
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
			return new Tuple<Class<?>, DDDLoader>(clazz, loader);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			// We're in real trouble
			throw new RuntimeException(e);
		}
	}

}
