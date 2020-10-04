package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.PhysicalDamageEvent;

public class CTPhysicalDamageEvent extends CTDDDEvent implements IPhysicalDamageEvent
{
	private final String damageType;
	public CTPhysicalDamageEvent(PhysicalDamageEvent evt)
	{
		super(evt);
		this.damageType = evt.getDamageType().toString().toLowerCase();
	}

	@Override
	public String getDamageType()
	{
		return this.damageType;
	}
}
