package yeelp.distinctdamagedescriptions.integration.capability;

import java.util.Map;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

/**
 * A Damage Distribution that is linked to another one. While it can't update
 * the distribution it is linked to, it forwards all calls for retrieving
 * weights and distributing damage to the linked one and returns the results.
 * 
 * While the LinkedDamageDistribution does implement {@link INBTSerializable},
 * as this class extends {@link DamageDistribution}, calls to
 * {@link #serializeNBT()} and {@link #deserializeNBT(NBTTagList)} throw
 * {@link UnsupportedOperationException}s as this capability is merely a
 * reference to another one, and is not saved to disk. Calls to update weights
 * also throw the same exception. No storage or factory methods exist and this
 * capability isn't even registered as one.
 * 
 * @author Yeelp
 *
 */
public abstract class LinkedDamageDistribution extends DamageDistribution {

	/**
	 * Get the {@link IDamageDistribution} this LinkedDamageDistribution is linked
	 * to.
	 * 
	 * @return The IDamageDistribution linked to by this one.
	 */
	protected abstract IDamageDistribution getDamageDistribution();

	@Override
	public Set<DDDDamageType> getCategories() {
		return this.getDamageDistribution().getCategories();
	}

	@Override
	public float getWeight(DDDDamageType type) {
		return this.getDamageDistribution().getWeight(type);
	}

	/**
	 * @throws UnsupportedOperationException This operation isn't supported.
	 */
	@Override
	public void deserializeSpecificNBT(NBTTagCompound lst) {
		throw new UnsupportedOperationException("LinkedDamageDistribution can not be deserialized!");
	}

	/**
	 * @throws UnsupportedOperationException This operation isn't supported.
	 */
	@Override
	public NBTTagCompound serializeSpecificNBT() {
		throw new UnsupportedOperationException("LinkedDamageDistribution can not be serialized!");
	}

	/**
	 * @throws UnsupportedOperationException This operation isn't supported.
	 */
	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("LinkedDamageDistribution has no meaningful weights to set!");
	}

	@Override
	public DamageMap distributeDamage(float dmg) {
		return this.getDamageDistribution().distributeDamage(dmg);
	}

	@Override
	public abstract IDamageDistribution copy();

	/**
	 * @throws UnsupportedOperationException This operation isn't supported.
	 */
	@Override
	public void setWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("LinkedDamageDistribution has no meaningful weights to set!");
	}

	@Override
	public IDamageDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(EntityLivingBase owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(IProjectile owner) {
		return this;
	}
}
