package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;

@ZenClass(CTConsts.CTClasses.CTDAMAGEDISTRIBUTION)
@ZenRegister
public interface ICTDamageDistribution extends ICTDistribution {

	@ZenMethod
	ICTDDDBaseMap distribute(float dmg);
}
