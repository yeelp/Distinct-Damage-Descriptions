package yeelp.distinctdamagedescriptions.registries.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.api.IDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.registries.IDDDModifierRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDistributionModifierRegistry.DDDDamageDistributionModifierRegistry;

public enum DDDModifierRegistries implements IDDDModifierRegistries {
	INSTANCE;
	
	private final IDDDModifierRegistry<ItemStack, IDamageDistribution, IDDDCapModifier<ItemStack>> itemDamage = new DDDDamageDistributionModifierRegistry<ItemStack>("Item Damage");
	private final IDDDModifierRegistry<ItemStack, IArmorDistribution, IDDDCapModifier<ItemStack>> armors = new DDDDistributionModifierRegistry<ItemStack, IArmorDistribution>(() -> 0.0f, "Armor");
	private final IDDDModifierRegistry<ItemStack, ShieldDistribution, IDDDCapModifier<ItemStack>> shields = new DDDDistributionModifierRegistry<ItemStack, ShieldDistribution>(() -> 0.0f, "Shield");
	private final IDDDModifierRegistry<Entity, IDamageDistribution, IDDDCapModifier<Entity>> mobDamage = new DDDDamageDistributionModifierRegistry<Entity>("Entity Damage");
	private final IDDDModifierRegistry<EntityLivingBase, IMobResistances, IDDDResistanceModifier> mobResists = new DDDResistancesModifierRegistry();

	@Override
	public IDDDModifierRegistry<ItemStack, IDamageDistribution, IDDDCapModifier<ItemStack>> getItemStackDamageDistributionRegistry() {
		return this.itemDamage;
	}

	@Override
	public IDDDModifierRegistry<ItemStack, IArmorDistribution, IDDDCapModifier<ItemStack>> getItemStackArmorDistributionRegistry() {
		return this.armors;
	}

	@Override
	public IDDDModifierRegistry<ItemStack, ShieldDistribution, IDDDCapModifier<ItemStack>> getItemStackShieldDistributionRegistry() {
		return this.shields;
	}

	@Override
	public IDDDModifierRegistry<Entity, IDamageDistribution, IDDDCapModifier<Entity>> getEntityDamageDistributionRegistry() {
		return this.mobDamage;
	}

	@Override
	public IDDDModifierRegistry<EntityLivingBase, IMobResistances, IDDDResistanceModifier> getEntityResistancesRegistry() {
		return this.mobResists;
	}
}
