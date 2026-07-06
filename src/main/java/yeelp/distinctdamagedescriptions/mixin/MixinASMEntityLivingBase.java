package yeelp.distinctdamagedescriptions.mixin;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLivingBase.class)
public interface MixinASMEntityLivingBase {
	
	@Invoker("blockUsingShield")
	void useBlockUsingShield(EntityLivingBase attacker);
}
