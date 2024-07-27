package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] b1) {
		this.byteArray = b1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeInt(this.byteArray.length);
		dataOutput1.write(this.byteArray);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		int i2 = dataInput1.readInt();
		this.byteArray = new byte[i2];
		dataInput1.readFully(this.byteArray);
	}

	public final byte getType() {
		return (byte)7;
	}

	public final String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}