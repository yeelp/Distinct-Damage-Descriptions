package yeelp.distinctdamagedescriptions.util.lib;


import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;

public interface DDDAttributeModifierCollections {
	
	interface DDDAttributeModifier {
		
		UUID getUUID();
		
		String getName();
		
		IAttribute getAttribute();
		
		default void applyModifier(EntityLivingBase target, float amount, int op) {
			IAttributeInstance attribute = target.getEntityAttribute(this.getAttribute());
			AttributeModifier mod = new AttributeModifier(this.getUUID(), this.getName(), amount, op);
			if(attribute.hasModifier(mod)) {
				DistinctDamageDescriptions.warn("Applying an armor modifier while one is already present! Normally, existing armor modifiers applied by DDD should get removed first, but something prevented that from happening...? Will remove current armor modifier.");
				attribute.removeModifier(mod);
			}
			attribute.applyModifier(mod);
		}
		
		default void applyModifier(EntityLivingBase target, float amount) {
			this.applyModifier(target, amount, 0);
		}
		
		default void removeModifier(EntityLivingBase target) {
			target.getEntityAttribute(this.getAttribute()).removeModifier(this.getUUID());
		}
		
		@Nullable
		default AttributeModifier getModifier(EntityLivingBase target) {
			return target.getEntityAttribute(this.getAttribute()).getModifier(this.getUUID());
		}
	}
	
	enum ArmorModifiers implements DDDAttributeModifier {
		ARMOR(UUID.fromString("72e5859a-02d8-4170-9632-f9786547d697"), "DDD Armor Calculations Modifier", SharedMonsterAttributes.ARMOR) {
			@Override
			protected float extractValue(ArmorValues aVals) {
				return aVals.getArmor();
			}
		},
		TOUGHNESS(UUID.fromString("c19d6077-8772-460e-8250-d7780cbb85ca"), "DDD Toughness Calculations Modifier", SharedMonsterAttributes.ARMOR_TOUGHNESS) {
			@Override
			protected float extractValue(ArmorValues aVals) {
				return aVals.getToughness();
			}
		};
		
		private final UUID uuid;
		private final String name;
		private final IAttribute attribute;
		
		private ArmorModifiers(UUID uuid, String name, IAttribute attribute) {
			this.uuid = uuid;
			this.name = name;
			this.attribute = attribute;
		}
		
		@Override
		public IAttribute getAttribute() {
			return this.attribute;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public UUID getUUID() {
			return this.uuid;
		}
		
		protected abstract float extractValue(ArmorValues aVals);
		
		public void applyModifier(EntityLivingBase target, ArmorValues values) {
			this.applyModifier(target, this.extractValue(values));
		}
	}
	

	static Optional<DDDAttributeModifier> getModifierFromName(String name) {
		for(ArmorModifiers mod : ArmorModifiers.values()) {
			if(mod.getName().equals(name)) {
				return Optional.of(mod);
			}
		}
		return Optional.empty();
	}
	
}
