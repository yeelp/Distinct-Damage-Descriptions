package yeelp.distinctdamagedescriptions.util.filtersystem;

public final class InDimensionFilter extends SimpleFilterOperation {

	public InDimensionFilter(int dim) {
		super((e) -> e.dimension == dim);
	}
}
