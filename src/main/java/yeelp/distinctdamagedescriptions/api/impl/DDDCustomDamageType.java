package yeelp.distinctdamagedescriptions.api.impl;

import yeelp.distinctdamagedescriptions.config.ModConfig;

public class DDDCustomDamageType extends DDDAbstractDamageType {
	
	private final Source src;
	
	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour) {
		this(name, displayName, isPhysical, deathAttackerMessage, deathMessage, colour, Source.JSON);
	}
	
	public DDDCustomDamageType(String name, String displayName, boolean isPhysical, String deathAttackerMessage, String deathMessage, int colour, Source src) {
		super(name, isPhysical, deathAttackerMessage, deathMessage, colour);
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
}
