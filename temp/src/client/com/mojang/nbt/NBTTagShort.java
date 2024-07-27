package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort() {
	}

	public NBTTagShort(short s1) {
		this.shortValue = s1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeShort(this.shortValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.shortValue = dataInput1.readShort();
	}

	public final byte getType() {
		return (byte)2;
	}

	public final String toString() {
		return "" + this.shortValue;
	}
}