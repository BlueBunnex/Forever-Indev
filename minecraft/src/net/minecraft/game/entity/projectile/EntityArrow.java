package net.minecraft.game.entity.projectile;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import util.MathHelper;

public class EntityArrow extends Entity {

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private boolean inGround = false;
    public int arrowShake = 0;
    public EntityLiving owner;
    private int ticksInGround;
    private int ticksInAir = 0;
    private int detonationLevel = 0; // Added for detonation effect
    private boolean fiery = false; // Added for fiery effect

    public EntityArrow(World world, EntityLiving owner) {
        super(world);
        this.owner = owner;
        this.setSize(0.5F, 0.5F);
        this.setPositionAndRotation(owner.posX, owner.posY, owner.posZ, owner.rotationYaw, owner.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.posY -= 0.1F;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    // New method to set detonation level
    public void setDetonationLevel(int level) {
        this.detonationLevel = level;
    }

    // New method to enable the fiery effect
    public void setFiery(boolean fiery) {
        this.fiery = fiery;
    }

    public final void setArrowHeading(float x, float y, float z, float velocity, float inaccuracy) {
        float length = MathHelper.sqrt_float(x * x + y * y + z * z);
        x /= length;
        y /= length;
        z /= length;
        x = (float) ((double) x + this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy);
        y = (float) ((double) y + this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy);
        z = (float) ((double) z + this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy);
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        velocity = MathHelper.sqrt_float(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2((double) x, (double) z) * 180.0D / (double) ((float) Math.PI));
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2((double) y, (double) velocity) * 180.0D / (double) ((float) Math.PI));
        this.ticksInGround = 0;
    }

    public final void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int blockId = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            if (blockId == this.inTile) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setEntityDead();
                }
                return;
            }

            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
            ++this.ticksInAir;
        }

        Vec3D start = new Vec3D(this.posX, this.posY, this.posZ);
        Vec3D end = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition hit = this.worldObj.rayTraceBlocks(start, end);
        start = new Vec3D(this.posX, this.posY, this.posZ);
        end = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (hit != null) {
            end = new Vec3D(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord);
        }

        Entity hitEntity = null;
        List<Entity> entities = this.worldObj.entityMap.getEntitiesWithinAABB(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0F, 1.0F, 1.0F));
        float closestDistance = 0.0F;

        for (Entity entity : entities) {
            if (entity.canBeCollidedWith() && (entity != this.owner || this.ticksInAir >= 5)) {
                AxisAlignedBB boundingBox = entity.boundingBox.expand(0.3F, 0.3F, 0.3F);
                MovingObjectPosition intercept = boundingBox.calculateIntercept(start, end);
                if (intercept != null) {
                    float distance = start.distance(intercept.hitVec);
                    if (distance < closestDistance || closestDistance == 0.0F) {
                        hitEntity = entity;
                        closestDistance = distance;
                    }
                }
            }
        }

        if (hitEntity != null) {
            hit = new MovingObjectPosition(hitEntity);
        }

        float f;
        if (hit != null) {
            if (hit.entityHit != null) {
                if (hit.entityHit.attackThisEntity(this, 4)) {
                    this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    
                    // Apply fiery effect
                    if (this.fiery) {
                        hit.entityHit.setFire(5); // Sets the entity on fire for 5 seconds
                    }

                    this.setEntityDead();
                } else {
                    this.motionX *= -0.1F;
                    this.motionY *= -0.1F;
                    this.motionZ *= -0.1F;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;
                    this.ticksInAir = 0;
                }
            } else {
                this.xTile = hit.blockX;
                this.yTile = hit.blockY;
                this.zTile = hit.blockZ;
                this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                this.motionX = hit.hitVec.xCoord - this.posX;
                this.motionY = hit.hitVec.yCoord - this.posY;
                this.motionZ = hit.hitVec.zCoord - this.posZ;
                f = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / f * 0.05F;
                this.posY -= this.motionY / f * 0.05F;
                this.posZ -= this.motionZ / f * 0.05F;
                this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.inGround = true;
                this.arrowShake = 7;

                // Handle detonation effect
                if (this.detonationLevel > 0) {
                    this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.detonationLevel * 2.0F);
                    this.setEntityDead();
                }
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        f = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2((double) this.motionX, (double) this.motionZ) * 180.0D / (double) ((float) Math.PI));

        for (this.rotationPitch = (float) (Math.atan2((double) this.motionY, (double) f) * 180.0D / (double) ((float) Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float drag = 0.99F;
        if (this.handleWaterMovement()) {
            for (int i = 0; i < 4; ++i) {
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25F, this.posY - this.motionY * 0.25F, this.posZ - this.motionZ * 0.25F, this.motionX, this.motionY, this.motionZ);
            }
            drag = 0.8F;
        }

        this.motionX *= drag;
        this.motionY *= drag;
        this.motionZ *= drag;
        this.motionY -= 0.03F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected final void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short) this.xTile);
        nbt.setShort("yTile", (short) this.yTile);
        nbt.setShort("zTile", (short) this.zTile);
        nbt.setByte("inTile", (byte) this.inTile);
        nbt.setByte("shake", (byte) this.arrowShake);
        nbt.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbt.setInteger("detonationLevel", this.detonationLevel); // Save detonation level
        nbt.setBoolean("fiery", this.fiery); // Save fiery effect
    }

    protected final void readEntityFromNBT(NBTTagCompound nbt) {
        this.xTile = nbt.getShort("xTile");
        this.yTile = nbt.getShort("yTile");
        this.zTile = nbt.getShort("zTile");
        this.inTile = nbt.getByte("inTile") & 255;
        this.arrowShake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getByte("inGround") == 1;
        this.detonationLevel = nbt.getInteger("detonationLevel"); // Load detonation level
        this.fiery = nbt.getBoolean("fiery"); // Load fiery effect
    }

    protected final String getEntityString() {
        return "Arrow";
    }

    public final void onCollideWithPlayer(EntityPlayer player) {
        if (this.inGround && this.owner == player && this.arrowShake <= 0 && player.inventory.storePartialItemStack(new ItemStack(Item.arrow.shiftedIndex, 1))) {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.onItemPickup(this);
            this.setEntityDead();
        }
    }

    public final float getShadowSize() {
        return 0.0F;
    }

    public void setDetonatesOnImpact(boolean b) {
        // Optional additional feature: setting an instant detonation on impact.
    }
}
