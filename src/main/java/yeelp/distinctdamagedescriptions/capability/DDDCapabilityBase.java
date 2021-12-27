package yeelp.distinctdamagedescriptions.capability;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface DDDCapabilityBase<T extends NBTBase> extends ICapabilitySerializable<T> {

	/**
	 * A skeletal {@link IStorage} implementation.
	 * 
	 * @author Yeelp
	 *
	 * @param <NBT> NBT type stored
	 * @param <Cap> Capability being stored.
	 */
	final class DDDCapStorage<NBT extends NBTBase, Cap extends DDDCapabilityBase<NBT>> implements IStorage<Cap> {
		private final Class<NBT> nbtClass;

		/**
		 * A new capability storage
		 * 
		 * @param nbtClass the nbt class used in storage
		 */
		public DDDCapStorage(Class<NBT> nbtClass) {
			this.nbtClass = nbtClass;
		}

		@Override
		public NBTBase writeNBT(Capability<Cap> capability, Cap instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<Cap> capability, Cap instance, EnumFacing side, NBTBase nbt) {
			if(this.nbtClass.isInstance(nbt)) {
				instance.deserializeNBT(this.nbtClass.cast(nbt));
			}
		}
	}

	/**
	 * A skeletal default capability factory
	 * 
	 * @author Yeelp
	 *
	 * @param <NBT> NBT stored.
	 * @param <Cap> Capability stored.
	 */
	final class DDDCapFactory<NBT extends NBTBase, Cap extends DDDCapabilityBase<NBT>> implements Callable<Cap> {
		private final Supplier<Cap> sup;

		/**
		 * New factory
		 * 
		 * @param sup default capability supplier
		 */
		public DDDCapFactory(Supplier<Cap> sup) {
			this.sup = sup;
		}

		@Override
		public Cap call() throws Exception {
			return this.sup.get();
		}
	}

	static <NBT extends NBTBase, C extends DDDCapabilityBase<NBT>> void register(Class<C> capClass, Class<NBT> nbtClass, Supplier<C> factorySup) {
		CapabilityManager.INSTANCE.register(capClass, new DDDCapStorage<NBT, C>(nbtClass), new DDDCapFactory<NBT, C>(factorySup));
	}
}
