package yeelp.distinctdamagedescriptions.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

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
	private static Random particleDisplacement = new Random();

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
			return new double[] {this.x, this.y, this.z};
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
	 * @param player   The attacking player
	 * @param defender the defending EntityLivingBase
	 * @param results  the results from DDD's combat calculations
	 * @param ratio    the ratio of final damage to unaltered damage.
	 * @return true if immunity blocked all damage. I.e.
	 *         {@code results.wasImmunityTriggered() && ratio == 0}
	 */
	public static boolean doEffects(EntityPlayer player, EntityLivingBase defender, CombatResults results, float ratio) {
		List<ParticleInfo> particles = new LinkedList<ParticleInfo>();
		SoundInfo soundInfo = null;
		boolean result = false;
		if(results.wasResistanceHit()) {
			addParticles(defender, DDDParticleType.RESISTANCE, particles);
		}
		if(results.wasWeaknessHit()) {
			addParticles(defender, DDDParticleType.WEAKNESS, particles);
		}
		if(results.wasImmunityTriggered()) {
			addParticles(defender, DDDParticleType.IMMUNITY, particles);
			result = ratio == 0;
			soundInfo = result ? new SoundInfo(player, DDDSounds.IMMUNITY_HIT, 1.5f, 1.0f) : null;
		}
		if(ratio != 0) {
			if(ratio > 1) {
				soundInfo = new SoundInfo(player, DDDSounds.WEAKNESS_HIT, 0.6f, 1.0f);
			}
			else if(ratio < 1) {
				soundInfo = new SoundInfo(player, DDDSounds.RESIST_DING, 1.7f, 1.0f);
			}
		}
		else if (soundInfo == null){
			soundInfo = new SoundInfo(player, DDDSounds.HIGH_RESIST_HIT, 1.7f, 1.0f);
		}
		sendEffectPackets(player, particles, soundInfo);
		return result;
	}

	private static void sendEffectPackets(EntityPlayer player, List<ParticleInfo> types, @Nullable SoundInfo info) {
		if(player instanceof EntityPlayerMP) {
			EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
			if(!types.isEmpty()) {
				PacketHandler.INSTANCE.sendTo(new ParticleMessage(types), mpPlayer);
			}
			if(info != null) {
				info.playSound();
			}
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
