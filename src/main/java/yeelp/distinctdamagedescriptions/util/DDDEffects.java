package yeelp.distinctdamagedescriptions.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticleType;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.network.ParticleMessage;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;

public class DDDEffects {
	private static Random particleDisplacement = new Random(), soundPitch = new Random();

	public static class ParticleInfo {
		private double x, y, z;
		private DDDParticleType type;

		public ParticleInfo(PacketBuffer pakBuf) {
			fromBytes(pakBuf);
		}

		ParticleInfo(DDDParticleType type, double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.type = type;
		}

		public double[] getCoordinates() {
			return new double[] {
					this.x,
					this.y,
					this.z};
		}

		public DDDParticleType getType() {
			return this.type;
		}

		public void toBytes(PacketBuffer pakBuf) {
			pakBuf.writeDouble(this.x);
			pakBuf.writeDouble(this.y);
			pakBuf.writeDouble(this.z);
			pakBuf.writeInt(this.type.ordinal());
		}

		private void fromBytes(PacketBuffer pakBuf) {
			this.x = pakBuf.readDouble();
			this.y = pakBuf.readDouble();
			this.z = pakBuf.readDouble();
			this.type = DDDParticleType.values()[pakBuf.readInt()];
		}
	}

	private static class SoundInfo {
		private SoundEvent evt;
		private float pitch, vol;
		private EntityPlayer target;

		SoundInfo(EntityPlayer target, SoundEvent evt, float vol, float pitch) {
			this.target = target;
			this.evt = evt;
			this.pitch = pitch;
			this.vol = vol;
		}

		boolean playSound() {
			return DDDSounds.playSound(this.target, this.evt, this.vol, this.pitch);
		}
	}

	/**
	 * Play sound effects and spawn particles based on combat results.
	 * 
	 * @param attacker The attacking entity
	 * @param defender the defending EntityLivingBase
	 * @param results  the results from DDD's combat calculations
	 * @return true if immunity blocked all damage. I.e.
	 *         {@code results.wasImmunityTriggered() && ratio == 0}
	 */
	public static void doEffects(Entity attacker, EntityLivingBase defender, CombatResults results) {
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;
			List<ParticleInfo> particles = new LinkedList<ParticleInfo>();
			List<SoundInfo> sounds = Arrays.stream(DDDSounds.DDDCombatSounds.values()).map((sound) -> sound.getSoundIfApplicable(results, true).map((evt) -> new SoundInfo(player, evt, sound.getRecommendedVolume(), 1.0f))).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
			if(results.wasResistanceHit()) {
				addParticles(defender, DDDParticleType.RESISTANCE, particles);
			}
			if(results.wasWeaknessHit()) {
				addParticles(defender, DDDParticleType.WEAKNESS, particles);
			}
			if(results.wasImmunityTriggered()) {
				addParticles(defender, DDDParticleType.IMMUNITY, particles);
			}
			sendEffectPackets(player, particles, sounds);
		}

		if(defender instanceof EntityPlayer) {
			EntityPlayer defendingPlayer = (EntityPlayer) defender;
			sendEffectPackets(defendingPlayer, Collections.emptyList(), Arrays.stream(DDDSounds.DDDCombatSounds.values()).map((sound) -> sound.getSoundIfApplicable(results, false).map((evt) -> new SoundInfo(defendingPlayer, evt, sound.getRecommendedVolume(), 0.8f + soundPitch.nextFloat() * 0.4f))).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()));
		}
	}

	private static void sendEffectPackets(EntityPlayer player, List<ParticleInfo> types, List<SoundInfo> sounds) {
		if(player instanceof EntityPlayerMP) {
			EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
			if(!types.isEmpty()) {
				PacketHandler.INSTANCE.sendTo(new ParticleMessage(types), mpPlayer);
			}
			sounds.forEach(SoundInfo::playSound);
		}
	}

	private static void addParticles(EntityLivingBase origin, DDDParticleType type, List<ParticleInfo> particles) {
		int amount = (int) (2 * Math.random()) + 2;
		for(int i = 0; i < amount; i++) {
			double x = origin.posX + origin.width * particleDisplacement.nextDouble() - origin.width / 2;
			double y = origin.posY + origin.getEyeHeight() + origin.height * particleDisplacement.nextDouble() - origin.height / 2;
			double z = origin.posZ + origin.width * particleDisplacement.nextDouble() - origin.width / 2;
			particles.add(new ParticleInfo(type, x, y, z));
		}
	}
}
