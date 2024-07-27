package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt() {
	}

	public NBTTagInt(int i1) {
		this.intValue = i1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeInt(this.intValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.intValue = dataInput1.readInt();
	}

	public final byte getType() {
		return (byte)3;
	}

	public final String toString() {
		return "" + this.intValue;
	}
}