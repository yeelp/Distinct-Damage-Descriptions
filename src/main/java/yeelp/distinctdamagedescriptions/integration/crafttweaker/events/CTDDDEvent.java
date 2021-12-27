package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import java.util.function.Supplier;

import stanhebben.zenscript.ZenRuntimeException;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public abstract class CTDDDEvent implements IDDDEvent {

	protected static DDDDamageType parseDamageType(String type) {
		return validateNonNull(DDDRegistries.damageTypes.get(type), () -> new ZenRuntimeException(String.format("%s isn't a recognized damage type! Remember types must start with the prefix: ddd_", type)));
	}

	private static <T, X extends Exception> T validateNonNull(T t, Supplier<X> exceptionSup) throws X {
		if(t != null) {
			return t;
		}
		throw exceptionSup.get();
	}
}
