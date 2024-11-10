package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import java.util.Map;

import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public abstract class AbstractBaubleModifier<E extends Event> extends DDDBaseMap<Float> implements IDDDBaubleModifier<E> { 
	
	public AbstractBaubleModifier() {
		super(() -> 0.0f);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof AbstractBaubleModifier) {
			@SuppressWarnings("unchecked")
			AbstractBaubleModifier<Event> mod = (AbstractBaubleModifier<Event>) o;
			return super.equals(mod) && this.getType() == mod.getType();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Bauble Modifier Type: %s, Mod: %s", this.getType(), super.toString());
	}
	
	@Override
	public Map<DDDDamageType, Float> getModifications() {
		return this;
	}
}