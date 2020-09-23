package yeelp.distinctdamagedescriptions.client.render.particle;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConsts;

/**
 * Particle class for DDD
 * @author Yeelp
 *
 */
@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class DDDParticle extends Particle
{
	private static TextureAtlasSprite sprite;
	private DDDParticleType type;
	public DDDParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, DDDParticleType particleType)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.type = particleType;
		
		this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(type.getResourceLocation().toString()));
	}
	
	public DDDParticle(Entity origin, double dx, double dy, double dz, DDDParticleType type)
	{
		this(origin.getEntityWorld(), origin.posX, origin.posY, origin.posZ, dx, dy, dz, type);
	}
	
	public DDDParticle(Entity origin, double dx, double dy, double dz, DDDParticleType type, Random displacementSeed)
	{
		this(origin.getEntityWorld(), origin.posX + origin.width*displacementSeed.nextDouble() - origin.width/2, origin.posY + origin.getEyeHeight() + origin.height*displacementSeed.nextDouble() - origin.height/2, origin.posZ + origin.width*displacementSeed.nextDouble() - origin.width/2, dx, dy, dx, type);
	}
	
	@Override 
	public int getFXLayer()
	{
		return 1;
	}
	
	@SubscribeEvent
	public static void onTextureStitchEvent(TextureStitchEvent.Pre evt)
	{
		for(DDDParticleType type : DDDParticleType.values())
		{
			evt.getMap().registerSprite(type.getResourceLocation());
		}
	}
}
