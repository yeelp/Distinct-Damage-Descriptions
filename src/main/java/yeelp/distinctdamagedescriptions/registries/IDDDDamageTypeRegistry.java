package yeelp.distinctdamagedescriptions.registries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;

public interface IDDDDamageTypeRegistry extends IDDDRegistry<DDDDamageType> {
	/**
	 * Get the display name for a custom damage type
	 * 
	 * @param name the internal name, with the "ddd_" prefix.
	 * @return the display name
	 */
	default String getDisplayName(String name) {
		return get(name).getDisplayName();
	}

	/**
	 * Get the death message for a damage type
	 * 
	 * @param type     type
	 * @param attacker attacking Entity
	 * @param defender defending EntityLivingBase
	 * @return the ITextComponent death message
	 */
	@Nonnull
	@SuppressWarnings("null")
	default ITextComponent getDeathMessageForType(@Nonnull DDDDamageType type, @Nullable Entity attacker, @Nonnull EntityLivingBase defender) {
		boolean hasAttacker = attacker != null;
		String msg = type.getDeathMessage(hasAttacker);
		if(msg == null) {
			return defender.getCombatTracker().getDeathMessage();
		}
		if(hasAttacker) {
			msg = msg.replaceAll("#attacker", attacker.getName());
		}
		msg = msg.replaceAll("#defender", defender.getName());
		return new TextComponentString(msg);
	}
}
