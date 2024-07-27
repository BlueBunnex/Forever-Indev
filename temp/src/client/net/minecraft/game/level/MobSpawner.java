package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityAnimal;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;

public class MobSpawner {
	private World worldObj;

	public MobSpawner(World world) {
		this.worldObj = world;
	}

	public final void performSpawning() {
		int i1 = this.worldObj.width * this.worldObj.length * this.worldObj.height * 20 / 64 / 64 / 64 / 2;
		if(this.worldObj.difficultySetting == 0) {
			i1 = 0 / 4;
		}

		if(this.worldObj.difficultySetting == 1) {
			i1 = i1 * 3 / 4;
		}

		if(this.worldObj.difficultySetting == 2) {
			i1 = (i1 << 2) / 4;
		}

		if(this.worldObj.difficultySetting == 3) {
			i1 = i1 * 6 / 4;
		}

		int i2 = this.worldObj.width * this.worldObj.length / 4000;
		int i3 = this.worldObj.entitiesInLevelList(EntityMob.class);

		int i4;
		EntityLiving entityLiving5;
		int i6;
		int i7;
		int i8;
		int i9;
		int i10;
		int i11;
		int i12;
		int i13;
		int i14;
		int i15;
		float f16;
		float f17;
		float f18;
		float f19;
		float f20;
		float f21;
		MobSpawner mobSpawner22;
		Object object23;
		for(i4 = 0; i4 < 4; ++i4) {
			if(i3 < i1) {
				entityLiving5 = this.worldObj.playerEntity;
				mobSpawner22 = this;
				i6 = 0;
				i7 = this.worldObj.random.nextInt(5);
				i8 = this.worldObj.random.nextInt(this.worldObj.width);
				i9 = (int)(Math.min(this.worldObj.random.nextFloat(), this.worldObj.random.nextFloat()) * (float)this.worldObj.height);
				i10 = this.worldObj.random.nextInt(this.worldObj.length);

				for(i11 = 0; i11 < 2; ++i11) {
					i12 = i8;
					i13 = i9;
					i14 = i10;

					for(i15 = 0; i15 < 3; ++i15) {
						i12 += mobSpawner22.worldObj.random.nextInt(6) - mobSpawner22.worldObj.random.nextInt(6);
						i13 += mobSpawner22.worldObj.random.nextInt(1) - mobSpawner22.worldObj.random.nextInt(1);
						i14 += mobSpawner22.worldObj.random.nextInt(6) - mobSpawner22.worldObj.random.nextInt(6);
						if(i12 >= 0 && i14 > 0 && i13 >= 0 && i13 < mobSpawner22.worldObj.height - 2 && i12 < mobSpawner22.worldObj.width && i14 < mobSpawner22.worldObj.length) {
							f16 = (float)i12 + 0.5F;
							f17 = (float)i13 + 0.5F;
							f18 = (float)i14 + 0.5F;
							if(entityLiving5 != null) {
								f19 = f16 - entityLiving5.posX;
								f20 = f17 - entityLiving5.posY;
								f21 = f18 - entityLiving5.posZ;
								if(f19 * f19 + f20 * f20 + f21 * f21 < 1024.0F) {
									continue;
								}
							} else {
								f19 = f16 - (float)mobSpawner22.worldObj.xSpawn;
								f20 = f17 - (float)mobSpawner22.worldObj.ySpawn;
								f21 = f18 - (float)mobSpawner22.worldObj.zSpawn;
								if(f19 * f19 + f20 * f20 + f21 * f21 < 1024.0F) {
									continue;
								}
							}

							object23 = null;
							if(i7 == 0) {
								object23 = new EntitySkeleton(mobSpawner22.worldObj);
							}

							if(i7 == 1) {
								object23 = new EntityCreeper(mobSpawner22.worldObj);
							}

							if(i7 == 2) {
								object23 = new EntitySpider(mobSpawner22.worldObj);
							}

							if(i7 == 3) {
								object23 = new EntityZombie(mobSpawner22.worldObj);
							}

							if(object23 instanceof EntityMob && mobSpawner22.worldObj.difficultySetting == 0) {
								object23 = null;
							}

							if(object23 != null && !mobSpawner22.worldObj.isBlockNormalCube(i12, i13, i14) && mobSpawner22.worldObj.isBlockNormalCube(i12, i13 - 1, i14) && ((EntityLiving)object23).getCanSpawnHere(f16, f17, f18)) {
								f21 = mobSpawner22.worldObj.random.nextFloat() * 360.0F;
								((EntityLiving)object23).setPositionAndRotation(f16, f17, f18, f21, 0.0F);
								++i6;
								mobSpawner22.worldObj.spawnEntityInWorld((Entity)object23);
							}
						}
					}
				}

				i3 += i6;
			}
		}

		i4 = this.worldObj.entitiesInLevelList(EntityAnimal.class);

		for(i1 = 0; i1 < 4; ++i1) {
			if(i4 < i2) {
				entityLiving5 = this.worldObj.playerEntity;
				mobSpawner22 = this;
				i6 = 0;
				i7 = this.worldObj.random.nextInt(2);
				i8 = this.worldObj.random.nextInt(this.worldObj.width);
				i9 = this.worldObj.random.nextInt(this.worldObj.height);
				i10 = this.worldObj.random.nextInt(this.worldObj.length);

				for(i11 = 0; i11 < 2; ++i11) {
					i12 = i8;
					i13 = i9;
					i14 = i10;

					for(i15 = 0; i15 < 3; ++i15) {
						i12 += mobSpawner22.worldObj.random.nextInt(6) - mobSpawner22.worldObj.random.nextInt(6);
						i13 += mobSpawner22.worldObj.random.nextInt(1) - mobSpawner22.worldObj.random.nextInt(1);
						i14 += mobSpawner22.worldObj.random.nextInt(6) - mobSpawner22.worldObj.random.nextInt(6);
						if(i12 >= 0 && i14 > 0 && i13 >= 0 && i13 < mobSpawner22.worldObj.height - 2 && i12 < mobSpawner22.worldObj.width && i14 < mobSpawner22.worldObj.length) {
							f16 = (float)i12 + 0.5F;
							f17 = (float)i13 + 0.5F;
							f18 = (float)i14 + 0.5F;
							if(entityLiving5 != null) {
								f19 = f16 - entityLiving5.posX;
								f20 = f17 - entityLiving5.posY;
								f21 = f18 - entityLiving5.posZ;
								if(f19 * f19 + f20 * f20 + f21 * f21 < 1024.0F) {
									continue;
								}
							} else {
								f19 = f16 - (float)mobSpawner22.worldObj.xSpawn;
								f20 = f17 - (float)mobSpawner22.worldObj.ySpawn;
								f21 = f18 - (float)mobSpawner22.worldObj.zSpawn;
								if(f19 * f19 + f20 * f20 + f21 * f21 < 1024.0F) {
									continue;
								}
							}

							object23 = null;
							if(i7 == 0) {
								object23 = new EntityPig(mobSpawner22.worldObj);
							}

							if(i7 == 1) {
								object23 = new EntitySheep(mobSpawner22.worldObj);
							}

							if(object23 != null && !mobSpawner22.worldObj.isBlockNormalCube(i12, i13, i14) && mobSpawner22.worldObj.isBlockNormalCube(i12, i13 - 1, i14) && ((EntityLiving)object23).getCanSpawnHere(f16, f17, f18)) {
								f21 = mobSpawner22.worldObj.random.nextFloat() * 360.0F;
								((EntityLiving)object23).setPositionAndRotation(f16, f17, f18, f21, 0.0F);
								++i6;
								mobSpawner22.worldObj.spawnEntityInWorld((Entity)object23);
							}
						}
					}
				}

				i4 += i6;
			}
		}

	}
}