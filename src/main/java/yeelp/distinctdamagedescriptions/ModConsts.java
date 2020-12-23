package yeelp.distinctdamagedescriptions;

public class ModConsts
{
	public static final String MODID = "distinctdamagedescriptions";
	public static final String MODID_SHORT = "ddd";
	public static final String NAME = "Distinct Damage Descriptions";
	public static final String VERSION = "1.1.0-beta";
	
	public static final String CRAFTTWEAKER_ID = "crafttweaker";
	public static final class InternalDamageTypes
	{
		public static final String SLASHING = "slashing", PIERCING = "piercing", BLUDGEONING = "bludgeoning";
		public static final String[] PHYSICAL_DAMAGE_TYPES = {SLASHING, PIERCING, BLUDGEONING};
	}
}
