package yeelp.distinctdamagedescriptions.handlers;

public abstract class DDDTrackers
{
	public static DaylightTracker daylight;
	public static void register()
	{
		(daylight = new DaylightTracker()).register();
	}
}
