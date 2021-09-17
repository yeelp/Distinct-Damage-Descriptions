package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Comparator;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;

/**
 * An abstract skeleton implementation of DDDDamageType
 * 
 * @author Yeelp
 *
 */
public abstract class DDDAbstractDamageType implements DDDDamageType {
	private static final Comparator<DDDDamageType> DAMAGE_TYPE_COMPARATOR = Comparator.comparing(DDDDamageType::getType).thenComparing(DDDDamageType::getDisplayName);
	protected String displayName;
	private final String name;
	private final String attackerDeathMessage;
	private final String noAttackerDeathMessage;
	private final IDamageDistribution dist;
	private final Type type;
	private final int colour;

	/**
	 * Build a new damage type
	 * 
	 * @param name                 the internal name of the type. Will be prepended
	 *                             with "ddd_"
	 * @param isPhysical           true if the damage type is physical or not.
	 * @param deathAttackerMessage The death message to display when the death was
	 *                             caused by an attacker
	 * @param deathMessage         The death message to display when there is no
	 *                             attacker
	 * @param colour               the display colour to use in tooltips.
	 */
	DDDAbstractDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour) {
		this.name = "ddd_" + name;
		this.attackerDeathMessage = deathAttackerMessage;
		this.noAttackerDeathMessage = deathMessage;
		this.dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(this, 1.0f));
		this.type = isPhysical ? Type.PHYSICAL : Type.SPECIAL;
		this.colour = colour;
	}

	@Override
	public final String getTypeName() {
		return this.name;
	}

	@Override
	public IDamageDistribution getBaseDistribution() {
		return this.dist;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String getDeathMessage(boolean hasAttacker) {
		return hasAttacker ? this.attackerDeathMessage : this.noAttackerDeathMessage;
	}

	@Override
	public final String getDisplayName() {
		return this.displayName;
	}

	@Override
	public final int getColour() {
		return this.colour;
	}

	@Override
	public String toString() {
		return String.format("%s (%s, %s)", this.name, this.type.toString(), this.isCustomDamage() ? "custom" : "built-in");
	}

	/**
	 * @implNote The ordering is done via a {@link Comparator} that first compares this {@link DDDDamageType}'s {@link DDDDamageType.Type} by ordinal, then comparing type name lexicographically.
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(DDDDamageType o) {
		return DAMAGE_TYPE_COMPARATOR.compare(this, o);
	}
}
