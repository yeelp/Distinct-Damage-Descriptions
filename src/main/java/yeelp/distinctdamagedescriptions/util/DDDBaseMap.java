package yeelp.distinctdamagedescriptions.util;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDBaseMap<T> extends NonNullMap<DDDDamageType, T> {
	public DDDBaseMap(T defaultVal) {
		super(defaultVal);
		// TODO Auto-generated constructor stub
	}
}
