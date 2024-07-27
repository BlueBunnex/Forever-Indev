package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float f1) {
		this.floatValue = f1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeFloat(this.floatValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.floatValue = dataInput1.readFloat();
	}

	public final byte getType() {
		return (byte)5;
	}

	public final String toString() {
		return "" + this.floatValue;
	}
}