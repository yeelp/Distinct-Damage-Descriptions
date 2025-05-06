package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import electroblob.wizardry.event.SpellCastEvent.Post;
import electroblob.wizardry.spell.Spell;
import net.minecraft.world.World;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.development.AbstractDeveloperModeInfo;

public final class SpellCastInfoCallback extends AbstractDeveloperModeInfo<Post> {

	public SpellCastInfoCallback() {
		super(() -> ModConfig.compat.ebwizardry.showSpellNameInfo);
	}

	@Override
	public boolean shouldFire(Post evt) {
		return true;
	}

	@Override
	public World getWorld(Post evt) {
		return evt.getCaster().world;
	}

	@Override
	protected StringBuilder getInfo(Post evt, StringBuilder sb) {
		Spell spell = evt.getSpell();
		return sb.append(String.format("SPELL CAST: Spell Name: %s (%s)", spell.getDisplayName(), spell.getRegistryName().toString()));
	}

}
