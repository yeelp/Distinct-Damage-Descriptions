package yeelp.distinctdamagedescriptions.util.filtersystem;

import net.minecraft.entity.EnumCreatureAttribute;

public abstract class IsVanillaCreatureAttributeFilter extends SimpleFilterOperation {

	protected IsVanillaCreatureAttributeFilter(EnumCreatureAttribute attribute) {
		super((e) -> e.getCreatureAttribute() == attribute);
	}

	public static final class IsUndeadMob extends IsVanillaCreatureAttributeFilter {
		public IsUndeadMob() {
			super(EnumCreatureAttribute.UNDEAD);
		}
	}
	
	public static final class IsArthopodMob extends IsVanillaCreatureAttributeFilter {
		public IsArthopodMob() {
			super(EnumCreatureAttribute.ARTHROPOD);
		}
	}
	
	public static final class IsIllagerMob extends IsVanillaCreatureAttributeFilter {
		public IsIllagerMob() {
			super(EnumCreatureAttribute.ILLAGER);
		}
	}
	
	public static final class IsUndefinedMob extends IsVanillaCreatureAttributeFilter {
		public IsUndefinedMob() {
			super(EnumCreatureAttribute.UNDEFINED);
		}
	}
}
