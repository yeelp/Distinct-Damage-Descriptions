package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import java.util.Arrays;
import java.util.List;

import yeelp.distinctdamagedescriptions.event.CustomDamageEvent;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public class CTCustomDamageEvent extends CTDDDEvent implements ICustomDamageEvent
{
	private final List<String> damageTypes;
	public CTCustomDamageEvent(CustomDamageEvent evt)
	{
		super(evt);
		damageTypes = Arrays.asList(evt.getDamageTypes());
	}

	@Override
	public List<String> getDamageTypes()
	{
		return this.damageTypes;
	}
}
