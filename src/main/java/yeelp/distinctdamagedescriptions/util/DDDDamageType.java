package yeelp.distinctdamagedescriptions.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public final class DDDDamageType extends DamageSource
{
	private Set<String> types;
	private DamageSource parentSource;
	/**
	 * Make a new DDDDamageType
	 * @param parentSource parent source
	 * @param types extended types.
	 */
	public DDDDamageType(DamageSource parentSource, String... types)
	{
		super(parentSource.damageType);
		this.parentSource = parentSource;
		this.types = new HashSet<String>(Arrays.asList(types));
		this.types.remove("ddd_normal");
	}
	
	/**
	 * Get all extended types.
	 * @return
	 */
	public Set<String> getExtendedTypes()
	{
		return this.types;
	}
	
	//The following is copied and altered from DamageSource to wrap it correctly
	
	/**
     * Returns true if the damage is projectile based.
     */
	@Override
    public boolean isProjectile()
    {
        return parentSource.isProjectile();
    }

    /**
     * Define the damage type as projectile based.
     */
	@Override
    public DamageSource setProjectile()
    {
        this.parentSource = this.parentSource.setProjectile();
        return this;
    }

	@Override
    public boolean isExplosion()
    {
        return parentSource.isExplosion();
    }

	@Override
    public DamageSource setExplosion()
    {
    	this.parentSource = this.parentSource.setExplosion();
        return this;
    }

	@Override
    public boolean isUnblockable()
    {
        return this.parentSource.isUnblockable();
    }

    /**
     * How much satiate(food) is consumed by this DamageSource
     */
	@Override
    public float getHungerDamage()
    {
        return this.parentSource.getHungerDamage();
    }

	@Override
    public boolean canHarmInCreative()
    {
        return this.parentSource.canHarmInCreative();
    }

    /**
     * Whether or not the damage ignores modification by potion effects or enchantments.
     */
	@Override
    public boolean isDamageAbsolute()
    {
        return this.parentSource.isDamageAbsolute();
    }
    
    /**
     * Retrieves the immediate causer of the damage, e.g. the arrow entity, not its shooter
     */
	@Override
    @Nullable
    public Entity getImmediateSource()
    {
        return this.parentSource.getImmediateSource();
    }

    /**
     * Retrieves the true causer of the damage, e.g. the player who fired an arrow, the shulker who fired the bullet,
     * etc.
     */
	@Override
    @Nullable
    public Entity getTrueSource()
    {
        return this.parentSource.getTrueSource();
    }
	
	@Override
    public DamageSource setDamageBypassesArmor()
    {
        this.parentSource = this.parentSource.setDamageBypassesArmor();
        return this;
    }

	@Override
    public DamageSource setDamageAllowedInCreativeMode()
    {
    	this.parentSource = this.parentSource.setDamageAllowedInCreativeMode();
        return this;
    }

    /**
     * Sets a value indicating whether the damage is absolute (ignores modification by potion effects or enchantments),
     * and also clears out hunger damage.
     */
	@Override
    public DamageSource setDamageIsAbsolute()
    {
    	this.parentSource = this.parentSource.setDamageIsAbsolute();
        return this;
    }

    /**
     * Define the damage type as fire based.
     */
	@Override
    public DamageSource setFireDamage()
    {
    	this.parentSource = this.parentSource.setFireDamage();
        return this;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
	@Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
    	//return this.parentSource.getDeathMessage(entityLivingBaseIn);
		if(entityLivingBaseIn.getAttackingEntity() == null)
		{
			return new TextComponentString("The world made an example of "+entityLivingBaseIn.getDisplayName().getFormattedText());
		}
		return new TextComponentString(entityLivingBaseIn.getAttackingEntity().getDisplayName().getFormattedText()+" made an example of "+entityLivingBaseIn.getDisplayName().getFormattedText());
    }

    /**
     * Returns true if the damage is fire based.
     */
	@Override
    public boolean isFireDamage()
    {
        return this.parentSource.isFireDamage();
    }

    /**
     * Return the name of damage type. This is from the original DamageSource class.
     */
	@Override
    public String getDamageType()
    {
        return this.parentSource.damageType;
    }

    /**
     * Set whether this damage source will have its damage amount scaled based on the current difficulty.
     */
	@Override
    public DamageSource setDifficultyScaled()
    {
    	this.parentSource = this.parentSource.setDifficultyScaled();
        return this;
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
	@Override
    public boolean isDifficultyScaled()
    {
        return this.parentSource.isDifficultyScaled();
    }

    /**
     * Returns true if the damage is magic based.
     */
	@Override
    public boolean isMagicDamage()
    {
        return this.parentSource.isMagicDamage();
    }

    /**
     * Define the damage type as magic based.
     */
	@Override
    public DamageSource setMagicDamage()
    {
    	this.parentSource = this.parentSource.setMagicDamage();
        return this;
    }

	@Override
    public boolean isCreativePlayer()
    {
        return this.parentSource.isCreativePlayer();
    }

    /**
     * Gets the location from which the damage originates.
     */
    @Nullable
    @Override
    public Vec3d getDamageLocation()
    {
        return this.parentSource.getDamageLocation();
    }
}
