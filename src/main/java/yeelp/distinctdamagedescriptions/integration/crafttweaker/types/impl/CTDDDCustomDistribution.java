package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.CoTDDDDistributionBuilder.IsContextApplicable;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDCustomDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class CTDDDCustomDistribution extends CTDamageDistribution implements ICTDDDCustomDistribution {

	private final IsContextApplicable checkContext;
	private final String name;
	private final int priority;
	
	public CTDDDCustomDistribution(String name, DDDBaseMap<Float> map, IsContextApplicable ctx, int priority) {
		super(new DamageDistribution(map));
		this.checkContext = ctx;
		this.name = name;
		this.priority = priority;
	}
	
	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
		return this.valid(src, target) ? this.getDist().getCategories() : Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nonnull DamageSource src, @Nonnull EntityLivingBase target) {
		return this.valid(src, target) ? Optional.of((IDamageDistribution) this.getDist()) : Optional.empty();
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public int priority() {
		return this.priority;
	}

	private boolean valid(DamageSource src, EntityLivingBase target) {
		return this.checkContext.handle(this, CraftTweakerMC.getIDamageSource(src), CraftTweakerMC.getIEntityLivingBase(target));
	}

}
