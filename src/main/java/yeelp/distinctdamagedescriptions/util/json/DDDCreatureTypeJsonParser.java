package yeelp.distinctdamagedescriptions.util.json;

import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.api.impl.CreatureType;

public class DDDCreatureTypeJsonParser extends AbstractJsonParser<Tuple<CreatureType, Iterable<String>>> {

	protected DDDCreatureTypeJsonParser(JsonObject root) {
		super(root);
	}

	@Override
	public Tuple<CreatureType, Iterable<String>> parseJson() {
		String type = this.getString("name");
		if(type.equals("unknown")) {
			throw new IllegalArgumentException("unknown is an invalid type name!");
		}
		boolean critImmunity = this.getBoolean("critical_hit_immunity");
		Set<String> potionImmunities = this.extractStringsFromArray("potion_immunities");
		return new Tuple<CreatureType, Iterable<String>>(new CreatureType(type, potionImmunities, critImmunity), this.extractStringsFromArray("mobs"));
	}	
}
