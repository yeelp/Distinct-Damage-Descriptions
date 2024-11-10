package yeelp.distinctdamagedescriptions.integration.baubles.util;

import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDTooltipColourScheme;

public enum BaubleModifierType {
	DAMAGE_MOD {
		@Override
		public boolean doesRespondToEvent(Event evt) {
			return evt.getClass().equals(DetermineDamageEvent.class);
		}

		@Override
		public boolean allowsNegative() {
			return true;
		}
		
		@Override
		public DDDTooltipColourScheme getColourScheme() {
			return ModConfig.compat.baubles.colourScheme;
		}
	},
	RESISTANCE_MOD {
		@Override
		public boolean doesRespondToEvent(Event evt) {
			return evt.getClass().equals(GatherDefensesEvent.class);
		}

		@Override
		public boolean allowsNegative() {
			return true;
		}
		
		@Override
		public DDDTooltipColourScheme getColourScheme() {
			return ModConfig.compat.baubles.colourScheme;
		}
	},
	BRUTE_FORCE {
		@Override
		public boolean doesRespondToEvent(Event evt) {
			return evt.getClass().equals(GatherDefensesEvent.class);
		}

		@Override
		public boolean allowsNegative() {
			return false;
		}
	},
	SLY_STRIKE {
		@Override
		public boolean doesRespondToEvent(Event evt) {
			return evt.getClass().equals(GatherDefensesEvent.class);
		}

		@Override
		public boolean allowsNegative() {
			return false;
		}
	},
	IMMUNITY {
		@Override
		public boolean doesRespondToEvent(Event evt) {
			return evt.getClass().equals(GatherDefensesEvent.class);
		}

		@Override
		public boolean allowsNegative() {
			return false;
		}
	};
	
	public abstract boolean doesRespondToEvent(Event evt);
	
	public abstract boolean allowsNegative();
	
	@SuppressWarnings("static-method")
	public DDDTooltipColourScheme getColourScheme() {
		return DDDTooltipColourScheme.GRAY;
	}
}
