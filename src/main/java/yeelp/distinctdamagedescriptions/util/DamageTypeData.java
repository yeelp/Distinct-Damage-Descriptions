package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.Tuple;

public final class DamageTypeData
{
	public static final DamageTypeData NORMAL = new DamageTypeData();
	private Set<String> directSources, indirectSources;
	private String originalSource;
	private boolean includeAll;
	private boolean noSource;
	
	private DamageTypeData()
	{
		this("", new HashSet<String>(), new HashSet<String>(), false, false);
	}
	
	public DamageTypeData(String name, Set<String> directSources, Set<String> indirectSources, boolean includeAll, boolean noSource)
	{
		this.directSources = directSources;
		this.indirectSources = indirectSources;
		this.originalSource = name;
		this.includeAll = includeAll;
		this.noSource = noSource;
	}	
	
	public String getOriginalSource()
	{
		return originalSource;
	}
	
	public Set<String> getDirectSources()
	{
		return directSources;
	}
	
	public Set<String> getIndirectSources()
	{
		return indirectSources;
	}
}
