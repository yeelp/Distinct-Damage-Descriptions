package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Comparator;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import net.minecraft.util.Tuple;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

/**
 * A container for armor values in the order armor, then toughness.
 * 
 * This class' {@link Iterable} implementation iterates in the order armor, then
 * toughness, and only iterates over the two elements.
 * 
 * This class' {@link Comparable} implementation compares armor first, then
 * compares toughness if armor is the same.
 * 
 * @author Yeelp
 *
 */
public class ArmorValues implements Iterable<Float>, Comparable<ArmorValues> {
	private static final Comparator<ArmorValues> ARMOR_VALUES_COMPARATOR = Comparator.comparingDouble(ArmorValues::getArmor).thenComparingDouble(ArmorValues::getToughness);
	private float armor, toughness;

	public static final ArmorValues ZERO = new ArmorValuesZero();

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

	public ArmorValues(ArmorProperties props) {
		this.armor = (float) props.Armor;
		this.toughness = (float) props.Toughness;
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

	public ArmorValues sub(float armor, float toughness) {
		return this.setValues(this.armor - armor, this.toughness - toughness);
	}

	public ArmorValues sub(@Nullable ArmorValues av) {
		if(av == null) {
			return this;
		}
		return this.sub(av.armor, av.toughness);
	}

	public ArmorValues add(float armor, float toughness) {
		return this.setValues(this.armor + armor, this.toughness + toughness);
	}

	public ArmorValues add(@Nullable ArmorValues av) {
		if(av == null) {
			return this;
		}
		return this.add(av.armor, av.toughness);
	}

	public ArmorValues mul(float value) {
		return this.setValues(this.armor * value, this.toughness * value);
	}

	/**
	 * Set this ArmorValues to the absolute value of itself. This is the result of
	 * getting the absolute value of the armor and toughness and setting this
	 * ArmorValues' armor and toughness to those results.
	 * 
	 * @return
	 */
	public ArmorValues abs() {
		return this.setValues(Math.abs(this.armor), Math.abs(this.toughness));
	}

	/**
	 * Merge two ArmorValues together by adding their respective armor and toughness
	 * values. This is a static version of {@link #add(ArmorValues)}.
	 * 
	 * @param a1 The first ArmorValues
	 * @param a2 The second ArmorValues
	 * @return the result of adding the two ArmorValues together
	 */
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

	/**
	 * Combine two ArmorValues together by subtracting {@code a2}'s armor and
	 * toughness from {@code a1}. This is a static version of
	 * {@link #sub(ArmorValues)}.
	 * 
	 * @param a1 The first ArmorValues
	 * @param a2 The second ArmorValues
	 * @return the result of subtracting {@code a2}'s armor and toughness from
	 *         {@code a1}'s.
	 */
	public static ArmorValues sub(@Nullable ArmorValues a1, @Nullable ArmorValues a2) {
		if(a1 == null && a2 == null) {
			return new ArmorValues();
		}
		if(a1 == null) {
			return a2.setValues(-a2.getArmor(), -a2.getToughness());
		}
		if(a2 == null) {
			return a1;
		}
		return a1.setValues(a1.armor - a2.armor, a1.toughness - a2.toughness);
	}

	/**
	 * <em>For specifics on how comparing ArmorValues work, check the end of this
	 * method's documentation.</em>
	 * <p>
	 * {@inheritDoc}
	 * <p>
	 * <b>NOTE</b>: This implementation of compareTo compares ArmorValues by their
	 * armor value first, and then their toughness value.
	 */
	@Override
	public int compareTo(ArmorValues o) {
		return ARMOR_VALUES_COMPARATOR.compare(this, o);
	}

	/**
	 * <em>For specifics on how iterating over ArmorValues work, check the end of
	 * this method's documentation.</em>
	 * <p>
	 * {@inheritDoc}
	 * <p>
	 * <b>NOTE</b>: This iterator iterates over two values in the order: armor, then
	 * toughness. It will only have two values.
	 */
	@Override
	public Iterator<Float> iterator() {
		return Iterators.forArray(this.armor, this.toughness);
	}

	@Override
	public String toString() {
		return String.format("Armor Values: {Armor: %f, Toughness %f}", this.armor, this.toughness);
	}
	
	private static final class ArmorValuesZero extends ArmorValues {
		
		private boolean immutable = false;
		ArmorValuesZero() {
			super();
			this.immutable = true;
		}
		
		@Override
		public ArmorValues setValues(float armor, float toughness) {
			if(this.immutable) {
				throw new UnsupportedOperationException("Altering ArmorValues.ZERO not allowed!");				
			}
			return super.setValues(armor, toughness);
		}
	}
}
