package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public class LycanitesConfigCategory {
	@Name("Enable Smited Distribution")
	@Comment("Enable/disable the smited distribution. Burning entities will take radiant damage while Smited")
	public boolean enableSmitedDist = true;
}
