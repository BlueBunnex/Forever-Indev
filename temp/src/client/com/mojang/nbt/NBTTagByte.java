package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte b1) {
		this.byteValue = b1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeByte(this.byteValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.byteValue = dataInput1.readByte();
	}

	public final byte getType() {
		return (byte)1;
	}

	public final String toString() {
		return "" + this.byteValue;
	}
}