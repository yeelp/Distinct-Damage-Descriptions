package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ddd.IDamageDistribution")
@ZenRegister
public interface ICTDamageDistribution extends ICTDistribution {

	@ZenMethod
	ICTDDDBaseMap distribute(float dmg);
}
