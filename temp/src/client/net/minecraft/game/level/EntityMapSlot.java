package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

final class EntityMapSlot {
	private int xSlot;
	private int ySlot;
	private int zSlot;
	private EntityMap entityMap;

	private EntityMapSlot(EntityMap blockMap, byte b2) {
		this.entityMap = blockMap;
	}

	public final EntityMapSlot init(float f1, float f2, float f3) {
		this.xSlot = (int)(f1 / 8.0F);
		this.ySlot = (int)(f2 / 8.0F);
		this.zSlot = (int)(f3 / 8.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		if(this.xSlot >= this.entityMap.width) {
			this.xSlot = this.entityMap.width - 1;
		}

		if(this.ySlot >= this.entityMap.depth) {
			this.ySlot = this.entityMap.depth - 1;
		}

		if(this.zSlot >= this.entityMap.height) {
			this.zSlot = this.entityMap.height - 1;
		}

		return this;
	}

	public final void add(Entity entity) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.depth + this.ySlot) * this.entityMap.width + this.xSlot].add(entity);
		}

	}

	public final void remove(Entity entity) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.depth + this.ySlot) * this.entityMap.width + this.xSlot].remove(entity);
		}

	}

	EntityMapSlot(EntityMap entityMap1) {
		this(entityMap1, (byte)0);
	}

	static int a(EntityMapSlot entityMapSlot0) {
		return entityMapSlot0.xSlot;
	}

	static int b(EntityMapSlot entityMapSlot0) {
		return entityMapSlot0.ySlot;
	}

	static int c(EntityMapSlot entityMapSlot0) {
		return entityMapSlot0.zSlot;
	}
}