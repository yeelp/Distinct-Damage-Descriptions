package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.Tuple;

public final class DamageTypeData
{
	public static final DamageTypeData NORMAL = new DamageTypeData();
	private Set<String> items, directSources, indirectSources;
	private String name;
	private boolean includeAll;
	private boolean noSource;
	
	private DamageTypeData()
	{
		this("normal", new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), false, false);
	}
	
	public DamageTypeData(String name, Set<String> directSources, Set<String> indirectSources, Set<String> items, boolean includeAll, boolean noSource)
	{
		this.items = items;
		this.directSources = directSources;
		this.indirectSources = indirectSources;
		this.name = "ddd_"+name;
		this.includeAll = includeAll;
		this.noSource = noSource;
	}
	
	
}
