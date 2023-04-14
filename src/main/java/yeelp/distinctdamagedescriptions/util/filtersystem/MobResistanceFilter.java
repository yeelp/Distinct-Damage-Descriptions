package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.Collection;

import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public class MobResistanceFilter implements IDDDFilter {

	private FilterApplicationType type;
	
	private FilterCombinationMethod method;
	
	private Collection<FilterOperation> filters;
	
	private MobResistanceCategories cat;
	
	@Override
	public Collection<FilterOperation> getAppliedFilters() {
		return this.filters;
	}

	@Override
	public FilterApplicationType getApplicationType() {
		return this.type;
	}
	
	public MobResistanceCategories getResistances() {
		return this.cat;
	}

	@Override
	public FilterCombinationMethod getCombinationMethod() {
		return this.method;
	}

}
