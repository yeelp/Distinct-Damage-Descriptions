package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public class LycanitesConfigCategory {
	@Name("Enable Smited Distribution")
	@Comment("Enable/disable the smited distribution. Burning entities will take radiant damage while Smited")
	public boolean enableSmitedDist = true;

	@Name("Enable Scorchfire Distribution")
	@Comment("Enable/disable the Scorchfire Distribution. Burning in Scorchfire will inflict 50% fire and 50% force damage. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableScorchFireDistribution = true;

	@Name("Enable Doomfire Distribution")
	@Comment("Enable/disable the Doomfire Distribution. Burning in Doomfire will inflict 50% fire and 50% necrotic damage. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableDoomFireDistribution = true;

	@Name("Enable Hellfire Distribution")
	@Comment("Enable/disable the Hellfire Distribution. Burning in Hellfire will inflict 30% fire and 70% necrotic damage. Otherwise, will deal fire damage if the fire distribution is enabled.")
	public boolean enableHellFireDistribution = true;

	@Name("Enable Icefire Distribution")
	@Comment("Enable/disable the Icefire Distribution. Burning in Icefire will inflict 50% fire and 50% cold damage.")
	public boolean enableIceFireDistribution = true;

	@Name("Enable Shadowfire Distribution")
	@Comment("Enable/disable the Shadowfire Distribution. Burning in Shadowfire will inflict 20% fire, 40% psychic damage and 40% necrotic damage. Otherwise, will inflict necrotic damage if the wither distribution is enabled.")
	public boolean enableShadowFireDistribution = true;

	@Name("Enable Frostfire Distribution")
	@Comment("Enable/disable the Frostfire Distribution. Burning in Frostfire will inflict 30% fire and 70% cold damage.")
	public boolean enableFrostFireDistribution = true;

	@Name("Enable Smitefire Distribution")
	@Comment("Enable/disable the Smitefire Distribution. Burning in Smitefire will inflict 30% fire and 70% radiant damage. Otherwise, will inflict fire damage if the fire distribution is enabled.")
	public boolean enableSmiteFireDistribution = true;

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
	public String[] creatureProjectiles = DefaultValues.ENEMY_PROJECTILE_MAP;
}
