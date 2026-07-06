package yeelp.distinctdamagedescriptions.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStack.class)
public interface MixinASMItemStack {
	@Accessor(value = "capNBT", remap = false)
	NBTTagCompound getCapNBT();
	
	@Invoker(value = "forgeInit", remap = false)
	void doForgeInit();
}
