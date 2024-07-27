package net.minecraft.game.level.path;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class Pathfinder {
	private World worldMap;
	private Path path = new Path();
	private Map pointMap = new HashMap();
	private PathPoint[] pathOptions = new PathPoint[32];

	public Pathfinder(World world1) {
		this.worldMap = world1;
	}

	public final PathEntity createEntityPathTo(Entity entity, Entity entity2, float f3) {
		return this.addToPath(entity, entity2.posX, entity2.boundingBox.minY, entity2.posZ, 16.0F);
	}

	public final PathEntity createEntityPathTo(Entity entity, int i2, int i3, int i4, float f5) {
		return this.addToPath(entity, (float)i2 + 0.5F, (float)i3 + 0.5F, (float)i4 + 0.5F, 16.0F);
	}

	private PathEntity addToPath(Entity entity, float f2, float f3, float f4, float f5) {
		this.path.clearPath();
		this.pointMap.clear();
		PathPoint pathPoint6 = this.openPoint((int)entity.boundingBox.minX, (int)entity.boundingBox.minY, (int)entity.boundingBox.minZ);
		PathPoint pathPoint21 = this.openPoint((int)(f2 - entity.width / 2.0F), (int)f3, (int)(f4 - entity.width / 2.0F));
		PathPoint pathPoint23 = new PathPoint((int)(entity.width + 1.0F), (int)(entity.height + 1.0F), (int)(entity.width + 1.0F));
		float f26 = f5;
		PathPoint pathPoint25 = pathPoint23;
		PathPoint pathPoint24 = pathPoint21;
		Entity entity22 = entity;
		Pathfinder pathfinder20 = this;
		pathPoint6.totalPathDistance = 0.0F;
		pathPoint6.distanceToNext = pathPoint6.distanceTo(pathPoint21);
		pathPoint6.distanceToTarget = pathPoint6.distanceToNext;
		this.path.clearPath();
		this.path.addPoint(pathPoint6);
		PathPoint pathPoint7 = pathPoint6;

		PathEntity pathEntity10000;
		while(true) {
			if(pathfinder20.path.isPathEmpty()) {
				pathEntity10000 = pathPoint7 == pathPoint6 ? null : createEntityPath(pathPoint7);
				break;
			}

			PathPoint pathPoint8;
			if((pathPoint8 = pathfinder20.path.dequeue()).hash == pathPoint24.hash) {
				pathEntity10000 = createEntityPath(pathPoint24);
				break;
			}

			if(pathPoint8.distanceTo(pathPoint24) < pathPoint7.distanceTo(pathPoint24)) {
				pathPoint7 = pathPoint8;
			}

			pathPoint8.isFirst = true;
			int i15 = 0;
			byte b16 = 0;
			if(pathfinder20.getVerticalOffset(pathPoint8.xCoord, pathPoint8.yCoord + 1, pathPoint8.zCoord, pathPoint25) > 0) {
				b16 = 1;
			}

			PathPoint pathPoint17 = pathfinder20.getSafePoint(entity22, pathPoint8.xCoord, pathPoint8.yCoord, pathPoint8.zCoord + 1, pathPoint25, b16);
			PathPoint pathPoint18 = pathfinder20.getSafePoint(entity22, pathPoint8.xCoord - 1, pathPoint8.yCoord, pathPoint8.zCoord, pathPoint25, b16);
			PathPoint pathPoint19 = pathfinder20.getSafePoint(entity22, pathPoint8.xCoord + 1, pathPoint8.yCoord, pathPoint8.zCoord, pathPoint25, b16);
			PathPoint pathPoint10 = pathfinder20.getSafePoint(entity22, pathPoint8.xCoord, pathPoint8.yCoord, pathPoint8.zCoord - 1, pathPoint25, b16);
			if(pathPoint17 != null && !pathPoint17.isFirst && pathPoint17.distanceTo(pathPoint24) < f26) {
				++i15;
				pathfinder20.pathOptions[0] = pathPoint17;
			}

			if(pathPoint18 != null && !pathPoint18.isFirst && pathPoint18.distanceTo(pathPoint24) < f26) {
				pathfinder20.pathOptions[i15++] = pathPoint18;
			}

			if(pathPoint19 != null && !pathPoint19.isFirst && pathPoint19.distanceTo(pathPoint24) < f26) {
				pathfinder20.pathOptions[i15++] = pathPoint19;
			}

			if(pathPoint10 != null && !pathPoint10.isFirst && pathPoint10.distanceTo(pathPoint24) < f26) {
				pathfinder20.pathOptions[i15++] = pathPoint10;
			}

			int i9 = i15;

			for(int i27 = 0; i27 < i9; ++i27) {
				PathPoint pathPoint11 = pathfinder20.pathOptions[i27];
				float f12 = pathPoint8.totalPathDistance + pathPoint8.distanceTo(pathPoint11);
				if(!pathPoint11.isAssigned() || f12 < pathPoint11.totalPathDistance) {
					pathPoint11.previous = pathPoint8;
					pathPoint11.totalPathDistance = f12;
					pathPoint11.distanceToNext = pathPoint11.distanceTo(pathPoint24);
					if(pathPoint11.isAssigned()) {
						pathfinder20.path.changeDistance(pathPoint11, pathPoint11.totalPathDistance + pathPoint11.distanceToNext);
					} else {
						pathPoint11.distanceToTarget = pathPoint11.totalPathDistance + pathPoint11.distanceToNext;
						pathfinder20.path.addPoint(pathPoint11);
					}
				}
			}
		}

		return pathEntity10000;
	}

	private PathPoint getSafePoint(Entity entity, int i2, int i3, int i4, PathPoint pathPoint, int i6) {
		PathPoint pathPoint8 = null;
		if(this.getVerticalOffset(i2, i3, i4, pathPoint) > 0) {
			pathPoint8 = this.openPoint(i2, i3, i4);
		}

		if(pathPoint8 == null && this.getVerticalOffset(i2, i3 + i6, i4, pathPoint) > 0) {
			pathPoint8 = this.openPoint(i2, i3 + i6, i4);
		}

		if(pathPoint8 != null) {
			i6 = 0;

			while(true) {
				int i7;
				if(i3 <= 0 || (i7 = this.getVerticalOffset(i2, i3 - 1, i4, pathPoint)) <= 0) {
					Material material9;
					if((material9 = this.worldMap.getBlockMaterial(i2, i3 - 1, i4)) == Material.water || material9 == Material.lava) {
						return null;
					}
					break;
				}

				if(i7 < 0) {
					return null;
				}

				++i6;
				if(i6 >= 4) {
					return null;
				}

				--i3;
				pathPoint8 = this.openPoint(i2, i3, i4);
			}
		}

		return pathPoint8;
	}

	private final PathPoint openPoint(int i1, int i2, int i3) {
		int i4 = i1 | i2 << 10 | i3 << 20;
		PathPoint pathPoint5;
		if((pathPoint5 = (PathPoint)this.pointMap.get(i4)) == null) {
			pathPoint5 = new PathPoint(i1, i2, i3);
			this.pointMap.put(i4, pathPoint5);
		}

		return pathPoint5;
	}

	private int getVerticalOffset(int i1, int i2, int i3, PathPoint pathPoint4) {
		for(int i5 = i1; i5 < i1 + pathPoint4.xCoord; ++i5) {
			if(i5 < 0 || i5 >= this.worldMap.width) {
				return 0;
			}

			for(int i6 = i2; i6 < i2 + pathPoint4.yCoord; ++i6) {
				if(i6 < 0 || i6 >= this.worldMap.height) {
					return 0;
				}

				int i7 = i3;

				while(i7 < i3 + pathPoint4.zCoord) {
					if(i7 >= 0 && i7 < this.worldMap.length) {
						Material material8;
						if((material8 = this.worldMap.getBlockMaterial(i1, i2, i3)).getIsSolid()) {
							return 0;
						}

						if(material8 != Material.water && material8 != Material.lava) {
							++i7;
							continue;
						}

						return -1;
					}

					return 0;
				}
			}
		}

		return 1;
	}

	private static PathEntity createEntityPath(PathPoint pathPoint) {
		int i1 = 1;

		PathPoint pathPoint2;
		for(pathPoint2 = pathPoint; pathPoint2.previous != null; pathPoint2 = pathPoint2.previous) {
			++i1;
		}

		PathPoint[] pathPoint3 = new PathPoint[i1];
		pathPoint2 = pathPoint;
		--i1;

		for(pathPoint3[i1] = pathPoint; pathPoint2.previous != null; pathPoint3[i1] = pathPoint2) {
			pathPoint2 = pathPoint2.previous;
			--i1;
		}

		return new PathEntity(pathPoint3);
	}
}