package yeelp.distinctdamagedescriptions.util.filtersystem;

import net.minecraft.entity.EnumCreatureType;

public abstract class IsVanillaCreatureTypeFilter extends SimpleFilterOperation {
	
	protected IsVanillaCreatureTypeFilter(EnumCreatureType type) {
		super((e) -> e.isCreatureType(type, false));
	}
	
	public static final class IsMonsterCreatureType extends IsVanillaCreatureTypeFilter {
		public IsMonsterCreatureType() {
			super(EnumCreatureType.MONSTER);
		}
	}

	public static final class IsWaterCreatureType extends IsVanillaCreatureTypeFilter {
		public IsWaterCreatureType() {
			super(EnumCreatureType.WATER_CREATURE);
		}
	}
	
	public static final class IsAnimalCreatureType extends IsVanillaCreatureTypeFilter {
		public IsAnimalCreatureType() {
			super(EnumCreatureType.CREATURE);
		}
	}
	
	public static final class IsAmbientCreatureType extends IsVanillaCreatureTypeFilter {
		public IsAmbientCreatureType() {
			super(EnumCreatureType.AMBIENT);
		}
	}

}
