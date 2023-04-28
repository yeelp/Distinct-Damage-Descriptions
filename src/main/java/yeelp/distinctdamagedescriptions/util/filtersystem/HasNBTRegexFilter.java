package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.regex.Pattern;

public class HasNBTRegexFilter extends SimpleFilterOperation {

	protected HasNBTRegexFilter(Pattern regex) {
		super((e) -> regex.asPredicate().test(e.serializeNBT().toString()));
	}

}
