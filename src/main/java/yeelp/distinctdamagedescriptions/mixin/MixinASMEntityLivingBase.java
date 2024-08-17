package yeelp.distinctdamagedescriptions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.EntityLivingBase;

@Mixin(EntityLivingBase.class)
public interface MixinASMEntityLivingBase {
	
	@Invoker("blockUsingShield")
	public void useBlockUsingShield(EntityLivingBase attacker);
}
