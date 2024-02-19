package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;

@ZenClass("mods.ddd.ICustomDistribution")
@ZenRegister
public interface ICTDDDCustomDistribution extends ICTDamageDistribution, DDDPredefinedDistribution {

	@Override
	@ZenGetter("name")
	String getName();

	@Override
	default Source getCreationSource() {
		return Source.CoT;
	}
	
}
