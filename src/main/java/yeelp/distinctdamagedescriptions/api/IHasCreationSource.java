package yeelp.distinctdamagedescriptions.api;

import yeelp.distinctdamagedescriptions.ModConsts;

/**
 * A simple interface that outlines an object with a creation source. Indicates
 * what created this object.
 * 
 * @author Yeelp
 *
 */
public interface IHasCreationSource {

	/**
	 * Get the creation Source as a string. The default implementation uses
	 * {@link #getCreationSource()} and calls {@code toString()} on it, but
	 * implementors are free to use whatever they wish. If you intend on overriding
	 * this, you should return {@link Source#OTHER} in {@link #getCreationSource()}.
	 * 
	 * @return The creation source as a string.
	 */
	default String getCreationSourceString() {
		return this.getCreationSource().toString();
	}

	/**
	 * The source that created this.
	 * @return The source that created this.
	 */
	Source getCreationSource();

	enum Source {
		/**
		 * Built In
		 */
		BUILTIN {
			@Override
			public String toString() {
				return ModConsts.BUILT_IN;
			}
		},
		/**
		 * Created by CraftTweaker
		 */
		CT {
			@Override
			public String toString() {
				return ModConsts.IntegrationTitles.CRAFTWEAKER_TITLE;
			}
		},
		/**
		 * Created by ContentTweaker
		 */
		CoT {
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return ModConsts.IntegrationTitles.CONTENTTWEAKER_TITLE;
			}
		},
		/**
		 * Created by JSON
		 */
		JSON,
		/**
		 * Created by some other source
		 */
		OTHER;
	}
}
