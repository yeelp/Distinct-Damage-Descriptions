package yeelp.distinctdamagedescriptions.util;

import java.util.Optional;

public final class DDDFontColour {

	/**
	 * This marker denotes the beginning and end of a DDD string
	 * 
	 * @author Yeelp
	 *
	 */
	public static enum Marker {
		START('\u222b'),
		END('\u222e'),
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
		static Optional<Marker> getMarker(char c) {
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
		public String replaceWith() {
			return "";
		}
	}

	private DDDFontColour() {
	}

	public static String encodeColour(int colour) {
		int r = ((colour >> 16) & 255);
		int g = ((colour >> 8) & 255);
		int b = ((colour >> 0) & 255);
		return String.format("%c%c%c%c", Marker.START.c, encode(r), encode(g), encode(b));
	}

	private static char encode(int i) {
		return (char) (0x4700 + (i & 0xFF));
	}
}
