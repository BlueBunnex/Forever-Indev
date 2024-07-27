package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class NBTTagCompound extends NBTBase {
	private Map tagMap = new HashMap();

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		Iterator iterator2 = this.tagMap.values().iterator();

		while(iterator2.hasNext()) {
			NBTBase.writeTag((NBTBase)iterator2.next(), dataOutput1);
		}

		dataOutput1.writeByte(0);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.tagMap.clear();

		NBTBase nBTBase2;
		while((nBTBase2 = NBTBase.readTag(dataInput1)).getType() != 0) {
			this.tagMap.put(nBTBase2.getKey(), nBTBase2);
		}

	}

	public final byte getType() {
		return (byte)10;
	}

	public final void setTag(String string1, NBTBase nBTBase2) {
		this.tagMap.put(string1, nBTBase2.setKey(string1));
	}

	public final void setByte(String string1, byte b2) {
		this.tagMap.put(string1, (new NBTTagByte(b2)).setKey(string1));
	}

	public final void setShort(String string1, short s2) {
		this.tagMap.put(string1, (new NBTTagShort(s2)).setKey(string1));
	}

	public final void setInteger(String string1, int i2) {
		this.tagMap.put(string1, (new NBTTagInt(i2)).setKey(string1));
	}

	public final void setLong(String string1, long j2) {
		this.tagMap.put(string1, (new NBTTagLong(j2)).setKey(string1));
	}

	public final void setFloat(String string1, float f2) {
		this.tagMap.put(string1, (new NBTTagFloat(f2)).setKey(string1));
	}

	public final void setString(String string1, String string2) {
		this.tagMap.put(string1, (new NBTTagString(string2)).setKey(string1));
	}

	public final void setByteArray(String string1, byte[] b2) {
		this.tagMap.put(string1, (new NBTTagByteArray(b2)).setKey(string1));
	}

	public final void setCompoundTag(String string1, NBTTagCompound nBTTagCompound2) {
		this.tagMap.put(string1, nBTTagCompound2.setKey(string1));
	}

	public final void setBoolean(String string1, boolean z2) {
		this.setByte(string1, (byte)(z2 ? 1 : 0));
	}

	public final boolean hasKey(String string1) {
		return this.tagMap.containsKey(string1);
	}

	public final byte getByte(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagByte)this.tagMap.get(string1)).byteValue;
	}

	public final short getShort(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagShort)this.tagMap.get(string1)).shortValue;
	}

	public final int getInteger(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagInt)this.tagMap.get(string1)).intValue;
	}

	public final long getLong(String string1) {
		return !this.tagMap.containsKey(string1) ? 0L : ((NBTTagLong)this.tagMap.get(string1)).longValue;
	}

	public final float getFloat(String string1) {
		return !this.tagMap.containsKey(string1) ? 0.0F : ((NBTTagFloat)this.tagMap.get(string1)).floatValue;
	}

	public final String getString(String string1) {
		return !this.tagMap.containsKey(string1) ? "" : ((NBTTagString)this.tagMap.get(string1)).stringValue;
	}

	public final byte[] getByteArray(String string1) {
		return !this.tagMap.containsKey(string1) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(string1)).byteArray;
	}

	public final NBTTagCompound getCompoundTag(String string1) {
		return !this.tagMap.containsKey(string1) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(string1);
	}

	public final NBTTagList getTagList(String string1) {
		return !this.tagMap.containsKey(string1) ? new NBTTagList() : (NBTTagList)this.tagMap.get(string1);
	}

	public final boolean getBoolean(String string1) {
		return this.getByte(string1) != 0;
	}

	public final String toString() {
		return "" + this.tagMap.size() + " entries";
	}

	public final boolean emptyNBTMap() {
		return this.tagMap.isEmpty();
	}
}