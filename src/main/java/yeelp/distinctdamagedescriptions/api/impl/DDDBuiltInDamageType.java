package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Arrays;
import java.util.function.Predicate;

import yeelp.distinctdamagedescriptions.ModConsts.DamageTypeNames;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

/**
 * Implementation of all built in damage types
 * 
 * @author Yeelp
 *
 */
public final class DDDBuiltInDamageType extends DDDAbstractDamageType {
	private static final String HEX_REGEX = "([0-9a-f]){6}";
	public static final DDDDamageType UNKNOWN = new DDDBuiltInDamageType(DamageTypeNames.UNKNOWN, false, true);
	public static final DDDDamageType NORMAL = new DDDBuiltInDamageType(DamageTypeNames.NORMAL, false, true);
	public static final DDDDamageType SLASHING = new DDDBuiltInDamageType(DamageTypeNames.SLASHING, true);
	public static final DDDDamageType PIERCING = new DDDBuiltInDamageType(DamageTypeNames.PIERCING, true, "#defender was stabbed by #attacker", null);
	public static final DDDDamageType BLUDGEONING = new DDDBuiltInDamageType(DamageTypeNames.BLUDGEONING, true, "#defender was brutally crushed by #attacker", null);
	public static final DDDDamageType ACID = new DDDBuiltInDamageType(DamageTypeNames.ACID, false, "#defender was melted by #attacker", "#defender corroded away");
	public static final DDDDamageType COLD = new DDDBuiltInDamageType(DamageTypeNames.COLD, false, "#defender was frozen by #attacker", "#defender froze to death");
	public static final DDDDamageType FIRE = new DDDBuiltInDamageType(DamageTypeNames.FIRE, false, "#defender was immolated by #attacker", "#defender went up in flames");
	public static final DDDDamageType FORCE = new DDDBuiltInDamageType(DamageTypeNames.FORCE, false, "#defender was obliterated by #attacker", "#defender was eradicated");
	public static final DDDDamageType LIGHTNING = new DDDBuiltInDamageType(DamageTypeNames.LIGHTNING, false, "#defender was zapped by #attacker", "#defender was turned into a lightning rod");
	public static final DDDDamageType NECROTIC = new DDDBuiltInDamageType(DamageTypeNames.NECROTIC, false, "#defender had their life force stolen by #attacker", "#defender lost all vitality");
	public static final DDDDamageType POISON = new DDDBuiltInDamageType(DamageTypeNames.POISON, false, "#defender got a lethal dose of poison from #attacker", "#defender choked on poison");
	public static final DDDDamageType PSYCHIC = new DDDBuiltInDamageType(DamageTypeNames.PSYCHIC, false, "#defender got schooled by #attacker", "#defender had their mind blown");
	public static final DDDDamageType RADIANT = new DDDBuiltInDamageType(DamageTypeNames.RADIANT, false, "#defender was smited by #attacker", "#defender was smited from above");
	public static final DDDDamageType THUNDER = new DDDBuiltInDamageType(DamageTypeNames.THUNDER, false, "#defender was blasted to bits by #attacker", "#defender was destroyed by a lethal shockwave");
	public static final DDDDamageType[] PHYSICAL_TYPES = {
			SLASHING,
			PIERCING,
			BLUDGEONING};
	public static final DDDDamageType[] BUILT_IN_TYPES = {
			SLASHING,
			PIERCING,
			BLUDGEONING,
			ACID,
			COLD,
			FIRE,
			FORCE,
			LIGHTNING,
			NECROTIC,
			POISON,
			PSYCHIC,
			RADIANT,
			THUNDER};
	public static final DDDDamageType[] INTERNAL_TYPES = {
			NORMAL,
			UNKNOWN
	};

	static {
		Arrays.sort(BUILT_IN_TYPES);
		Arrays.sort(INTERNAL_TYPES);
	}
	
	public static final Predicate<DDDDamageType> filterOutHidden(boolean keepUnknown) {
		return (t) -> (keepUnknown && DDDDamageType.isUnknownType(t)) || !t.isHidden();
	}

	private DDDBuiltInDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, boolean hidden) {
		super(name, isPhysical, deathAttackerMessage, deathMessage, hidden);
		this.displayName = this.getTypeName().substring(DDDDamageType.DDD_PREFIX.length());
	}
	
	private DDDBuiltInDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage) {
		this(name, isPhysical, deathAttackerMessage, deathMessage, false);
	}
	
	private DDDBuiltInDamageType(String name, boolean isPhysical, boolean hidden) {
		this(name, isPhysical, null, null, hidden);
	}

	private DDDBuiltInDamageType(String name, boolean isPhysical) {
		this(name, isPhysical, false);
	}
	
	@Override
	public boolean isCustomDamage() {
		return false;
	}

	@Override
	public Source getCreationSource() {
		return Source.BUILTIN;
	}
	
	@Override
	protected void initialize() {
		this.setColour(parseHexOrThrow(DDDDamageType.removeDDDPrefixIfPresent(this.getTypeName())));
	}
	
	private static int parseHexOrThrow(String name) {
		String colour = ModConfig.client.damageTypeColours.get(name);
		if(colour == null) {
			throw new SyntaxException(String.format("Damage type %s doesn't have a colour defined in client damage type colours!", name));
		}
		if(!colour.matches(HEX_REGEX)) {
			throw new SyntaxException(String.format("Damage type %s has invalid HEX colour (is %s) defined in client damage type colours config", name, colour));
		}
		return Integer.parseInt(colour, 16);
	}
}
