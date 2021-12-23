package yeelp.distinctdamagedescriptions.config.enchant;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

public final class EnchantCategory {
	@Name("Enable Brute Force")
	@Comment("If false, the Brute Force enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
	@RequiresMcRestart
	public boolean enableBruteForce = true;

	@Name("Enable Sly Strike")
	@Comment("If false, the Sly Strike enchantment won't be registered. Worlds that had this enchant enabled will have this enchant removed from all items if loaded with this option set to false.")
	@RequiresMcRestart
	public boolean enableSlyStrike = true;
}
