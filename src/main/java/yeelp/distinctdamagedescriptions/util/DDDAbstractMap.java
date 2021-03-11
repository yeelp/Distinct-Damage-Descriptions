package yeelp.distinctdamagedescriptions.util;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public abstract class DDDAbstractMap<T> extends NonNullMap<DDDDamageType, T>
{	
	public DDDAbstractMap(T defaultVal)
	{
		super(defaultVal);
		// TODO Auto-generated constructor stub
	}
}
