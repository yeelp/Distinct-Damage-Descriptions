package yeelp.distinctdamagedescriptions.integration.thebewteenlands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public final class BetweenlandsShieldHandler extends Handler {
	private final Field ignoreEvent;
	private final Object blHandlerInstance;
	private final Method canBlockMethod;
	private boolean wasCanceled = false;
	
	private static BetweenlandsShieldHandler instance;
	
	public static BetweenlandsShieldHandler getInstance() throws NoSuchFieldException, SecurityException, IllegalAccessException, NoSuchMethodException {
		return instance == null ? instance = new BetweenlandsShieldHandler() : instance;
	}
	
	private BetweenlandsShieldHandler() throws NoSuchFieldException, SecurityException, IllegalAccessException, NoSuchMethodException {
		Field instanceField = TheBetweenlandsCompat.getBLShieldHandlerClass().getDeclaredField("INSTANCE");
		instanceField.setAccessible(true);
		(this.ignoreEvent = TheBetweenlandsCompat.getBLShieldHandlerClass().getDeclaredField("ignoreEvent")).setAccessible(true);
		this.blHandlerInstance = instanceField.get(null);
		this.canBlockMethod = TheBetweenlandsCompat.getBLShieldClass().getDeclaredMethod("canBlockDamageSource", ItemStack.class, EntityLivingBase.class, EnumHand.class, DamageSource.class);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public final void onAttackFirst(LivingAttackEvent evt) {
		try {
			if(this.ignoreEvent.getBoolean(this.blHandlerInstance)) {
				return;
			}			
		}
		catch(IllegalAccessException e) {
			return;
		}
		EntityLivingBase defender = evt.getEntityLiving();
		for(EnumHand hand : EnumHand.values()) {
			ItemStack stack = defender.getHeldItem(hand);
			if(!stack.isEmpty() && TheBetweenlandsCompat.getBLShieldClass().isInstance(stack.getItem())) {
				try {
					if((Boolean) this.canBlockMethod.invoke(TheBetweenlandsCompat.getBLShieldClass().cast(stack.getItem()), stack, defender, hand, evt.getSource())) {
						if(!defender.world.isRemote) {
							this.wasCanceled = true;							
						}
					}
				}
				//this shouldn't happen
				catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
	public void onAttackAfter(LivingAttackEvent evt) {
		if(this.wasCanceled && evt.isCanceled()) {
			this.wasCanceled = false;
			evt.setCanceled(false);
		}
	}
}
