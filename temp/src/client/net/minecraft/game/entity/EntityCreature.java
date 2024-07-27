package net.minecraft.game.entity;

import net.minecraft.game.level.World;
import net.minecraft.game.level.path.PathEntity;
import net.minecraft.game.physics.Vec3D;

import util.MathHelper;

public class EntityCreature extends EntityLiving {
	private PathEntity pathToEntity;
	protected Entity playerToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World world1) {
		super(world1);
	}

	protected void updatePlayerActionState() {
		this.hasAttacked = false;
		float f6;
		float f7;
		float f8;
		if(this.playerToAttack == null) {
			this.playerToAttack = this.findPlayerToAttack();
			if(this.playerToAttack != null) {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			}
		} else if(!this.playerToAttack.isEntityAlive()) {
			this.playerToAttack = null;
		} else {
			Entity entity5 = this.playerToAttack;
			f7 = this.playerToAttack.posX - super.posX;
			f8 = entity5.posY - super.posY;
			f6 = entity5.posZ - super.posZ;
			float f1 = MathHelper.sqrt_float(f7 * f7 + f8 * f8 + f6 * f6);
			if(this.worldObj.rayTraceBlocks(new Vec3D(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3D(this.playerToAttack.posX, this.playerToAttack.posY + this.playerToAttack.getEyeHeight(), this.playerToAttack.posZ)) == null) {
				this.attackEntity(this.playerToAttack, f1);
			}
		}

		if(this.hasAttacked) {
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.isJumping = false;
		} else {
			float f4;
			if(this.playerToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0)) {
				this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, this.playerToAttack, 16.0F);
			} else if(this.pathToEntity == null || this.rand.nextInt(100) == 0) {
				int i10 = -1;
				int i2 = -1;
				int i3 = -1;
				f4 = -99999.0F;

				for(int i14 = 0; i14 < 200; ++i14) {
					int i16 = (int)(this.posX + (float)this.rand.nextInt(21) - 10.0F);
					int i17 = (int)(this.posY + (float)this.rand.nextInt(9) - 4.0F);
					int i18 = (int)(this.posZ + (float)this.rand.nextInt(21) - 10.0F);
					float f9;
					if((f9 = this.getBlockPathWeight(i16, i17, i18)) > f4) {
						f4 = f9;
						i10 = i16;
						i2 = i17;
						i3 = i18;
					}
				}

				if(i10 > 0) {
					this.pathToEntity = this.worldObj.pathFinder.createEntityPathTo(this, i10, i2, i3, 16.0F);
				}
			}

			boolean z11 = this.handleWaterMovement();
			boolean z12 = this.handleLavaMovement();
			if(this.pathToEntity != null && this.rand.nextInt(100) != 0) {
				Vec3D vec3D13 = this.pathToEntity.getPosition(this);
				f4 = this.width * 2.0F;

				float f15;
				while(vec3D13 != null) {
					f8 = this.posZ;
					f7 = this.posY;
					f6 = this.posX;
					f6 -= vec3D13.xCoord;
					f7 -= vec3D13.yCoord;
					f15 = f8 - vec3D13.zCoord;
					if(f6 * f6 + f7 * f7 + f15 * f15 >= f4 * f4 || vec3D13.yCoord > this.posY) {
						break;
					}

					this.pathToEntity.incrementPathIndex();
					if(this.pathToEntity.isFinished()) {
						vec3D13 = null;
						this.pathToEntity = null;
					} else {
						vec3D13 = this.pathToEntity.getPosition(this);
					}
				}

				this.isJumping = false;
				if(vec3D13 != null) {
					f15 = vec3D13.xCoord - this.posX;
					f6 = vec3D13.zCoord - this.posZ;
					f7 = vec3D13.yCoord - this.posY;
					this.rotationYaw = (float)(Math.atan2((double)f6, (double)f15) * 180.0D / (double)(float)Math.PI) - 90.0F;
					this.moveForward = this.moveSpeed;
					if(f7 > 0.0F) {
						this.isJumping = true;
					}
				}

				if(this.rand.nextFloat() < 0.8F && (z11 || z12)) {
					this.isJumping = true;
				}

			} else {
				super.updatePlayerActionState();
				this.pathToEntity = null;
			}
		}
	}

	protected void attackEntity(Entity entity, float f2) {
	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	public boolean getCanSpawnHere(float f1, float f2, float f3) {
		return super.getCanSpawnHere(f1, f2, f3) && this.getBlockPathWeight((int)f1, (int)f2, (int)f3) >= 0.0F;
	}
}