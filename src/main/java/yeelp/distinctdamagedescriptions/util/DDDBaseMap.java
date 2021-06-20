package yeelp.distinctdamagedescriptions.util;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDBaseMap<T> extends NonNullMap<DDDDamageType, T> {

	private static final long serialVersionUID = -7187725991607204803L;

	public DDDBaseMap(T defaultVal) {
		super(defaultVal);
		// TODO Auto-generated constructor stub
	}
}
