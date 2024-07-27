package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
	private String key = null;

	abstract void writeTagContents(DataOutput dataOutput1) throws IOException;

	abstract void readTagContents(DataInput dataInput1) throws IOException;

	public abstract byte getType();

	public final String getKey() {
		return this.key == null ? "" : this.key;
	}

	public final NBTBase setKey(String string1) {
		this.key = string1;
		return this;
	}

	public static NBTBase readTag(DataInput dataInput0) throws IOException {
		byte b1;
		if((b1 = dataInput0.readByte()) == 0) {
			return new NBTTagEnd();
		} else {
			NBTBase nBTBase3 = createTagOfType(b1);
			byte[] b2 = new byte[dataInput0.readShort()];
			dataInput0.readFully(b2);
			nBTBase3.key = new String(b2, "UTF-8");
			nBTBase3.readTagContents(dataInput0);
			return nBTBase3;
		}
	}

	public static void writeTag(NBTBase nbtBase, DataOutput dataOutput1) throws IOException {
		dataOutput1.writeByte(nbtBase.getType());
		if(nbtBase.getType() != 0) {
			byte[] b2 = nbtBase.getKey().getBytes("UTF-8");
			dataOutput1.writeShort(b2.length);
			dataOutput1.write(b2);
			nbtBase.writeTagContents(dataOutput1);
		}
	}

	public static NBTBase createTagOfType(byte b0) {
		switch(b0) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		default:
			return null;
		}
	}
}