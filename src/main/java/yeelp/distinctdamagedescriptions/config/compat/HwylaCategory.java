package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DamageDistributionNumberFormat;

public class HwylaCategory {
	@Name("HWYLA Damage Format")
	@Comment({
		"Control the way DDD displays damage on HWYLA info",
		"    PLAIN: Show the actual damage per type this mob inflicts.",
		"    PERCENT: Show the percentage of total damage this mob inflicts of a particular type."
	})
	public DamageDistributionNumberFormat hwylaDamageFormat = DamageDistributionNumberFormat.PERCENT;
	
	@Name("Show Mob Damage")
	@Comment("Shows the mob's damage they inflict. Takes into account the weapon being held.")
	public boolean showMobDamage = true;
	
	@Name("Show Mob Armor")
	@Comment("Shows how the mob's armor affects their resistances. Must have showMobReists set to true")
	public boolean showMobArmor = true;
	
	@Name("Show Mob Resists")
	@Comment("Shows mob resistances.")
	public boolean showMobResists = true;
	
}
