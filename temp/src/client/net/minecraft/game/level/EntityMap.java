package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class EntityMap {
	public int width;
	public int depth;
	public int height;
	private EntityMapSlot slot = new EntityMapSlot(this);
	private EntityMapSlot slot2 = new EntityMapSlot(this);
	public List[] entityGrid;
	public List entities = new ArrayList();
	private List entitiesExcludingEntity = new ArrayList();

	public EntityMap(int width, int depth, int height) {
		this.width = width / 8;
		this.depth = depth / 8;
		this.height = height / 8;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.depth * this.height];

		for(width = 0; width < this.width; ++width) {
			for(depth = 0; depth < this.depth; ++depth) {
				for(height = 0; height < this.height; ++height) {
					this.entityGrid[(height * this.depth + depth) * this.width + width] = new ArrayList();
				}
			}
		}

	}

	public final void insert(Entity entity) {
		this.entities.add(entity);
		this.slot.init(entity.posX, entity.posY, entity.posZ).add(entity);
		entity.lastTickPosX = entity.posX;
		entity.lastTickPosY = entity.posY;
		entity.lastTickPosZ = entity.posZ;
	}

	public final void remove(Entity entity) {
		this.slot.init(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).remove(entity);
		this.slot.init(entity.posX, entity.posY, entity.posZ).remove(entity);
		this.entities.remove(entity);
	}

	public final List getEntities(Entity entity, float f2, float f3, float f4, float f5, float f6, float f7) {
		this.entitiesExcludingEntity.clear();
		return this.getEntities(entity, f2, f3, f4, f5, f6, f7, this.entitiesExcludingEntity);
	}

	private List getEntities(Entity entity, float f2, float f3, float f4, float f5, float f6, float f7, List list8) {
		EntityMapSlot entityMapSlot9 = this.slot.init(f2, f3, f4);
		EntityMapSlot entityMapSlot10 = this.slot2.init(f5, f6, f7);

		for(int i11 = EntityMapSlot.a(entityMapSlot9) - 1; i11 <= EntityMapSlot.a(entityMapSlot10) + 1; ++i11) {
			for(int i12 = EntityMapSlot.b(entityMapSlot9) - 1; i12 <= EntityMapSlot.b(entityMapSlot10) + 1; ++i12) {
				for(int i13 = EntityMapSlot.c(entityMapSlot9) - 1; i13 <= EntityMapSlot.c(entityMapSlot10) + 1; ++i13) {
					if(i11 >= 0 && i12 >= 0 && i13 >= 0 && i11 < this.width && i12 < this.depth && i13 < this.height) {
						List list14 = this.entityGrid[(i13 * this.depth + i12) * this.width + i11];

						for(int i15 = 0; i15 < list14.size(); ++i15) {
							Entity entity16;
							if((entity16 = (Entity)list14.get(i15)) != entity) {
								AxisAlignedBB axisAlignedBB17 = entity16.boundingBox;
								if(f5 > axisAlignedBB17.minX && f2 < axisAlignedBB17.maxX ? (f6 > axisAlignedBB17.minY && f3 < axisAlignedBB17.maxY ? f7 > axisAlignedBB17.minZ && f4 < axisAlignedBB17.maxZ : false) : false) {
									list8.add(entity16);
								}
							}
						}
					}
				}
			}
		}

		return list8;
	}

	public final List getEntitiesWithinAABB(Entity entity, AxisAlignedBB aabb) {
		this.entitiesExcludingEntity.clear();
		return aabb == null ? this.entitiesExcludingEntity : this.getEntities(entity, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, this.entitiesExcludingEntity);
	}

	public final void updateEntities() {
		for(int i1 = 0; i1 < this.entities.size(); ++i1) {
			Entity entity2;
			(entity2 = (Entity)this.entities.get(i1)).lastTickPosX = entity2.posX;
			entity2.lastTickPosY = entity2.posY;
			entity2.lastTickPosZ = entity2.posZ;
			entity2.onEntityUpdate();
			if(entity2.isDead) {
				this.entities.remove(i1--);
				this.slot.init(entity2.lastTickPosX, entity2.lastTickPosY, entity2.lastTickPosZ).remove(entity2);
			} else {
				int i3 = (int)(entity2.lastTickPosX / 8.0F);
				int i4 = (int)(entity2.lastTickPosY / 8.0F);
				int i5 = (int)(entity2.lastTickPosZ / 8.0F);
				int i6 = (int)(entity2.posX / 8.0F);
				int i7 = (int)(entity2.posY / 8.0F);
				int i8 = (int)(entity2.posZ / 8.0F);
				if(i3 != i6 || i4 != i7 || i5 != i8) {
					EntityMapSlot entityMapSlot11 = this.slot.init(entity2.lastTickPosX, entity2.lastTickPosY, entity2.lastTickPosZ);
					EntityMapSlot entityMapSlot9 = this.slot2.init(entity2.posX, entity2.posY, entity2.posZ);
					if(!entityMapSlot11.equals(entityMapSlot9)) {
						entityMapSlot11.remove(entity2);
						entityMapSlot9.add(entity2);
					}
				}
			}
		}

	}
}