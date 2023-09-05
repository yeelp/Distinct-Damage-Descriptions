package yeelp.distinctdamagedescriptions.capability.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

public class TestDefaultDistribution {

	@Test
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected static @interface CompatTest {
		boolean compat();

		Type type();

		enum Type {
			ITEM {
				@Override
				void setup(boolean val) {
					ModConfig.compat.definedItemsOnly = val;
				}
			},
			ENTITY {
				@Override
				void setup(boolean val) {
					ModConfig.compat.definedEntitiesOnly = val;
				}
			};

			abstract void setup(boolean val);
		}
	}

	protected static final ItemStack MOCK_NO_CAPS_STACK = mock(ItemStack.class);
	protected static final ItemStack MOCK_ALL_CAPS_STACK = mock(ItemStack.class);
	protected static final EntityLivingBase MOCK_NO_CAPS_ELB = mock(EntityLivingBase.class);
	protected static final EntityLivingBase MOCK_ALL_CAPS_ELB = mock(EntityLivingBase.class);
	protected static final EntityArrow MOCK_NO_CAPS_PROJECTILE = mock(EntityArrow.class);
	protected static final EntityArrow MOCK_ALL_CAPS_PROJECTILE = mock(EntityArrow.class);

	@SuppressWarnings("unchecked")
	@BeforeAll
	static void setup() {
		ModConfig.core.suppressRegistrationInfo = true;
		DDDRegistries.damageTypes = new DDDDamageTypes();
		DDDConfigurations.init();
		DDDAPI.init();
		DDDConfigLoader.readConfig();
		IDamageDistribution acidCold = new DamageDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.ACID, 0.5f), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.COLD, 0.5f));
		when(MOCK_NO_CAPS_STACK.getCapability(any(), isNull())).thenReturn(null);

		when(MOCK_ALL_CAPS_STACK.getCapability(any(Capability.class), isNull())).then(new Answer<DDDCapabilityBase<? extends NBTBase>>() {
			@Override
			public DDDCapabilityBase<? extends NBTBase> answer(InvocationOnMock arg0) throws Throwable {
				Capability<?> arg = arg0.getArgument(0);
				if(arg == DamageDistribution.cap) {
					return acidCold;
				}
				if(arg == ShieldDistribution.cap) {
					return new ShieldDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.ACID, 0.3f));
				}
				if(arg == ArmorDistribution.cap) {
					return new ArmorDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.ACID, 0.6f));
				}
				return null;
			}

		});

		when(MOCK_NO_CAPS_ELB.getCapability(any(), isNull())).thenReturn(null);

		when(MOCK_ALL_CAPS_ELB.getCapability(any(Capability.class), isNull())).then(new Answer<DDDCapabilityBase<? extends NBTBase>>() {
			@Override
			public DDDCapabilityBase<? extends NBTBase> answer(InvocationOnMock arg0) throws Throwable {
				Capability<?> arg = arg0.getArgument(0);
				if(arg == DamageDistribution.cap) {
					return acidCold;
				}
				if(arg == MobResistances.cap) {
					return new MobResistances(new DDDBaseMap<Float>(() -> 0.0f), Collections.singleton(DDDBuiltInDamageType.ACID), true, 0.3f);
				}
				return null;
			}

		});

		when(MOCK_NO_CAPS_PROJECTILE.getCapability(any(), isNull())).thenReturn(null);

		when(MOCK_ALL_CAPS_PROJECTILE.getCapability(eq(DamageDistribution.cap), isNull())).thenReturn(acidCold);
	}

	@SuppressWarnings("static-method")
	@BeforeEach
	final void before(TestInfo info) {
		info.getTestMethod().map((m) -> m.getDeclaredAnnotation(CompatTest.class)).filter(Objects::nonNull).ifPresent((ct) -> {
			ct.type().setup(ct.compat());
		});
	}

	@SuppressWarnings("static-method")
	@Test
	void testNoCapStackGivesDefault() {
		assertEquals(DefaultDamageDistribution.getInstance(), DDDAPI.accessor.getDamageDistribution(MOCK_NO_CAPS_STACK).get());
	}

}
