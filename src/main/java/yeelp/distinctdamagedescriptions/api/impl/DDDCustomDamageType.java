package yeelp.distinctdamagedescriptions.api.impl;

import yeelp.distinctdamagedescriptions.config.ModConfig;

public class DDDCustomDamageType extends DDDAbstractDamageType {
	
	public enum Source {
		JSON {
			@Override
			public boolean isAllowed() {
				return ModConfig.core.useCustomDamageTypesFromJSON;
			}
		},
		CT {
			@Override
			public boolean isAllowed() {
				return true;
			}
		};
		
		public abstract boolean isAllowed();
	}
	
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
		return this.src.isAllowed();
	}
}
