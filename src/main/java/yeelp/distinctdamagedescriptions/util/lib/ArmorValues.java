package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Comparator;

import javax.annotation.Nullable;

import net.minecraft.util.Tuple;

public final class ArmorValues implements Comparable<ArmorValues> {
	private static final Comparator<ArmorValues> ARMOR_VALUES_COMPARATOR = Comparator.comparingDouble(ArmorValues::getArmor).thenComparingDouble(ArmorValues::getToughness);
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
	
	public static ArmorValues merge(@Nullable ArmorValues a1, @Nullable ArmorValues a2) {
		if(a1 == null && a2 == null) {
			return new ArmorValues();
		}
		if(a1 == null) {
			return a2;
		}
		if(a2 == null) {
			return a1;
		}
		return a1.setValues(a1.armor + a2.armor, a1.toughness + a2.toughness);
	}

	@Override
	public int compareTo(ArmorValues o) {
		return ARMOR_VALUES_COMPARATOR.compare(this, o);
	}
}
