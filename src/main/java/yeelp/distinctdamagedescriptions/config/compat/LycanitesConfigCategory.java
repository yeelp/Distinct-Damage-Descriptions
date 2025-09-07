package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class LycanitesConfigCategory {
	@Name("Enable Smited Distribution")
	@Comment("Enable/disable the smited distribution. Burning entities will take radiant damage while Smited")
	public boolean enableSmitedDist = true;
	
	@Name("Enable Bleed Distribution")
	@Comment("Enable/disable the bleed distribution. Taking damage from the Bleed potion effect will use the Bleed distribution. Otherwise, damage will be unclassified and not use DDD's damage calculations")
	public boolean enableBleedDist = true;

	@Name("Enable Scorchfire Distribution")
	@Comment("Enable/disable the Scorchfire Distribution. Burning in Scorchfire will use the Scorchfire distribution. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableScorchFireDistribution = true;

	@Name("Enable Doomfire Distribution")
	@Comment("Enable/disable the Doomfire Distribution. Burning in Doomfire will use the Doomfire distribution. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableDoomFireDistribution = true;

	@Name("Enable Hellfire Distribution")
	@Comment("Enable/disable the Hellfire Distribution. Burning in Hellfire will use the Hellfire distribution. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableHellFireDistribution = true;

	@Name("Enable Icefire Distribution")
	@Comment("Enable/disable the Icefire Distribution. Burning in Icefire will use the Icefire distribution.")
	public boolean enableIceFireDistribution = true;

	@Name("Enable Shadowfire Distribution")
	@Comment("Enable/disable the Shadowfire Distribution. Burning in Shadowfire will use the Shadowfire distribution. Otherwise, will inflict necrotic damage if the wither distribution is enabled.")
	public boolean enableShadowFireDistribution = true;

	@Name("Enable Frostfire Distribution")
	@Comment("Enable/disable the Frostfire Distribution. Burning in Frostfire will use the Frostfire distribution.")
	public boolean enableFrostFireDistribution = true;

	@Name("Enable Smitefire Distribution")
	@Comment("Enable/disable the Smitefire Distribution. Burning in Smitefire will use the Smitefire distribution. Otherwise, will inflict fire damage if the fire distribution is enabled.")
	public boolean enableSmiteFireDistribution = true;
	
	@Name("Enable Primefire Distribution")
	@Comment("Enable/disable the Primefire Distribution. Burning in Primefire will use the Primefire distribution. Otherwise, will inflict fire damage if the fire distribution is enabled.")
	public boolean enablePrimeFireDistribution = true;
	
	@Name("Enable Ooze Distribution")
	@Comment("Enable/disable the Ooze Distribution. Ooze and Rabbitooze will inflict cold damage.")
	public boolean enableOozeDistribution = true;

	@Name("Enable Acid Distribution")
	@Comment("Enable/disable the Acid Distribution. Acid and Sharacid will inflict acid damage.")
	public boolean enableAcidDistribution = true;

	@Name("Mob Projectiles")
	@Comment({
			"This is to give DDD a hint as to what creatures fire what types of projectiles.",
			"DDD needs this hint as when mobs are summoned as minions from Lycanite's Mobs, they use a different damage source instead of their projectiles.",
			"This list lets you set mobs' damage distribution to the projectile they fire when they are minions, instead of setting that mobs damage distribution to the same thing as the projectile manually.",
			"You don't need to use this, you can just set the mob's damage distribution yourself, if you want. This is provided for convenience. You also only really need to include mobs that are able to be summoned, but the default includes most, if not all mobs",
			"Format is <projectile name>;mobs",
			"    <projectile name> is the name of the projectile used in the projectile distributions configuration, without the mod id.",
			"    mobs is a comma separated list of mob ids (without their modid) that shoot that projectile."})
	@RequiresMcRestart
	public String[] creatureProjectiles = DefaultValues.ENEMY_PROJECTILE_MAP;
	
	@Name("Scorchfire Distribution")
	@Comment("The distribution that Scorchfire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String scorchFireDistribution = DefaultValues.SCORCHFIRE_DIST;
	
	@Name("Doomfire Distribution")
	@Comment("The distribution that Doomfire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String doomFireDistribution = DefaultValues.DOOMFIRE_DIST;
	
	@Name("Hellfire Distribution")
	@Comment("The distirbution that Hellfire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String hellFireDistribution = DefaultValues.HELLFIRE_DIST;
	
	@Name("Shadowfire Distribution")
	@Comment("The distribution that Shadowfire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String shadowFireDistribution = DefaultValues.SHADOWFIRE_DIST;
	
	@Name("Icefire Distribution")
	@Comment("The distribution that Icefire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String iceFireDistribution = DefaultValues.ICEFIRE_DIST;
	
	@Name("Frostfire Distribution")
	@Comment("The distribution that Frostfire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String frostFireDistribution = DefaultValues.FROSTFIRE_DIST;
	
	@Name("Smitefire Distribution")
	@Comment("The distribution that Smitefire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String smiteFireDistribution = DefaultValues.SMITEFIRE_DIST;
	
	@Name("Primefire Distribution")
	@Comment("The distribution that Primefire uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String primeFireDistribution = DefaultValues.PRIMEFIRE_DIST;
	
	@Name("Ooze Distribution")
	@Comment("The distribution that Ooze and Rabbitooze use if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String oozeDistirbution = DefaultValues.OOZE_DIST;
	
	@Name("Acid Distribution")
	@Comment("The distribution that Acid and Sharacid use if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String acidDistribution = DefaultValues.ACID_DIST;
	
	@Name("Bleed Distribution")
	@Comment("The distribution that taking damage from the Bleed effect uses if enabled; a list of comma separated tuples [(t, a)] with the same rules as mob or weapon damage")
	public String bleedDistribution = DefaultValues.LYCANITES_BLEED_DIST;
}
