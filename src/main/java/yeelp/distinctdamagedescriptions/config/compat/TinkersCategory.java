package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class TinkersCategory {
	@Name("Tool Bias")
	@Comment({
			"Control a tool's bias. Tool bias is a tinker's tool's ability to stick to its base distribution.",
			"Base distributions are defined in items.",
			"Entries are of the form id;bias",
			"   id is the namespaced id of the item (e.g. tconstruct:rapier). The mod name can be excluded ONLY if it's from tconstruct. Tinker tools added from other mods need the full namepaced id.",
			"   bias is the tool's bias. A non negative decimal value. Negatives will be treated as 0. This value represents a percentage, so values under 1 are recommended, but you can use values over 1 for tool's whose distributions will never ever change."})
	@RequiresMcRestart
	public String[] toolBias = DefaultValues.TOOL_BIAS;

	@Name("Material Influence")
	@Comment({
			"Control's a material's influence. A material has two parts to its influence: and actual influence rating and a distribution.",
			"A material's distribution is its preferred distribution it tries to lean the tool towards.",
			"A material's influence rating is a measure of how good that material is at influencing head pieces."})
	@RequiresMcRestart
	public String[] matBias = DefaultValues.MATERIAL_BIAS;
}
