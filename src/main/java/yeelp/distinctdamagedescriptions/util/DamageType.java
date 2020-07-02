package yeelp.distinctdamagedescriptions.util;

/**
 * The different damage types.
 * @author dunca
 *
 */
public enum DamageType
{
	SLASHING,
	PIERCING,
	BLUDGEONING;
	
	/**
	 * Build a new DamageType from a String.<p>
	 * <p>
	 * This tries to parse the character to determine the DamageType.<p>
	 *     - s: SLASHING<p>
	 *     - p: PIERCING<p>
	 *     - b: BLUDGEONING<p>
	 * Otherwise, this method throws an IllegalArgumentException
	 * @param c character to parse
	 * @throws IllegalArgumentException if the character isn't one of the ones described above.
	 */
	public static DamageType parseDamageType(char c) throws IllegalArgumentException
	{
		switch(c)
		{
			case 's':
				return SLASHING;
			case 'p':
				return PIERCING;
			case 'b':
				return BLUDGEONING;
			default:
				throw new IllegalArgumentException("Invalid character for parsing DamageType!");
		}
	}
}
