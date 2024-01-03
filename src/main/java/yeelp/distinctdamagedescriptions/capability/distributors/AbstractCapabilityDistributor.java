package yeelp.distinctdamagedescriptions.capability.distributors;

import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

public abstract class AbstractCapabilityDistributor<T, U, C extends DDDCapabilityBase<? extends NBTBase>> {

	private final ResourceLocation loc;
	private Supplier<Boolean> shouldAssignDefault;

	protected AbstractCapabilityDistributor(ResourceLocation loc, Supplier<Boolean> shouldAssignDefault) {
		this.loc = loc;
		this.shouldAssignDefault = shouldAssignDefault;
	}

	protected AbstractCapabilityDistributor(AbstractCapabilityDistributor<T, U, C> distributor) {
		this(distributor.loc, distributor.shouldAssignDefault);
	}

	public abstract boolean isApplicable(T t);

	public final Tuple<ResourceLocation, C> getResourceLocationAndCapability(@Nonnull T t, @Nonnull String key) {
		return new Tuple<ResourceLocation, C>(this.loc, this.getCapability(Objects.requireNonNull(t, "Can't get capability for null!"), Objects.requireNonNull(key, "Can't get capability with null ResourceLocation!")));
	}

	protected abstract C getCapability(@Nonnull T t, @Nonnull String key);

	protected abstract IDDDConfiguration<U> getConfig();
	
	protected final boolean shouldAssignDefault() {
		return this.shouldAssignDefault.get();
	}
	
	public ResourceLocation getLoc() {
		return this.loc;
	}
}
