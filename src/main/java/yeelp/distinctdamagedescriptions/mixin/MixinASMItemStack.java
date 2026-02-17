package yeelp.distinctdamagedescriptions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Mixin(ItemStack.class)
public interface MixinASMItemStack {
	@Accessor("capNBT")
	NBTTagCompound getCapNBT();
	
	@Invoker("forgeInit")
	void doForgeInit();
}
