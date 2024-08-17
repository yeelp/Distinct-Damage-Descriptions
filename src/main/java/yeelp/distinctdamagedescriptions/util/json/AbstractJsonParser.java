package yeelp.distinctdamagedescriptions.util.json;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

/**
 * A JSON Parser. It expects the file passed in the constructor to contain a
 * valid JSON object.
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractJsonParser<T> {
	protected static final String HEX_REGEX = "([0-9a-f]){6}";
	
	private final JsonObject root;
	
	protected AbstractJsonParser(JsonObject root) {
		this.root = root;
	}
	
	public boolean hasKey(String key) {
		return this.root.has(key);
	}
	
	private Optional<JsonElement> getOptionalElement(String key) {
		Queue<String> path = new LinkedList<String>(Arrays.asList(key.split("\\.")));
		if(path.isEmpty()) {
			path.add(key);
		}
		JsonObject e = this.root;
		JsonElement elem = null;
		while(path.size() > 0) {
			elem = e.get(path.poll());
			if(elem.isJsonObject()) {
				e = elem.getAsJsonObject();
			}
		}
		return Optional.ofNullable(elem);
	}

	private JsonElement getElement(String key) {
		return this.getOptionalElement(key).orElseThrow(() -> new SyntaxException(key + " not found in JSON file! Verify it's present and spelled correctly."));
	}
	
	private JsonPrimitive getPrimitive(String key) {
		return this.tryConversion(key, JsonElement::isJsonPrimitive, JsonElement::getAsJsonPrimitive);		
	}
	
	private JsonArray getArray(String key) {
		return this.tryConversion(key, JsonElement::isJsonArray, JsonElement::getAsJsonArray);
	}
	
	private <R> R tryPrimitiveConversion(String key, Predicate<JsonPrimitive> isConvertible, Function<JsonPrimitive, R> convert) {
		JsonPrimitive p = this.getPrimitive(key);
		if(isConvertible.test(p)) {
			return convert.apply(p);
		}
		throw new IllegalArgumentException(key +" can not be converted as a primitive! Likely the wrong primitive type!");
	}
	
	private <R> R tryConversion(String key, Predicate<JsonElement> isConvertible, Function<JsonElement, R> convert) {
		JsonElement e = this.getElement(key);
		if(isConvertible.test(e)) {
			return convert.apply(e);
		}
		throw new IllegalArgumentException("Can not convert value at "+ key);
	}
	
	protected boolean getBoolean(String key) {
		return this.tryPrimitiveConversion(key, JsonPrimitive::isBoolean, JsonPrimitive::getAsBoolean).booleanValue();
	}
	
	protected String getString(String key) {
		return this.tryPrimitiveConversion(key, JsonPrimitive::isString, JsonPrimitive::getAsString);
	}
	
	protected int getInt(String key) {
		return this.tryPrimitiveConversion(key, JsonPrimitive::isNumber, JsonPrimitive::getAsInt);
	}
	
	private Stream<JsonElement> buildStreamOfJsonElements(String key) {
		Stream.Builder<JsonElement> builder = Stream.builder();
		this.getArray(key).iterator().forEachRemaining(builder::add);
		return builder.build();
	}
	
	private Stream<JsonPrimitive> buildStreamOfJsonPrimitives(String key) {
		return this.buildStreamOfJsonElements(key).filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive);
	}
	
	private <R> Set<R> extractTypesFromArray(String key, Predicate<JsonPrimitive> filter, Function<JsonPrimitive, R> converter) {
		return this.buildStreamOfJsonPrimitives(key).filter(filter).map(converter).collect(Collectors.toSet());
	}
	
	protected Set<String> extractStringsFromArray(String key) {
		return this.extractTypesFromArray(key, JsonPrimitive::isString, JsonPrimitive::getAsString);
	}

	public abstract T parseJson();
	
	protected <R> AbstractJsonParser<R> getNestedObject(String key, Function<JsonObject, AbstractJsonParser<R>> constructor) {
		return constructor.apply(this.tryConversion(key, JsonElement::isJsonObject, JsonElement::getAsJsonObject));
	}
	
	protected <R> Set<AbstractJsonParser<R>> getArrayOfNestedObjects(String key, Function<JsonObject, AbstractJsonParser<R>> constructor) {
		return this.buildStreamOfJsonElements(key).filter(JsonElement::isJsonObject).map((e) -> constructor.apply(e.getAsJsonObject())).collect(Collectors.toSet());
	}
	
	protected static <X extends Exception> void validateNumber(String input, String regex, Supplier<X> exceptionSup) throws X {
		if(!input.matches(regex)) {
			throw exceptionSup.get();
		}
	}
}
