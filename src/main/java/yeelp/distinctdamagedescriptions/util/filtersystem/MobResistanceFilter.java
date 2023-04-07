package yeelp.distinctdamagedescriptions.util.filtersystem;

import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public class MobResistanceFilter implements IDDDFilter {

	private FilterApplicationType type;
	
	private Iterable<FilterOperation> filters;
	
	private MobResistanceCategories cat;
	
	@Override
	public Iterable<FilterOperation> getAppliedFilters() {
		return this.filters;
	}

	@Override
	public FilterApplicationType getApplicationType() {
		return this.type;
	}
	
	public MobResistanceCategories getResistances() {
		return this.cat;
	}

}
