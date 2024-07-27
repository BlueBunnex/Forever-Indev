package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagLong extends NBTBase {
	public long longValue;

	public NBTTagLong() {
	}

	public NBTTagLong(long j1) {
		this.longValue = j1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeLong(this.longValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.longValue = dataInput1.readLong();
	}

	public final byte getType() {
		return (byte)4;
	}

	public final String toString() {
		return "" + this.longValue;
	}
}