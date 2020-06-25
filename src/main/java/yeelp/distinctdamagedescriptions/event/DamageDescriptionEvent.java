package yeelp.distinctdamagedescriptions.event;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.util.DamageType;

public class DamageDescriptionEvent extends Event
{
	private DamageType type;
	private LivingAttackEvent evt;
	public DamageDescriptionEvent(DamageType type, LivingAttackEvent evt)
	{
		super();
		this.evt = evt;
		this.type = type;
	}
	
	public DamageType getType()
	{
		return this.type;
	}
	
	public LivingAttackEvent getLivingAttackEvent()
	{
		return this.evt;
	}
	
	@Override
	public void setCanceled(boolean cancel)
	{
		evt.setCanceled(cancel);
		super.setCanceled(cancel);
	}
	
	public static class SlashingDamage extends DamageDescriptionEvent
	{
		public SlashingDamage(LivingAttackEvent evt)
		{
			super(DamageType.SLASHING, evt);
		}
	}
	
	public static class BludgeoningDamage extends DamageDescriptionEvent
	{
		public BludgeoningDamage(LivingAttackEvent evt)
		{
			super(DamageType.BLUDGEONING, evt);
		}
	}
	
	public static class PiercingDamage extends DamageDescriptionEvent
	{
		public PiercingDamage(LivingAttackEvent evt)
		{
			super(DamageType.PIERCING, evt);
		}
	}
}
