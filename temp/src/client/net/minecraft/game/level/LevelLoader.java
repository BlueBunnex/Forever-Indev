package net.minecraft.game.level;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;
import com.mojang.nbt.NBTTagShort;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiantZombie;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.block.tileentity.TileEntityChest;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;

import util.IProgressUpdate;

public abstract class LevelLoader {
	private IProgressUpdate guiLoading;

	public LevelLoader(IProgressUpdate guiLoading) {
		this.guiLoading = guiLoading;
	}

	public final World load(InputStream writeLevelTags) throws IOException {
		if(this.guiLoading != null) {
			this.guiLoading.displayProgressMessage("Loading level");
		}

		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Reading..");
		}

		NBTTagCompound nBTTagCompound13;
		NBTTagCompound nBTTagCompound2 = (nBTTagCompound13 = LoadingScreenRenderer.writeLevelTags(writeLevelTags)).getCompoundTag("About");
		NBTTagCompound nBTTagCompound3 = nBTTagCompound13.getCompoundTag("Map");
		NBTTagCompound nBTTagCompound4 = nBTTagCompound13.getCompoundTag("Environment");
		NBTTagList nBTTagList5 = nBTTagCompound13.getTagList("Entities");
		short s6 = nBTTagCompound3.getShort("Width");
		short s7 = nBTTagCompound3.getShort("Length");
		short s8 = nBTTagCompound3.getShort("Height");
		World world9 = new World();
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing level..");
		}

		NBTTagList nBTTagList10 = nBTTagCompound3.getTagList("Spawn");
		world9.xSpawn = ((NBTTagShort)nBTTagList10.tagAt(0)).shortValue;
		world9.ySpawn = ((NBTTagShort)nBTTagList10.tagAt(1)).shortValue;
		world9.zSpawn = ((NBTTagShort)nBTTagList10.tagAt(2)).shortValue;
		world9.authorName = nBTTagCompound2.getString("Author");
		world9.name = nBTTagCompound2.getString("Name");
		world9.createTime = nBTTagCompound2.getLong("CreatedOn");
		world9.cloudColor = nBTTagCompound4.getInteger("CloudColor");
		world9.skyColor = nBTTagCompound4.getInteger("SkyColor");
		world9.fogColor = nBTTagCompound4.getInteger("FogColor");
		world9.skyBrightness = nBTTagCompound4.getByte("SkyBrightness");
		if(world9.skyBrightness < 0) {
			world9.skyBrightness = 0;
		}

		if(world9.skyBrightness > 16) {
			world9.skyBrightness = world9.skyBrightness * 15 / 100;
		}

		world9.cloudHeight = nBTTagCompound4.getShort("CloudHeight");
		world9.groundLevel = nBTTagCompound4.getShort("SurroundingGroundHeight");
		world9.waterLevel = nBTTagCompound4.getShort("SurroundingWaterHeight");
		world9.defaultFluid = nBTTagCompound4.getByte("SurroundingWaterType");
		world9.worldTime = nBTTagCompound4.getShort("TimeOfDay");
		world9.skylightSubtracted = world9.getSkyBrightness();
		world9.generate(s6, s8, s7, nBTTagCompound3.getByteArray("Blocks"), nBTTagCompound3.getByteArray("Data"));
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing entities..");
		}

		for(int i16 = 0; i16 < nBTTagList5.tagCount(); ++i16) {
			try {
				String string19 = (nBTTagCompound3 = (NBTTagCompound)nBTTagList5.tagAt(i16)).getString("id");
				Entity entity22;
				if((entity22 = this.loadEntity(world9, string19)) != null) {
					entity22.readFromNBT(nBTTagCompound3);
					world9.spawnEntityInWorld(entity22);
				} else {
					System.out.println("Skipping unknown entity id \"" + string19 + "\"");
				}
			} catch (Exception exception12) {
				System.out.println("Error reading entity");
				exception12.printStackTrace();
			}
		}

		NBTTagList nBTTagList17 = nBTTagCompound13.getTagList("TileEntities");

		for(int i18 = 0; i18 < nBTTagList17.tagCount(); ++i18) {
			try {
				int i23 = (nBTTagCompound4 = (NBTTagCompound)nBTTagList17.tagAt(i18)).getInteger("Pos");
				String string14;
				String string20;
				Object object21;
				if((object21 = (string20 = string14 = nBTTagCompound4.getString("id")).equals("Chest") ? new TileEntityChest() : (string20.equals("Furnace") ? new TileEntityFurnace() : null)) != null) {
					int i15 = i23 % 1024;
					int i24 = (i23 >> 10) % 1024;
					i23 = (i23 >> 20) % 1024;
					((TileEntity)object21).readFromNBT(nBTTagCompound4);
					world9.setBlockTileEntity(i15, i24, i23, (TileEntity)object21);
				} else {
					System.out.println("Skipping unknown tile entity id \"" + string14 + "\"");
				}
			} catch (Exception exception11) {
				System.out.println("Error reading tileentity");
				exception11.printStackTrace();
			}
		}

		return world9;
	}

	protected Entity loadEntity(World world, String playerName) {
		return (Entity)(playerName.equals("Pig") ? new EntityPig(world) : (playerName.equals("Sheep") ? new EntitySheep(world) : (playerName.equals("Creeper") ? new EntityCreeper(world) : (playerName.equals("Skeleton") ? new EntitySkeleton(world) : (playerName.equals("Spider") ? new EntitySpider(world) : (playerName.equals("Zombie") ? new EntityZombie(world) : (playerName.equals("Giant") ? new EntityGiantZombie(world) : (playerName.equals("Item") ? new EntityItem(world) : (playerName.equals("Painting") ? new EntityPainting(world) : null)))))))));
	}

	public final void save(World world, OutputStream outputStream2) throws IOException {
		if(this.guiLoading != null) {
			this.guiLoading.displayProgressMessage("Saving level");
		}

		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing level..");
		}

		NBTTagCompound nBTTagCompound3;
		(nBTTagCompound3 = new NBTTagCompound()).setInteger("CloudColor", world.cloudColor);
		nBTTagCompound3.setInteger("SkyColor", world.skyColor);
		nBTTagCompound3.setInteger("FogColor", world.fogColor);
		nBTTagCompound3.setByte("SkyBrightness", (byte)world.skyBrightness);
		nBTTagCompound3.setShort("CloudHeight", (short)world.cloudHeight);
		nBTTagCompound3.setShort("SurroundingGroundHeight", (short)world.groundLevel);
		nBTTagCompound3.setShort("SurroundingWaterHeight", (short)world.waterLevel);
		nBTTagCompound3.setByte("SurroundingGroundType", (byte)Block.grass.blockID);
		nBTTagCompound3.setByte("SurroundingWaterType", (byte)world.defaultFluid);
		nBTTagCompound3.setShort("TimeOfDay", (short)world.worldTime);
		NBTTagCompound nBTTagCompound4;
		(nBTTagCompound4 = new NBTTagCompound()).setShort("Width", (short)world.width);
		nBTTagCompound4.setShort("Length", (short)world.length);
		nBTTagCompound4.setShort("Height", (short)world.height);
		nBTTagCompound4.setByteArray("Blocks", world.blocks);
		nBTTagCompound4.setByteArray("Data", world.data);
		NBTTagList nBTTagList5;
		(nBTTagList5 = new NBTTagList()).setTag(new NBTTagShort((short)world.xSpawn));
		nBTTagList5.setTag(new NBTTagShort((short)world.ySpawn));
		nBTTagList5.setTag(new NBTTagShort((short)world.zSpawn));
		nBTTagCompound4.setTag("Spawn", nBTTagList5);
		NBTTagCompound nBTTagCompound15;
		(nBTTagCompound15 = new NBTTagCompound()).setString("Author", world.authorName);
		nBTTagCompound15.setString("Name", world.name);
		nBTTagCompound15.setLong("CreatedOn", world.createTime);
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing entities..");
		}

		NBTTagList nBTTagList6 = new NBTTagList();
		Iterator iterator7 = world.entityMap.entities.iterator();

		while(iterator7.hasNext()) {
			Entity entity8 = (Entity)iterator7.next();
			NBTTagCompound nBTTagCompound9 = new NBTTagCompound();
			entity8.writeToNBT(nBTTagCompound9);
			if(!nBTTagCompound9.emptyNBTMap()) {
				nBTTagList6.setTag(nBTTagCompound9);
			}
		}

		NBTTagList nBTTagList16 = new NBTTagList();
		Iterator iterator17 = world.map.keySet().iterator();

		while(iterator17.hasNext()) {
			int i19 = ((Integer)iterator17.next()).intValue();
			NBTTagCompound nBTTagCompound10;
			(nBTTagCompound10 = new NBTTagCompound()).setInteger("Pos", i19);
			((TileEntity)world.map.get(i19)).writeToNBT(nBTTagCompound10);
			nBTTagList16.setTag(nBTTagCompound10);
		}

		NBTTagCompound nBTTagCompound18;
		(nBTTagCompound18 = new NBTTagCompound()).setKey("MinecraftLevel");
		nBTTagCompound18.setCompoundTag("About", nBTTagCompound15);
		nBTTagCompound18.setCompoundTag("Map", nBTTagCompound4);
		nBTTagCompound18.setCompoundTag("Environment", nBTTagCompound3);
		nBTTagCompound18.setTag("Entities", nBTTagList6);
		nBTTagCompound18.setTag("TileEntities", nBTTagList16);
		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Writing..");
		}

		NBTTagCompound world1 = nBTTagCompound18;
		DataOutputStream dataOutputStream14 = new DataOutputStream(new GZIPOutputStream(outputStream2));

		try {
			NBTBase.writeTag(world1, dataOutputStream14);
		} finally {
			dataOutputStream14.close();
		}

	}
}