package yeelp.distinctdamagedescriptions.util.json;

import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDCustomDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

public final class DDDDamageTypeJsonParser extends AbstractJsonParser<Tuple<DDDDamageType, DamageTypeData[]>> {

	protected DDDDamageTypeJsonParser(JsonObject root) {
		super(root);
	}

	@Override
	public Tuple<DDDDamageType, DamageTypeData[]> parseJson() {
		String name = this.getString("name");
		String displayName;
		int colour;
		if(this.hasKey("displayName")) {
			displayName = this.getString("displayName");
		}
		else {
			displayName = name;
		}
		name = name.toLowerCase();
		String colourFromJson = this.getString("displayColour");
		if(!colourFromJson.matches(HEX_REGEX)) {
			throw new SyntaxException("displayColour is an invalid hex string!");
		}
		colour = Integer.parseInt(colourFromJson, 16);
		String entityMsg = this.getString("deathMessages.deathHasAttacker");
		String otherMsg = this.getString("deathMessages.deathHasNoAttacker");
		Set<AbstractJsonParser<DamageTypeData>> datas = this.getArrayOfNestedObjects("damageTypes", DamageTypeDataJsonParser::new);
		DDDDamageType type = DDDRegistries.damageTypes.get(name);
		if(type == null) {
			type = new DDDCustomDamageType(name, displayName, false, entityMsg, otherMsg, colour);
			DDDRegistries.damageTypes.register(type);
		}
		else {
			DistinctDamageDescriptions.info(String.format("ddd_%s is already registered with display name %s; will use this instead...", name, type.getDisplayName()));
		}
		return new Tuple<DDDDamageType, DamageTypeData[]>(type, datas.stream().map(AbstractJsonParser<DamageTypeData>::parseJson).toArray(DamageTypeData[]::new));
	}

	private static final class DamageTypeDataJsonParser extends AbstractJsonParser<DamageTypeData> {

		protected DamageTypeDataJsonParser(JsonObject root) {
			super(root);
		}

		@Override
		public DamageTypeData parseJson() {
			String damageName = this.getString("dmgSource");
			boolean includeAll = this.getBoolean("includeAll");
			Set<String> indirectSources = this.extractStringsFromArray("indirectSources");
			Set<String> directSources = this.extractStringsFromArray("directSources");
			return new DamageTypeData(damageName, directSources, indirectSources, includeAll);
		}

	}
}
