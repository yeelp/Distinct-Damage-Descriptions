package yeelp.distinctdamagedescriptions.api.impl;

import yeelp.distinctdamagedescriptions.config.ModConfig;

public class DDDCustomDamageType extends DDDAbstractDamageType {
	
	private final Source src;
	
	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour) {
		this(name, displayName, isPhysical, deathAttackerMessage, deathMessage, colour, false, Source.JSON);
	}
	
	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour, boolean hidden) {
		this(name, displayName, isPhysical, deathAttackerMessage, deathMessage, colour, hidden, Source.JSON);
	}

	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour, boolean hidden, Source src) {
		super(name, isPhysical, deathAttackerMessage, deathMessage, colour, hidden);
		this.displayName = displayName;
		this.src = src;
	}

	@Override
	public boolean isCustomDamage() {
		return true;
	}

	@Override
	public boolean isUsable() {
		return this.src != Source.JSON || ModConfig.core.useCustomDamageTypesFromJSON;
	}
	
	@Override
	public Source getCreationSource() {
		return this.src;
	}
	
	@Override
	protected void initialize() {
		return;
	}
}
