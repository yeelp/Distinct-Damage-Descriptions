package yeelp.distinctdamagedescriptions.util.lib;

public final class DDDMaps {

	private DDDMaps() {
		throw new RuntimeException("DDDMaps not to be instantiated");
	}
	
	public static DamageMap newDamageMap() {
		return new DamageMap();
	}
	
	public static ArmorMap newArmorMap() {
		return new ArmorMap();
	}
	
	public static ResistMap newResistMap() {
		return new ResistMap();
	}
	
	public static final class DamageMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 1800888433080051037L;

		DamageMap() {
			super(() -> 0.0f);
		}
	}
	
	public static final class ArmorMap extends DDDBaseMap<ArmorValues> {
		private static final long serialVersionUID = -7103973048532333006L;

		ArmorMap() {
			super(() -> new ArmorValues());
		}
	}
	
	public static final class ResistMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 7527157629195618664L;

		ResistMap() {
			super(() -> 0.0f);
		}
	}
}
