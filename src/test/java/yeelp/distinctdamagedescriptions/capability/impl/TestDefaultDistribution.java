package yeelp.distinctdamagedescriptions.capability.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.ArmorDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.distributors.ShieldDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.impl.TestDefaultDistribution.CompatTest.Type;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

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

	protected static final ItemStack MOCK_APPLE = mock(ItemStack.class);
	protected static final ItemStack MOCK_CHESTPLATE = mock(ItemStack.class);
	protected static final ItemStack MOCK_SHIELD = mock(ItemStack.class);
	protected static final ItemStack MOCK_NO_CAPS_STACK = mock(ItemStack.class);
	protected static final ItemStack MOCK_ALL_CAPS_STACK = mock(ItemStack.class);
	protected static final Item MOCK_ITEM_APPLE = mock(Item.class);
	protected static final ItemArmor MOCK_ITEM_CHESTPLATE = mock(ItemArmor.class);
	protected static final ItemShield MOCK_ITEM_SHIELD = mock(ItemShield.class);
	protected static final EntityLivingBase MOCK_NO_CAPS_ELB = mock(EntityLivingBase.class);
	protected static final EntityLivingBase MOCK_ALL_CAPS_ELB = mock(EntityLivingBase.class);
	protected static final EntityArrow MOCK_NO_CAPS_PROJECTILE = mock(EntityArrow.class);
	protected static final EntityArrow MOCK_ALL_CAPS_PROJECTILE = mock(EntityArrow.class);
	protected static final EntityPlayer MOCK_PLAYER = mock(EntityPlayer.class);

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
		
		setUpMock(MOCK_APPLE, MOCK_ITEM_APPLE, "minecraft:apple");
		setUpMock(MOCK_CHESTPLATE, MOCK_ITEM_CHESTPLATE, "minecraft:iron_chestplate");
		setUpMock(MOCK_SHIELD, MOCK_ITEM_SHIELD, "minecraft:shield");

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
	
	private static final void setUpMock(ItemStack mockStack, Item mockItem, String key) {
		when(mockStack.getItem()).thenReturn(mockItem);
		when(mockItem.getRegistryName()).thenReturn(new ResourceLocation(key));
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
	
	@SuppressWarnings("static-method")
	@Test
	void testNoCapEntityGivesDefault() {
		assertEquals(DefaultDamageDistribution.getInstance(), DDDAPI.accessor.getDamageDistribution(MOCK_NO_CAPS_ELB).get());
	}
	
	@SuppressWarnings("static-method")
	@Test
	void testNoCapProjectileGivesDefault() {
		assertEquals(DefaultDamageDistribution.getInstance(), DDDAPI.accessor.getDamageDistribution(MOCK_NO_CAPS_PROJECTILE).get());
	}
	
	@SuppressWarnings("static-method")
	@CompatTest(compat = true, type = Type.ITEM)
	void testDefinedOnlyGivesDefaultForNonDefinedItem() {
		Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> caps = DDDCapabilityDistributors.getCapabilities(MOCK_APPLE).get();
		assertEquals(1, caps.size());
		assertNull(caps.values().iterator().next());
		assertEquals(DefaultDamageDistribution.getInstance(), DDDAPI.accessor.getDamageDistribution(MOCK_APPLE).get());
	}
	
	@SuppressWarnings("static-method")
	@CompatTest(compat = false, type = Type.ITEM)
	void testNotDefinedOnlyGivesBludgeoningForNonDefinedItem() {
		Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> caps = DDDCapabilityDistributors.getCapabilities(MOCK_APPLE).get();
		assertEquals(1, caps.size());
		DDDCapabilityBase<?> cap = caps.values().iterator().next();
		assertNotNull(cap);
		assertTrue(cap instanceof IDamageDistribution);
		IDamageDistribution dist = (IDamageDistribution) cap;
		assertNotEquals(DefaultDamageDistribution.getInstance(), dist);
		assertEquals(1.0f, dist.getWeight(DDDBuiltInDamageType.BLUDGEONING));
	}
	
	@SuppressWarnings("static-method")
	@CompatTest(compat = true, type = Type.ITEM)
	void testDefinedOnlyGivesDefaultForNonDefinedArmor() {
		Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> caps = DDDCapabilityDistributors.getCapabilities(MOCK_CHESTPLATE).get();
		assertEquals(2, caps.size());
		assertTrue(caps.containsKey(ArmorDistributionCapabilityDistributor.LOC));
		assertEquals(DefaultArmorDistribution.getInstance(), DDDAPI.accessor.getArmorResistances(MOCK_CHESTPLATE).get());
	}
	
	@SuppressWarnings("static-method")
	@CompatTest(compat = true, type = Type.ITEM)
	void testDefinedOnlyGivesDefaultForNonDefinedShield() {
		Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> caps = DDDCapabilityDistributors.getCapabilities(MOCK_SHIELD).get();
		assertEquals(2, caps.size());
		assertTrue(caps.containsKey(ShieldDistributionCapabilityDistributor.LOC));
		assertEquals(DefaultShieldDistribution.getInstance(), DDDAPI.accessor.getShieldDistribution(MOCK_SHIELD).get());
	}
	
	@SuppressWarnings("static-method")
	@CompatTest(compat = true, type = Type.ENTITY)
	void testNotDefinedStillBehavesNormallyForPlayer() {
		Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>> caps = DDDCapabilityDistributors.getPlayerCapabilities(MOCK_PLAYER);
		assertEquals(2, caps.size());
		for(DDDCapabilityBase<?> cap : caps.values()) {
			if(cap instanceof IDamageDistribution) {
				assertNotEquals(DefaultDamageDistribution.getInstance(), cap);
			}
		}
	}
}
