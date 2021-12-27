package yeelp.distinctdamagedescriptions.config.compat;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import yeelp.distinctdamagedescriptions.config.DefaultValues;

public final class TetraConfigCategory {
	@Name("Tetra Tool Part Base Distributions")
	@Comment({
			"Modify base distributions for Tetra tool parts.",
			"This is the distribution that gets modified by the material used.",
			"The format is <nbt_tag>;<distribution>;<bias> where:",
			"   <nbt_tag> is the NBT tag for the part type. blade parts are prefixed with 'sword/' and duplex parts are prefixed with 'duplex/'. Duplex tools don't need to specify left or right heads; the same is used for both. Tool butts don't count towards Duplex tool distributions, so the entry 'duplex/butt...' can be omitted.",
			"   <distribution> is a distribution list, as specified in item distributions.",
			"   <bias> is the bias this part has towards its distribution The difference between this and the material bias is used to determine how much the material influences the distribution.",
			"A tool like /ct nbt or an NBT editor can be used to find the NBT tag names. All the defaults are included here."})
	@RequiresMcRestart
	public String[] toolPartDists = DefaultValues.TETRA_PART_BIAS;

	@Name("Tetra Material Distributions")
	@Comment({
			"Modify material distributions for Tetra tool parts.",
			"This is how the base tool part distribution gets modified.",
			"The format is <nbt_tag>;<distribution>;<bias> where:",
			"   <nbt_tag> is the material name found in the item NBT. Only the material name is needed, the part type referenced in the tag should not be included here. (That is, you only need to specify a material once, not for each part it's usable in).",
			"   <distribution> is the distribution the material has, as specified in item distributions",
			"   <bias> is the measure of how well this material influences parts. The difference between this and the tool part bias is used to determine how much this matereial influences the part's distribution.",
			"A tool like /ct nbt or an NBT editor is great for finding the material names. All the defaults are included here."})
	@RequiresMcRestart
	public String[] toolMatDists = DefaultValues.TETRA_MAT_BIAS;
}
