package yeelp.distinctdamagedescriptions.integration.baubles;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.IDDDBaubleModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public final class DDDBaublesConfiguration extends DDDBaseConfiguration<Map<BaubleModifierType, IDDDBaubleModifier<? extends Event>>> {

	public DDDBaublesConfiguration() {
		super(() -> new EnumMap<BaubleModifierType, IDDDBaubleModifier<? extends Event>>(BaubleModifierType.class));
	}
	
	/**
	 * Add a bauble modifier to this configuration.
	 * @param <E> The type of event the modifier operates on.
	 * @param key The item id the modifier operates with.
	 * @param modifier the modifier.
	 * @return True if the configuration entry was overwritten by this call, false if not.
	 */
	public <E extends Event> boolean put(String key, IDDDBaubleModifier<E> modifier) {
		if(!this.configured(key)) {
			this.put(key, this.getDefaultValue());
		}
		Map<BaubleModifierType, IDDDBaubleModifier<? extends Event>> map = this.get(key);
		BaubleModifierType type = modifier.getType();
		boolean contains = map.containsKey(type);
		DebugLib.outputFormattedDebug("Bauble Modifier Registered: %s", modifier.toString());
		map.put(type, modifier);
		return contains;
	}
	
	public Optional<IDDDBaubleModifier<? extends Event>> getModifier(String key, BaubleModifierType type) {
		return this.getSafe(key).map((m) -> m.get(type));
	}
}
