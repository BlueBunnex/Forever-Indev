package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public class RenderLiving extends Render {
	protected ModelBase mainModel;
	private ModelBase renderPassModel;

	public RenderLiving(ModelBase modelBase, float f2) {
		this.mainModel = modelBase;
		this.shadowSize = f2;
	}

	public final void setRenderPassModel(ModelBase modelBase) {
		this.renderPassModel = modelBase;
	}

	public void a(EntityLiving entityLiving1, float f2, float f3, float f4, float f5, float f6) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);

		try {
			f5 = entityLiving1.prevRenderYawOffset + (entityLiving1.renderYawOffset - entityLiving1.prevRenderYawOffset) * f6;
			float f7 = entityLiving1.prevRotationYaw + (entityLiving1.rotationYaw - entityLiving1.prevRotationYaw) * f6;
			float f8 = entityLiving1.prevRotationPitch + (entityLiving1.rotationPitch - entityLiving1.prevRotationPitch) * f6;
			GL11.glTranslatef(f2, f3, f4);
			f2 = (float)entityLiving1.ticksExisted + f6;
			GL11.glRotatef(180.0F - f5, 0.0F, 1.0F, 0.0F);
			if(entityLiving1.deathTime > 0) {
				if((f3 = MathHelper.sqrt_float(((float)entityLiving1.deathTime + f6 - 1.0F) / 20.0F * 1.6F)) > 1.0F) {
					f3 = 1.0F;
				}

				GL11.glRotatef(f3 * this.getDeathMaxRotation(entityLiving1), 0.0F, 0.0F, 1.0F);
			}

			GL11.glScalef(-0.0625F, -0.0625F, 0.0625F);
			this.preRenderCallback(entityLiving1, f6);
			GL11.glTranslatef(0.0F, -24.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			f3 = entityLiving1.prevLimbYaw + (entityLiving1.limbYaw - entityLiving1.prevLimbYaw) * f6;
			f4 = entityLiving1.limbSwing - entityLiving1.limbYaw * (1.0F - f6);
			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			this.loadDownloadableImageTexture(entityLiving1.skinUrl, entityLiving1.getTexture());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);

			for(int i9 = 0; i9 < 4; ++i9) {
				if(this.shouldRenderPass(entityLiving1, i9)) {
					this.renderPassModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			float f15 = entityLiving1.getEntityBrightness(f6);
			int i14;
			if((i14 = this.getColorMultiplier(entityLiving1, f15, f6)) >>> 24 > 0 || entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
					GL11.glColor4f(f15, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);

					for(int i10 = 0; i10 < 4; ++i10) {
						if(this.shouldRenderPass(entityLiving1, i10)) {
							GL11.glColor4f(f15, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);
						}
					}
				}

				if(i14 >>> 24 > 0) {
					float f16 = (float)(i14 >> 16 & 255) / 255.0F;
					f15 = (float)(i14 >> 8 & 255) / 255.0F;
					float f11 = (float)(i14 & 255) / 255.0F;
					f6 = (float)(i14 >>> 24) / 255.0F;
					GL11.glColor4f(f16, f15, f11, f6);
					this.mainModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);

					for(int i12 = 0; i12 < 4; ++i12) {
						if(this.shouldRenderPass(entityLiving1, i12)) {
							GL11.glColor4f(f16, f15, f11, f6);
							this.renderPassModel.render(f4, f3, f2, f7 - f5, f8, 1.0F);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL11.GL_NORMALIZE);
		} catch (Exception exception13) {
			exception13.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving, int i2) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving entityLiving) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving entityLiving, float f2, float f3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving entityLiving, float f2) {
	}

	public void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		this.a((EntityLiving)entity, f2, f3, f4, f5, f6);
	}
}