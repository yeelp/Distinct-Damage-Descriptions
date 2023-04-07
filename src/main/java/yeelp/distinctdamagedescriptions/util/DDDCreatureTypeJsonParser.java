package yeelp.distinctdamagedescriptions.util;

import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.util.Tuple;

public class DDDCreatureTypeJsonParser extends AbstractJsonParser<Tuple<CreatureTypeData, Iterable<String>>> {

	protected DDDCreatureTypeJsonParser(JsonObject root) {
		super(root);
	}

	@Override
	public Tuple<CreatureTypeData, Iterable<String>> parseJson() {
		String type = this.getString("name");
		if(type.equals("unknown")) {
			throw new IllegalArgumentException("unknown is an invalid type name!");
		}
		boolean critImmunity = this.getBoolean("critical_hit_immunity");
		Set<String> potionImmunities = this.extractStringsFromArray("potion_immunities");
		return new Tuple<CreatureTypeData, Iterable<String>>(new CreatureTypeData(type, potionImmunities, critImmunity), this.extractStringsFromArray("mobs"));
	}	
}
