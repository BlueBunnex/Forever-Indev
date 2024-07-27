package net.minecraft.client.render.entity;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

public final class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		EntityTNTPrimed entityTNTPrimed10001 = (EntityTNTPrimed)entity;
		f5 = f4;
		f4 = f3;
		f3 = f2;
		EntityTNTPrimed entityTNTPrimed7 = entityTNTPrimed10001;
		GL11.glPushMatrix();
		GL11.glTranslatef(f3, f4, f5);
		if((float)entityTNTPrimed7.fuse - f6 + 1.0F < 10.0F) {
			if((f3 = 1.0F - ((float)entityTNTPrimed7.fuse - f6 + 1.0F) / 10.0F) < 0.0F) {
				f3 = 0.0F;
			}

			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			f3 = (f3 *= f3) * f3;
			GL11.glScalef(f3 = 1.0F + f3 * 0.3F, f3, f3);
		}

		f3 = (1.0F - ((float)entityTNTPrimed7.fuse - f6 + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt);
		if(entityTNTPrimed7.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
			this.blockRenderer.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}
}