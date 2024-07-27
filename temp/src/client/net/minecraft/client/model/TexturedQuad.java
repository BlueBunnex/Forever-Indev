package net.minecraft.client.model;

public final class TexturedQuad {
	public PositionTextureVertex[] vertexPositions;

	private TexturedQuad(PositionTextureVertex[] positionTextureVertex1) {
		this.vertexPositions = positionTextureVertex1;
	}

	public TexturedQuad(PositionTextureVertex[] positionTextureVertex1, int i2, int i3, int i4, int i5) {
		this(positionTextureVertex1);
		positionTextureVertex1[0] = positionTextureVertex1[0].setTexturePosition((float)i4 / 64.0F - 0.0015625F, (float)i3 / 32.0F + 0.003125F);
		positionTextureVertex1[1] = positionTextureVertex1[1].setTexturePosition((float)i2 / 64.0F + 0.0015625F, (float)i3 / 32.0F + 0.003125F);
		positionTextureVertex1[2] = positionTextureVertex1[2].setTexturePosition((float)i2 / 64.0F + 0.0015625F, (float)i5 / 32.0F - 0.003125F);
		positionTextureVertex1[3] = positionTextureVertex1[3].setTexturePosition((float)i4 / 64.0F - 0.0015625F, (float)i5 / 32.0F - 0.003125F);
	}
}