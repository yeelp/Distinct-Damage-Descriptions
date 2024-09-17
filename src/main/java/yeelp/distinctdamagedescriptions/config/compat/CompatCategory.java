package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;

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
	
	@Name("HWYLA Integration")
	@Comment("Configure HWYLA integration")
	public HwylaCategory hwyla = new HwylaCategory();
	
	@Name("Only Use Defined Item Distributions")
	@Comment({"With this enabled, DDD will not assign distributions to items not given an explicit distribution in the config or through mod integration. It is recommended to leave this option untouched!",
		"These items will use a default damage distribution (defined in the damage category) if one isn't defined, a default armor distribution if applicable (defined in the resistance category) if one isn't defined and a default shield distribution if applicable (defined in the resistance category) if one isn't defined, which can not be altered during gameplay.",
		"This has the advantage of not saving any NBT data to these items, which may cause problems with some mod specific recipes, such as Mekanism.",
		"Note this has no effect on items with distributions set in the config or items already created. Those will still have NBT data saved to them. This is just for everything else."})
	@RequiresWorldRestart
	public boolean definedItemsOnly = true;
	
	@Name("Only Use Defined Entity Distributions")
	@Comment({"With this enabled, DDD will not assign distributions/resistances to entities not given an explicit distribution/resistances in the config or through mod integration.",
		"These entities will use a default damage distribution (defined in the damage category) and a default set of mob resistances (no resistances, no adaptability, no immunities), which can not be altered during gameplay.",
		"This has the advantage to not saving any NBT data to these entities, which may theoretically cause problems with some mod behaviour, though extremely unlikely."})
	@RequiresWorldRestart
	public boolean definedEntitiesOnly = true;
}
