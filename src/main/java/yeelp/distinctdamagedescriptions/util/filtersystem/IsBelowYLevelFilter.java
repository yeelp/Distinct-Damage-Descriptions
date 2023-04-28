package yeelp.distinctdamagedescriptions.util.filtersystem;

public final class IsBelowYLevelFilter extends SimpleFilterOperation {

	public IsBelowYLevelFilter(int y) {
		super((e) -> e.posY < y);
	}

}
