package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class EffectRenderer {
	private World worldObj;
	private List[] fxLayers = new List[3];
	private RenderEngine renderEngine;
	private Random rand = new Random();

	public EffectRenderer(World world, RenderEngine renderEngine) {
		if(world != null) {
			this.worldObj = world;
		}

		this.renderEngine = renderEngine;

		for(int i3 = 0; i3 < 3; ++i3) {
			this.fxLayers[i3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX entityFX) {
		int i2 = entityFX.getFXLayer();
		this.fxLayers[i2].add(entityFX);
	}

	public final void updateEffects() {
		for(int i1 = 0; i1 < 3; ++i1) {
			for(int i2 = 0; i2 < this.fxLayers[i1].size(); ++i2) {
				EntityFX entityFX3;
				(entityFX3 = (EntityFX)this.fxLayers[i1].get(i2)).onEntityUpdate();
				if(entityFX3.isDead) {
					this.fxLayers[i1].remove(i2--);
				}
			}
		}

	}

	public final void renderParticles(Entity particle, float f2) {
		float f3 = MathHelper.cos(particle.rotationYaw * (float)Math.PI / 180.0F);
		float f4;
		float f5 = -(f4 = MathHelper.sin(particle.rotationYaw * (float)Math.PI / 180.0F)) * MathHelper.sin(particle.rotationPitch * (float)Math.PI / 180.0F);
		float f6 = f3 * MathHelper.sin(particle.rotationPitch * (float)Math.PI / 180.0F);
		float f11 = MathHelper.cos(particle.rotationPitch * (float)Math.PI / 180.0F);

		for(int i7 = 0; i7 < 2; ++i7) {
			if(this.fxLayers[i7].size() != 0) {
				int i8 = 0;
				if(i7 == 0) {
					i8 = this.renderEngine.getTexture("/particles.png");
				}

				if(i7 == 1) {
					i8 = this.renderEngine.getTexture("/terrain.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
				Tessellator tessellator12 = Tessellator.instance;
				Tessellator.instance.startDrawingQuads();

				for(int i9 = 0; i9 < this.fxLayers[i7].size(); ++i9) {
					((EntityFX)this.fxLayers[i7].get(i9)).renderParticle(tessellator12, f2, f3, f11, f4, f5, f6);
				}

				tessellator12.draw();
			}
		}

	}

	public final void renderLitParticles(float f1) {
		if(this.fxLayers[2].size() != 0) {
			Tessellator tessellator2 = Tessellator.instance;

			for(int i3 = 0; i3 < this.fxLayers[2].size(); ++i3) {
				((EntityFX)this.fxLayers[2].get(i3)).renderParticle(tessellator2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public final void clearEffects(World world) {
		this.worldObj = world;

		for(int i2 = 0; i2 < 3; ++i2) {
			this.fxLayers[i2].clear();
		}

	}

	public final void addBlockDestroyEffects(int i1, int i2, int i3) {
		int i4;
		if((i4 = this.worldObj.getBlockId(i1, i2, i3)) != 0) {
			Block block11 = Block.blocksList[i4];

			for(int i5 = 0; i5 < 4; ++i5) {
				for(int i6 = 0; i6 < 4; ++i6) {
					for(int i7 = 0; i7 < 4; ++i7) {
						float f8 = (float)i1 + ((float)i5 + 0.5F) / 4.0F;
						float f9 = (float)i2 + ((float)i6 + 0.5F) / 4.0F;
						float f10 = (float)i3 + ((float)i7 + 0.5F) / 4.0F;
						this.addEffect(new EntityDiggingFX(this.worldObj, f8, f9, f10, f8 - (float)i1 - 0.5F, f9 - (float)i2 - 0.5F, f10 - (float)i3 - 0.5F, block11));
					}
				}
			}

		}
	}

	public final void addBlockHitEffects(int i1, int i2, int i3, int i4) {
		int i5;
		if((i5 = this.worldObj.getBlockId(i1, i2, i3)) != 0) {
			Block block9 = Block.blocksList[i5];
			float f6 = (float)i1 + this.rand.nextFloat() * (block9.maxX - block9.minX - 0.2F) + 0.1F + block9.minX;
			float f7 = (float)i2 + this.rand.nextFloat() * (block9.maxY - block9.minY - 0.2F) + 0.1F + block9.minY;
			float f8 = (float)i3 + this.rand.nextFloat() * (block9.maxZ - block9.minZ - 0.2F) + 0.1F + block9.minZ;
			if(i4 == 0) {
				f7 = (float)i2 + block9.minY - 0.1F;
			}

			if(i4 == 1) {
				f7 = (float)i2 + block9.maxY + 0.1F;
			}

			if(i4 == 2) {
				f8 = (float)i3 + block9.minZ - 0.1F;
			}

			if(i4 == 3) {
				f8 = (float)i3 + block9.maxZ + 0.1F;
			}

			if(i4 == 4) {
				f6 = (float)i1 + block9.minX - 0.1F;
			}

			if(i4 == 5) {
				f6 = (float)i1 + block9.maxX + 0.1F;
			}

			this.addEffect((new EntityDiggingFX(this.worldObj, f6, f7, f8, 0.0F, 0.0F, 0.0F, block9)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public final String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}