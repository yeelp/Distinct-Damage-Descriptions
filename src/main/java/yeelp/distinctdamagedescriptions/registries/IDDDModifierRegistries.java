package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.api.IDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

public interface IDDDModifierRegistries {

	IDDDModifierRegistry<ItemStack, IDamageDistribution, IDDDCapModifier<ItemStack>> getItemStackDamageDistributionRegistry();

	IDDDModifierRegistry<ItemStack, IArmorDistribution, IDDDCapModifier<ItemStack>> getItemStackArmorDistributionRegistry();

	IDDDModifierRegistry<ItemStack, ShieldDistribution, IDDDCapModifier<ItemStack>> getItemStackShieldDistributionRegistry();

	IDDDModifierRegistry<EntityLivingBase, IDamageDistribution, IDDDCapModifier<EntityLivingBase>> getMobDamageDistributionRegistry();
	
	IDDDModifierRegistry<Entity, IDamageDistribution, IDDDCapModifier<Entity>> getProjectileDistributionRegistry();

	IDDDModifierRegistry<EntityLivingBase, IMobResistances, IDDDResistanceModifier> getEntityResistancesRegistry();

	@SuppressWarnings("unchecked")
	default <T extends ICapabilityProvider, M extends IDDDCapModifier<T>> void register(M modifier) {
		if(!modifier.validate()) {
			throw new IllegalArgumentException(String.format("Modifier %s from %s could not be validated! Has weights: %s", modifier.getName(), modifier.getCreationSourceString(), modifier.entrySet().toString()));
		}
		switch(modifier.getAppliesToEnum()) {
			case ARMOR:
				this.getItemStackArmorDistributionRegistry().register((IDDDCapModifier<ItemStack>) modifier);
				break;
			case ITEM_DAMAGE:
				this.getItemStackDamageDistributionRegistry().register((IDDDCapModifier<ItemStack>) modifier);
				break;
			case MOB_DAMAGE:
				this.getMobDamageDistributionRegistry().register((IDDDCapModifier<EntityLivingBase>) modifier);
				break;
			case PROJECTILE:
				this.getProjectileDistributionRegistry().register((IDDDCapModifier<Entity>) modifier);
				break;
			case MOB_RESISTANCES:
				this.getEntityResistancesRegistry().register((IDDDResistanceModifier) modifier);
				break;
			case SHIELD:
				this.getItemStackShieldDistributionRegistry().register((IDDDCapModifier<ItemStack>) modifier);
				break;
			default:
				break;
		}
	}

	/**
	 * Registry of capability modifiers
	 * 
	 * @author Yeelp
	 *
	 * @param <T> The object that will have this capability that gets modified
	 * @param <D> The kind of capability that gets modified
	 * @param <M> The kind of modifier that gets applied to the capability.
	 */
	interface IDDDModifierRegistry<T extends ICapabilityProvider, D extends DDDCapabilityBase<?>, M extends IDDDCapModifier<T>> extends IDDDRegistry<M> {

		/**
		 * Get a set of all applicable modifiers, used for keeping track which ones have
		 * been applied
		 * 
		 * @param provider the object being modified.
		 * @return a Set of the names of applicable modifiers.
		 */
		Set<String> getNamesOfApplicableModifiers(T provider);

		/**
		 * Apply modifiers to this capability
		 * 
		 * @param provider The object to apply to
		 * @param cap      The capability being modified.
		 */
		void applyModifiers(T provider, D cap);
	}
	
}
