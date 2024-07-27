package net.minecraft.client.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public final class Tessellator {
	private static boolean convertQuadsToTriangles = false;
	private static boolean tryVBO = false;
	private ByteBuffer byteBuffer = BufferUtils.createByteBuffer(8388608);
	private int[] rawBuffer = new int[2097152];
	private int vertexCount = 0;
	private float textureU;
	private float textureV;
	private int color;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int rawBufferIndex = 0;
	private int addedVertices = 0;
	private boolean isColorDisabled = false;
	private int drawMode;
	public static Tessellator instance = new Tessellator();
	private boolean isDrawing = false;
	private boolean useVBO = false;
	private IntBuffer vertexBuffers;
	private int vboIndex = 0;
	private int vboCount = 10;

	private Tessellator() {
		this.useVBO = false;
		if(this.useVBO) {
			this.vertexBuffers = BufferUtils.createIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

	}

	public final void draw() {
		if(!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.isDrawing = false;
			if(this.vertexCount > 0) {
				IntBuffer intBuffer1 = this.byteBuffer.asIntBuffer();
				FloatBuffer floatBuffer2 = this.byteBuffer.asFloatBuffer();
				intBuffer1.clear();
				intBuffer1.put(this.rawBuffer, 0, this.rawBufferIndex);
				this.byteBuffer.position(0);
				this.byteBuffer.limit(this.rawBufferIndex << 2);
				if(this.useVBO) {
					this.vboIndex = (this.vboIndex + 1) % this.vboCount;
					ARBVertexBufferObject.glBindBufferARB(GL15.GL_ARRAY_BUFFER, this.vertexBuffers.get(this.vboIndex));
					ARBVertexBufferObject.glBufferDataARB(GL15.GL_ARRAY_BUFFER, this.byteBuffer, GL15.GL_STREAM_DRAW);
				}

				if(this.hasTexture) {
					if(this.useVBO) {
						GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
					} else {
						floatBuffer2.position(3);
						GL11.glTexCoordPointer(2, 32, floatBuffer2);
					}

					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if(this.hasColor) {
					if(this.useVBO) {
						GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
					} else {
						this.byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				}

				if(this.useVBO) {
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
				} else {
					floatBuffer2.position(0);
					GL11.glVertexPointer(3, 32, floatBuffer2);
				}

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glDrawArrays(this.drawMode, GL11.GL_POINTS, this.vertexCount);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				if(this.hasTexture) {
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if(this.hasColor) {
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				}
			}

			this.reset();
		}
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.rawBufferIndex = 0;
		this.addedVertices = 0;
	}

	public final void startDrawingQuads() {
		this.startDrawing(7);
	}

	public final void startDrawing(int i1) {
		if(this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = i1;
			this.hasColor = false;
			this.hasTexture = false;
			this.isColorDisabled = false;
		}
	}

	public final void setColorOpaque_F(float f1, float f2, float f3) {
		this.setColorOpaque((int)(f1 * 255.0F), (int)(f2 * 255.0F), (int)(f3 * 255.0F));
	}

	public final void setColorRGBA_F(float f1, float f2, float f3, float f4) {
		this.setColorRGBA((int)(f1 * 255.0F), (int)(f2 * 255.0F), (int)(f3 * 255.0F), (int)(f4 * 255.0F));
	}

	private void setColorOpaque(int i1, int i2, int i3) {
		this.setColorRGBA(i1, i2, i3, 255);
	}

	private void setColorRGBA(int i1, int i2, int i3, int i4) {
		if(!this.isColorDisabled) {
			if(i1 > 255) {
				i1 = 255;
			}

			if(i2 > 255) {
				i2 = 255;
			}

			if(i3 > 255) {
				i3 = 255;
			}

			if(i4 > 255) {
				i4 = 255;
			}

			if(i1 < 0) {
				i1 = 0;
			}

			if(i2 < 0) {
				i2 = 0;
			}

			if(i3 < 0) {
				i3 = 0;
			}

			if(i4 < 0) {
				i4 = 0;
			}

			this.hasColor = true;
			this.color = i4 << 24 | i3 << 16 | i2 << 8 | i1;
		}
	}

	public final void addVertexWithUV(float f1, float f2, float f3, float f4, float f5) {
		this.hasTexture = true;
		this.textureU = f4;
		this.textureV = f5;
		this.addVertex(f1, f2, f3);
	}

	public final void addVertex(float f1, float f2, float f3) {
		++this.addedVertices;
		if(this.hasTexture) {
			this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits(this.textureU);
			this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits(this.textureV);
		}

		if(this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		this.rawBuffer[this.rawBufferIndex] = Float.floatToRawIntBits(f1);
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits(f2);
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits(f3);
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.rawBufferIndex >= 2097120) {
			this.draw();
		}

	}

	public final void setColorOpaque_I(int i1) {
		int i2 = i1 >> 16 & 255;
		int i3 = i1 >> 8 & 255;
		i1 &= 255;
		this.setColorOpaque(i2, i3, i1);
	}

	public final void disableColor() {
		this.isColorDisabled = true;
	}

	public static void setNormal(float f0, float f1, float f2) {
		GL11.glNormal3f(f0, f1, f2);
	}
}