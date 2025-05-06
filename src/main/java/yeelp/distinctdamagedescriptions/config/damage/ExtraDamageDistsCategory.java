package yeelp.distinctdamagedescriptions.config.damage;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class ExtraDamageDistsCategory {
	@Name("Explosion DamageDistribution")
	@Comment("The damage distribution to use for explosion damage; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String explosionDist = DefaultValues.EXPLOSION_DIST;

	@Name("Cactus Distribution")
	@Comment("Enable/disable the cactus damage distribution. Cacti inflict piercing damage with this enabled. If disabled, vanilla handles it normally.")
	public boolean enableCactusDamage = true;

	@Name("Explosion Distribution")
	@Comment("Enable/disable the explosion damage distribution. This applies to player made explosions (TNT, etc.), non-player made explosions (Creepers, etc.) and Firework Rockets (while using Elytra), at least in vanilla. Explosions inflict bludgeoning damage if enabled. If disabled, vanilla handles it normally.")
	public boolean enableExplosionDamage = true;

	@Name("Falling Distribution")
	@Comment("Enable/disable the fall damage distribution. This applies to any fall damage (falling, ender pearls). Falling inflicts bludgeoning damage with this enabled. If disabled, vanilla handles it normally.")
	public boolean enableFallDamage = true;

	@Name("Fly Into Wall Distribution")
	@Comment("Enable/disable the fly-into-wall damage distribution. This occurs when a player flys into a wall with Elytra. Flying into a wall inflicts bludgeoning damage with this enabled. If disabled, vanilla handles it normally.")
	public boolean enableFlyIntoWallDamage = true;

	@Name("Anvil Distribution")
	@Comment("Enable/disable the anvil damage distribution. Anvils inflict bludgeoning damage with this enabled, however this damage can only be reduced by helmets, and is capped at a 25% damage reduction. If disabled, vanilla handles it normally.")
	public boolean enableAnvilDamage = true;

	@Name("Falling Block Distribution")
	@Comment("Enable/disable the falling block damage distribution. Falling Blocks inflict bludgeoning damage with this enabled, however this damage can only be reduced by helmets. Vanilla's falling blocks inflict no damage without other mods or unless they are summoned with altered NBT data. If disabled, vanilla handles it normally.")
	public boolean enableFallingBlockDamage = true;

	@Name("Lightning Distribution")
	@Comment("Enable/disable the lightning damage distribution. Lightning bolts will inflict lightning damage with this enabled.")
	public boolean enableLightningDamage = true;

	@Name("Wither Distribution")
	@Comment("Enable/disable the wither damage distribution. Withering will inflict necrotic damage with this enabled. It still can't be blocked by armor, but it can be blocked by resistances")
	public boolean enableWitherDamage = true;

	@Name("Potion Distribution")
	@Comment("Enable/disable potion damage distribution. Thrown potions of healing will inflict radiant damage against undead, and thrown potions of harming will inflict necrotic damage against non undead when enabled. It still can't be blocked by armor, but it can be blocked by resistances. Also applies to Area of Effect clouds")
	public boolean enablePotionDamage = true;

	@Name("Poison Effect Distribution")
	@Comment("Enable/diable poison effect distribution. Suffering from the Poison effect will inflict poison damage when enabled. It still can't be blocked by armor, but it can be blocked by resistances")
	public boolean enablePoisonEffectDamage = true;

	@Name("Thorns Distribution")
	@Comment("Enable/disable thorns distribution. The Thorns enchantment will inflict force damage when enabled.")
	public boolean enableThornsDamage = true;

	@Name("Evoker Fangs Distribution")
	@Comment("Enable/disable evoker fangs distribution. Evoker fanges will inflict force damage when enabled.")
	public boolean enableEvokerFangsDamage = true;

	@Name("Guardian Beam Distribution")
	@Comment("Enable/disable guardian beam distribution. Guardian beams (not the spikes) will inflict force damage when enabled.")
	public boolean enableGuardianDamage = true;

	@Name("Fire Distribution")
	@Comment("Enable/disable the fire damage distribution. All fire sources (fire, lava, magma etc.) will count as fire damage when enabled.")
	public boolean enableFireDamage = true;

	@Name("Daylight Burning Distribution")
	@Comment("Enable/disable daylight burning distribution. Undead burning in the day while exposed to the sky will receive radiant damage when enabled. Only those undead specified in Daylight Burning Entities will be susceptible.")
	public boolean enableDaylightBurningDamage = true;

	@Name("Daylight Burning Entities")
	@Comment("Entities listed here will be susceptible to the Daylight Burning Distribution. Entities not listed won't take radiant damage, even if undead. Ideally, only put undead entities that actually burn in daylight here.")
	public String[] daylightWhitelist = DefaultValues.ENTITIES_BURN_IN_DAYLIGHT;

	@Name("Guardian Thorns Damage")
	@Comment("Enable/disable guardian thorn distribution. Guardian's thorn attack will inflict piercing damage when enabled.")
	public boolean enableGuardianSpikesDamage = true;

	@Name("Parrot Poison Damage")
	@Comment("Enable/disable parrot cookie distribution. Parrots take poison damage when fed cookies.")
	public boolean enableParrotPoisonDamage = true;
	
	@Name("Suffocation Damage")
	@Comment("Enable/disable suffocation distribution. Suffocating inflicts force damage when enabled.")
	public boolean enableSuffocationDamage = true;
	
	@Name("Ender Pearl Damage")
	@Comment("Enable/disable Ender Pearl distribution, overriding the fall damage distribution for ender pearls specifically. Teleporting with Ender Pearls inflict force damage when enabled. Otherwise, it uses the same rules as falling.")
	public boolean enableEnderPearlDamage = true;
	
}
