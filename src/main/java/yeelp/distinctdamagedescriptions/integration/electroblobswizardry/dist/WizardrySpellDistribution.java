package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.util.IElementalDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public final class WizardrySpellDistribution extends DDDAbstractPredefinedDistribution {

	private final IDamageDistribution dist;
	private final String wizardryType;
	public WizardrySpellDistribution(String type, IDamageDistribution dist) {
		super(IntegrationTitles.WIZARDRY_NAME + ": "+type.toString(), Source.BUILTIN);
		this.dist = dist;
		this.wizardryType = type;
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		return this.isSpell(src) ? this.dist.getCategories() : Sets.newHashSet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return Optional.ofNullable(this.isSpell(src) ? this.dist : null);
	}
	
	private boolean isSpell(DamageSource src) {
		if(src.getImmediateSource() instanceof ISummonedCreature && src.damageType.equals("mob")) {
			return false;
		}
		if(!(src instanceof IElementalDamage)) {
			return false;
		}
		if(Wizardry.settings.damageTypePerElement) {
			return src.damageType.split("_")[0].equals(this.wizardryType);
		}
		return ((IElementalDamage) src).getType().name().equalsIgnoreCase(this.wizardryType);
	}
	
	@Override
	public int priority() {
		return -1;
	}

}
