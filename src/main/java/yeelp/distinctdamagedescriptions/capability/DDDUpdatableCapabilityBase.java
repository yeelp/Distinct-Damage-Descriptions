package yeelp.distinctdamagedescriptions.capability;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import yeelp.distinctdamagedescriptions.ModConsts;

public interface DDDUpdatableCapabilityBase<NBT extends NBTBase> extends DDDCapabilityBase<NBTBase> {
	
	String MODIFIERS_KEY = "modifiers";
	String NBT_KEY = "nbt";
	
	@Override
	default NBTBase serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList lst = new NBTTagList();
		this.getModifiers().forEach((s) -> lst.appendTag(new NBTTagString(s)));
		tag.setTag("modifiers", lst);
		tag.setTag("nbt", this.serializeSpecificNBT());
		return tag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	default void deserializeNBT(NBTBase nbt) {
		if(nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			tag.getTagList(MODIFIERS_KEY, ModConsts.NBT.STRING_TAG_ID).forEach((nbtString) -> this.addModifier(((NBTTagString) nbtString).getString()));			
			NBTBase base = tag.getTag(NBT_KEY);
			if(this.getSpecificNBTClass().isInstance(base)) {
				this.deserializeSpecificNBT((NBT) tag.getTag("nbt"));			
				return;
			}
		}
		this.deserializeOldNBT(nbt);
	}
	
	NBT serializeSpecificNBT();
	
	void deserializeSpecificNBT(NBT nbt);
	
	Set<String> getModifiers();
	
	void addModifier(String s);
	
	void removeModifier(String s);
	
	Class<NBT> getSpecificNBTClass();
	
	static <C extends DDDUpdatableCapabilityBase<? extends NBTBase>> void register(Class<C> capClass, Supplier<C> factorySup) {
		DDDCapabilityBase.register(capClass, NBTBase.class, factorySup);
	}
}
