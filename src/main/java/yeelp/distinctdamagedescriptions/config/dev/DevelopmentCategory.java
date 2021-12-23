package yeelp.distinctdamagedescriptions.config.dev;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public final class DevelopmentCategory {
	public enum DeveloperStatus {
		DISABLED(false, false),
		ENABLED(true, false),
		ENABLED_AND_IN_CHAT(true, true);

		private boolean allowed, inChat;

		private DeveloperStatus(boolean allowed, boolean inChat) {
			this.allowed = allowed;
			this.inChat = inChat;
		}

		public boolean isEnabled() {
			return this.allowed;
		}

		public boolean sendInfoToChat() {
			return this.inChat;
		}
	}

	@Name("Enabled")
	@Comment({
			"A blanket switch to turn off all features. If true, features will follow their original configured behaviour. A warning message is given to all players who join worlds while true.",
			"This is to make sure pack makers are aware of when the development tools are on or not.",
			"To minimize log spam (and to actually be useful), it is recommended to playtest in a very controlled environment (void superflat world) when this feature is on, so the console doesn't get flooded with messages."})
	public boolean enabled = false;

	@Name("Show Attack Info")
	@Comment({
			"If enabled, DDD will log information whenever a LivingAttackEvent is fired (when an entity is attacked, but before damage calculations begin). DDD will log the attacker name and id, defender name and id, original damage source, and damage amount",
			"Useful if you need to know what damage source names are.",
			"Setting this to ENABLED_AND_IN_CHAT will additionally send this information to the in game chat (i.e. to all players in game)"})
	public DeveloperStatus showAttackInfo = DeveloperStatus.DISABLED;

	@Name("Show Hurt Info")
	@Comment({
			"If enabled, DDD will log information whenever a LivingAttackEvent is fired (when an entity is about to take damage, and when DDD reduces damage via shields and applies armor bonuses). DDD will log the armor bonuses that the entity gets for that attack (as a result of their armor distributions), along with the current damage.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showHurtInfo = DeveloperStatus.DISABLED;

	@Name("Show Damage Info")
	@Comment({
			"If enabled, DDD will log information whenever a LivingDamageEvent is fired (when an entity is about to take damage, after vanilla armor and potion calculations, and when DDD applies mob resistances and immunities). DDD will log the final amount of damage being dealt to the entity after all DDD calculations.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showDamageInfo = DeveloperStatus.DISABLED;

	@Name("Show Damage Classification")
	@Comment({
			"If enabled, DDD will log information whenever it determines and classifies damage. DDD will display the different damage types inflicted, along with their damage amounts.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showDamageClassification = DeveloperStatus.DISABLED;

	@Name("Show Defense Classification")
	@Comment({
			"If enabled, DDD will log information whenever it gather mob resistances and immunities. DDD will display the resistances and immunities it has found.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showDefenseClassification = DeveloperStatus.DISABLED;

	@Name("Show Shield Calculations")
	@Comment({
			"If enabled, DDD will log information whenever it does shield reductions. If the shield block gets canceled, that gets displayed instead. If not canceled, DDD will display the shield distribution it used for the calculation.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showShieldCalculation = DeveloperStatus.DISABLED;

	@Name("Show Adaptive Calculations")
	@Comment({
			"If enabled, DDD will log information whenever it checks if it should update mob resistances from adaptability. If adaptability is going to take place (forced or naturally), DDD will show what types the mob will be adapting to.",
			"Setting this to ENABED_AND_IN_CHAT will additionally send this information to the in game chat (i.e to all players in game)"})
	public DeveloperStatus showAdaptiveCalculation = DeveloperStatus.DISABLED;
	
	@Name("Show Config Errors on Join")
	@Comment("If enabled, DDD will display config errors for any multi-entry config option in the chat when a player joins the world.")
	public boolean showConfigErrors = false;
}
