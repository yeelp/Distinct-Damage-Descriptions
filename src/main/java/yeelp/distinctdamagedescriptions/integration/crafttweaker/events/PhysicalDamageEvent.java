package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

public class PhysicalDamageEvent extends DDDEvent implements IPhysicalDamageEvent
{
	private final String damageType;
	public PhysicalDamageEvent(yeelp.distinctdamagedescriptions.event.PhysicalDamageEvent evt)
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
