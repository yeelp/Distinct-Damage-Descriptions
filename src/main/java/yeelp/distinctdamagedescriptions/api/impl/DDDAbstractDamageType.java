package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Comparator;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

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
	private int colour;
	private boolean hidden;
	private boolean registered = false;

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
	 * 
	 * @param hidden               If this type should show in tooltips.
	 */
	DDDAbstractDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour, boolean hidden) {
		this.name = DDDDamageType.addDDDPrefixIfNeeded(name);
		this.attackerDeathMessage = deathAttackerMessage;
		this.noAttackerDeathMessage = deathMessage;
		this.dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(this, 1.0f));
		this.type = isPhysical ? Type.PHYSICAL : Type.SPECIAL;
		this.colour = colour;
		this.hidden = hidden;
	}
	
	/**
	 * Build a new damage type without a defined colour.
	 * 
	 * @param name                 the internal name of the type. Will be prepended
	 *                             with "ddd_"
	 * @param isPhysical           true if the damage type is physical or not.
	 * @param deathAttackerMessage The death message to display when the death was
	 *                             caused by an attacker
	 * @param deathMessage         The death message to display when there is no
	 *                             attacker
	 * 
	 * @param hidden               If this type should show in tooltips.
	 */
	DDDAbstractDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, boolean hidden) {
		this(name, isPhysical, deathAttackerMessage, deathMessage, -1, hidden);
	}
	

	@Override
	public final String getTypeName() {
		return this.name;
	}

	@Override
	public IDamageDistribution getBaseDistribution() {
		return this.dist.copy();
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String getDeathMessage(boolean hasAttacker) {
		return hasAttacker ? this.attackerDeathMessage : this.noAttackerDeathMessage;
	}

	@SuppressWarnings("deprecation")
	@Override
	public final String getDisplayName() {
		if(net.minecraft.util.text.translation.I18n.canTranslate("damagetypes.distinctdamagedescriptions." + this.displayName)) {
			return net.minecraft.util.text.translation.I18n.translateToLocal("damagetypes.distinctdamagedescriptions." + this.displayName);
		}
		return YLib.capitalize(this.displayName);
	}

	@Override
	public final int getColour() {
		return this.colour;
	}
	
	protected final void setColour(int colour) {
		this.colour = colour;
	}
	
	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void hideType() {
		this.hidden = true;
	}
	
	@Override
	public void unhideType() {
		this.hidden = false;
	}

	@Override
	public String toString() {
		return String.format("%s (%s, %s)", this.name, this.type.toString(), this.isCustomDamage() ? "custom" : "built-in");
	}
	
	@Override
	public final void onRegister() {
		if(this.registered) {
			throw new RuntimeException("Damage types can't be registered twice?");
		}
		this.initialize();
		this.registered = true;
	}
	
	protected abstract void initialize();

	/**
	 * @implNote The ordering is done via a {@link Comparator} that first compares
	 *           this {@link DDDDamageType}'s {@link DDDDamageType.Type} by ordinal,
	 *           then comparing type name lexicographically. {@inheritDoc}
	 */
	@Override
	public int compareTo(DDDDamageType o) {
		return DAMAGE_TYPE_COMPARATOR.compare(this, o);
	}
}
