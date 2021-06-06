package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.Tuple;

public final class ArmorValues {
	private float armor, toughness;

	public ArmorValues() {
		this(0.0f, 0.0f);
	}

	public ArmorValues(float armor, float toughness) {
		setValues(armor, toughness);
	}

	public ArmorValues(Tuple<Float, Float> t) {
		this.armor = t.getFirst();
		this.toughness = t.getSecond();
	}

	public float getArmor() {
		return this.armor;
	}

	public float getToughness() {
		return this.toughness;
	}

	public ArmorValues setValues(float armor, float toughness) {
		this.armor = armor;
		this.toughness = toughness;
		return this;
	}

	public static ArmorValues merge(ArmorValues a1, ArmorValues a2) {
		return a1.setValues(a1.armor + a2.armor, a1.toughness + a2.toughness);
	}
}
