package yeelp.distinctdamagedescriptions.util.lib;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;

public interface DDDAttributeModifierCollections {
	
	UUID ARMOR_CALC_UUID = UUID.fromString("72e5859a-02d8-4170-9632-f9786547d697");
	UUID TOUGHNESS_CALC_UUID = UUID.fromString("c19d6077-8772-460e-8250-d7780cbb85ca");

	ModifierRecord ARMOR = new ModifierRecord("DDD Armor Calculations Modifier", ARMOR_CALC_UUID, SharedMonsterAttributes.ARMOR);
	ModifierRecord TOUGHNESS = new ModifierRecord("DDD Toughness Calculations Modifier", TOUGHNESS_CALC_UUID, SharedMonsterAttributes.ARMOR_TOUGHNESS);
	
	static final class ModifierRecord {
		private final String name;
		private final UUID uuid;
		private final IAttribute attribute;
		
		ModifierRecord(String name, UUID uuid, IAttribute attributeModified) {
			this.name = name;
			this.uuid = uuid;
			this.attribute = attributeModified;
		}
		
		public String getName() {
			return this.name;
		}
		
		public UUID getUUID() {
			return this.uuid;
		}
		
		public IAttribute getAttributeModified() {
			return this.attribute;
		}
				
	}
}
