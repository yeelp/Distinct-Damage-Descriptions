package yeelp.distinctdamagedescriptions.init;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import yeelp.distinctdamagedescriptions.ModConsts;

/**
 * Marks a class as being part of DDD's key loading cycle. There should also be
 * at least one method annotated with the {@link Initializer} annotation, which
 * will be invoked. Multiple methods will have them invoked in the order
 * {@link Class#getDeclaredMethods()} provides them.
 * 
 * @author Yeelp
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface DDDLoader {

	/**
	 * The modid we need for this loader to load.
	 * 
	 * @return The mod id needed
	 */
	String modid() default ModConsts.MODID;

	/**
	 * The name of this Loader.
	 * 
	 * @return The Loader's name.
	 */
	String name();

	/**
	 * A list of required Loaders, by their name.
	 * 
	 * @return A list of required loaders. These ones must run before this one
	 */
	String[] requiredLoaders() default {};

	/**
	 * The initializer method
	 * 
	 * @author Yeelp
	 * 
	 */
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Initializer {
		/**
		 * Should this initializer be timed? Useful for benchmarks
		 * 
		 * @return True if the method should be timed or not.
		 */
		boolean shouldTime() default true;
	}
}
