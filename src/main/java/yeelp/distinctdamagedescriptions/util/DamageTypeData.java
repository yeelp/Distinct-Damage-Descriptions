package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;
import java.util.Set;

public final class DamageTypeData {
	public static final DamageTypeData NORMAL = new DamageTypeData();
	private Set<String> directSources, indirectSources;
	private String originalSource;
	private boolean includeAll;

	private DamageTypeData() {
		this("", new HashSet<String>(), new HashSet<String>(), false);
	}

	public DamageTypeData(String name, Set<String> directSources, Set<String> indirectSources, boolean includeAll) {
		this.directSources = directSources;
		this.indirectSources = indirectSources;
		this.originalSource = name;
		this.includeAll = includeAll;
	}

	public String getOriginalSource() {
		return this.originalSource;
	}

	public Set<String> getDirectSources() {
		return this.directSources;
	}

	public Set<String> getIndirectSources() {
		return this.indirectSources;
	}

	public boolean includeAll() {
		return this.includeAll;
	}
}
