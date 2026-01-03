package yeelp.distinctdamagedescriptions.integration.capability;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public abstract class ModUpdatingDamageDistribution extends DamageDistribution {

	protected ModUpdatingDamageDistribution() {
		super();
	}
	
	protected ModUpdatingDamageDistribution(Map<DDDDamageType, Float> map) {
		super(map);
	}
	
	@Override
	public final IDamageDistribution update(EntityLivingBase owner) {
		this.update(owner, this::getUpdatedWeights);
		return super.update(owner);
	}
	
	@Override
	public final IDamageDistribution update(IProjectile owner) {
		this.update(owner, this::getUpdatedWeights);
		return super.update(owner);
	}
	
	@Override
	public final IDamageDistribution update(ItemStack owner) {
		this.update(owner, this::getUpdatedWeights);
		return super.update(owner);
	}
	
	protected abstract Optional<DDDBaseMap<Float>> getUpdatedWeights(ItemStack owner);
	
	protected abstract Optional<DDDBaseMap<Float>> getUpdatedWeights(IProjectile owner);
	
	protected abstract Optional<DDDBaseMap<Float>> getUpdatedWeights(EntityLivingBase owner);
	
	private <T> void update(T t, Function<T, Optional<DDDBaseMap<Float>>> f) {
		f.apply(t).filter((m) -> !this.originalMap.entrySet().equals(m.entrySet())).ifPresent((m) -> {
			this.setNewWeights(m);
			this.updateOriginalWeightsToCurrentWeights();
			//clear modifiers to recompute after changing weights
			this.getModifiers().clear();
		});
	}
}
