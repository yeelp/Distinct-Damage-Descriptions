package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

import java.util.Map;

public interface IDDDBaubleModifier<E extends Event> {

	void respondToEvent(E evt);
	
	BaubleModifierType getType();
	
	@Override
	boolean equals(Object other);
	
	Map<DDDDamageType, Float> getModifications();
}
