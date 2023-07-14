package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.List;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenClass("mods.ddd.IDistribution")
@ZenRegister
public interface ICTDistribution {

	@ZenGetter("categories")
	List<ICTDDDDamageType> getCategories();
}
