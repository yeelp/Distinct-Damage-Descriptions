package yeelp.distinctdamagedescriptions.util;

import java.util.Optional;

import javax.annotation.Nullable;

public final class DDDFontColour {

	/**
	 * This marker denotes the beginning and end of a DDD string
	 * 
	 * @author Yeelp
	 *
	 */
	public static enum Marker {
		START('\u222b') {
			@Override
			public String replaceWith() {
				return "";
			}
		},
		END('\u222e') {
			@Override
			public String replaceWith() {
				return "";
			}
		},
		SPACE('\u222c') {
			@Override
			public String replaceWith() {
				return " ";
			}
		};

		private final char c;

		private Marker(char c) {
			this.c = c;
		}

		/**
		 * @return the character used for encoding
		 */
		public char getC() {
			return this.c;
		}

		/**
		 * Determine the type of Marker
		 * 
		 * @param c character to check against
		 * @return An {@link Optional} with the Marker that has a character matching the
		 *         supplied character, or {@link Optional#empty()} if no match was made.
		 */
		public static Optional<Marker> getMarker(char c) {
			for(Marker m : Marker.values()) {
				if(m.c == c) {
					return Optional.of(m);
				}
			}
			return Optional.empty();
		}

		/**
		 * What to replace this marker with in a string. By default, this returns an
		 * empty string.
		 * 
		 * @return The String to replace all marker occurrences with.
		 */
		public abstract String replaceWith();
	}

	private DDDFontColour() {
	}

	public static String encodeColour(int colour) {
		int r = ((colour >> 16) & 255);
		int g = ((colour >> 8) & 255);
		int b = ((colour >> 0) & 255);
		return String.format("%c%c%c%c", Marker.START.getC(), encode(r), encode(g), encode(b));
	}

	/**
	 * Decode a type's encoded text colour information.
	 * 
	 * @param type The type with encoded text colour information.
	 * @return a short array with values {r, g, b} for colour information, or null
	 *         if the provided string had no colour information that could be
	 *         decoded.
	 */
	@Nullable
	public static short[] decodeColour(String type) {
		byte colourState = 0;
		short[] colour = new short[3];
		for(char c : type.toCharArray()) {
			if(colourState > 0) {
				short val = (short) (c & 0xFF);
				colour[colourState++ - 1] = val;
				if((colourState %= 4) == 0) {
					return colour;
				}
			}
			else {
				colourState += DDDFontColour.Marker.getMarker(c).filter(Marker.START::equals).map((marker) -> 1).orElse(0).byteValue();
			}
		}
		return null;
	}

	private static char encode(int i) {
		return (char) (0x4700 + (i & 0xFF));
	}
}
