package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Arrays;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

/**
 * Implementation of all built in damage types
 * 
 * @author Yeelp
 *
 */
public final class DDDBuiltInDamageType extends DDDAbstractDamageType {
	public static final DDDDamageType UNKNOWN = new DDDBuiltInDamageType("unknown", false, null, null, 0);
	public static final DDDDamageType NORMAL = new DDDBuiltInDamageType("normal", false, null, null, 0);
	public static final DDDDamageType SLASHING = new DDDBuiltInDamageType("slashing", true, null, null, 0xffffff);
	public static final DDDDamageType PIERCING = new DDDBuiltInDamageType("piercing", true, "#defender was stabbed by #attacker", null, 0xffffff);
	public static final DDDDamageType BLUDGEONING = new DDDBuiltInDamageType("bludgeoning", true, "#defender was brutally crushed by #attacker", null, 0xffffff);
	public static final DDDDamageType ACID = new DDDBuiltInDamageType("acid", false, "#defender was melted by #attacker", "#defender corroded away", 0x00e600);
	public static final DDDDamageType COLD = new DDDBuiltInDamageType("cold", false, "#defender was frozen by #attacker", "#defender froze to death", 0x00dbd8);
	public static final DDDDamageType FIRE = new DDDBuiltInDamageType("fire", false, "#defender was immolated by #attacker", "#defender went up in flames", 0xdb5f00);
	public static final DDDDamageType FORCE = new DDDBuiltInDamageType("force", false, "#defender was obliterated by #attacker", "#defender was eradicated", 0xdb2100);
	public static final DDDDamageType LIGHTNING = new DDDBuiltInDamageType("lightning", false, "#defender was zapped by #attacker", "#defender was turned into a lightning rod", 0x0000cf);
	public static final DDDDamageType NECROTIC = new DDDBuiltInDamageType("necrotic", false, "#defender had their life force stolen by #attacker", "#defender lost all vitality", 0x404040);
	public static final DDDDamageType POISON = new DDDBuiltInDamageType("poison", false, "#defender got a lethal dose of poison from #attacker", "#defender choked on poison", 0x7600ba);
	public static final DDDDamageType PSYCHIC = new DDDBuiltInDamageType("psychic", false, "#defender got schooled by #attacker", "#defender had their mind blown", 0xff0084);
	public static final DDDDamageType RADIANT = new DDDBuiltInDamageType("radiant", false, "#defender was smited by #attacker", "#defender was smited from above", 0xfffa5e);
	public static final DDDDamageType THUNDER = new DDDBuiltInDamageType("thunder", false, "#defender was blasted to bits by #attacker", "#defender was destroyed by a lethal shockwave", 0xc9c9c9);
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

	static {
		Arrays.sort(BUILT_IN_TYPES);
	}

	private DDDBuiltInDamageType(String name, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour) {
		super(name, isPhysical, deathAttackerMessage, deathMessage, colour);
		this.displayName = YLib.capitalize(this.getTypeName().substring("ddd_".length()));
	}

	@Override
	public boolean isCustomDamage() {
		return false;
	}
}
