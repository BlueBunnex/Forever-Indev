package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString() {
	}

	public NBTTagString(String string1) {
		this.stringValue = string1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		byte[] b2 = this.stringValue.getBytes("UTF-8");
		dataOutput1.writeShort(b2.length);
		dataOutput1.write(b2);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		byte[] b2 = new byte[dataInput1.readShort()];
		dataInput1.readFully(b2);
		this.stringValue = new String(b2, "UTF-8");
	}

	public final byte getType() {
		return (byte)8;
	}

	public final String toString() {
		return "" + this.stringValue;
	}
}