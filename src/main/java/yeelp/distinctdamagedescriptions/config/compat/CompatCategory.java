package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public final class CompatCategory {
	@Name("Construct's Armory Integration")
	@Comment("Configure integration with Construct's Armory")
	public ConarmCategory conarm = new ConarmCategory();

	@Name("Tinker's Construct Integration")
	@Comment("Configure integration with Tinker's Construct")
	public TinkersCategory tinkers = new TinkersCategory();
	
	@Name("Lycanite's Mobs Integration")
	@Comment("Configure integration with Lycanite's Mobs")
	public LycanitesConfigCategory lycanites = new LycanitesConfigCategory();
	
	@Name("Tetra Integration")
	@Comment("Configure Tetra integration")
	public TetraConfigCategory tetra = new TetraConfigCategory();
}
