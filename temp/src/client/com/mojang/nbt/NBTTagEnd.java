package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagEnd extends NBTBase {
	final void readTagContents(DataInput dataInput1) throws IOException {
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
	}

	public final byte getType() {
		return (byte)0;
	}

	public final String toString() {
		return "END";
	}
}