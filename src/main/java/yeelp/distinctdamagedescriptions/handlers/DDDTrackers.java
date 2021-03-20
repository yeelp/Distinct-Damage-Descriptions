package yeelp.distinctdamagedescriptions.handlers;

public abstract class DDDTrackers
{
	public static DaylightTracker daylight;
	public static ParrotPoisonDamageHandler parrot;
	public static void register()
	{
		(daylight = new DaylightTracker()).register();
		(parrot = new ParrotPoisonDamageHandler()).register();
	}
}
