package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class NBTTagList extends NBTBase {
	private List tagList = new ArrayList();
	private byte tagType;

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		if(this.tagList.size() > 0) {
			this.tagType = ((NBTBase)this.tagList.get(0)).getType();
		} else {
			this.tagType = 1;
		}

		dataOutput1.writeByte(this.tagType);
		dataOutput1.writeInt(this.tagList.size());

		for(int i2 = 0; i2 < this.tagList.size(); ++i2) {
			((NBTBase)this.tagList.get(i2)).writeTagContents(dataOutput1);
		}

	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.tagType = dataInput1.readByte();
		int i2 = dataInput1.readInt();
		this.tagList = new ArrayList();

		for(int i3 = 0; i3 < i2; ++i3) {
			NBTBase nBTBase4;
			(nBTBase4 = NBTBase.createTagOfType(this.tagType)).readTagContents(dataInput1);
			this.tagList.add(nBTBase4);
		}

	}

	public final byte getType() {
		return (byte)9;
	}

	public final String toString() {
		StringBuilder stringBuilder10000 = (new StringBuilder()).append("").append(this.tagList.size()).append(" entries of type ");
		byte b1 = this.tagType;
		String string10001;
		switch(this.tagType) {
		case 0:
			string10001 = "TAG_End";
			break;
		case 1:
			string10001 = "TAG_Byte";
			break;
		case 2:
			string10001 = "TAG_Short";
			break;
		case 3:
			string10001 = "TAG_Int";
			break;
		case 4:
			string10001 = "TAG_Long";
			break;
		case 5:
			string10001 = "TAG_Float";
			break;
		case 6:
			string10001 = "TAG_Double";
			break;
		case 7:
			string10001 = "TAG_Byte_Array";
			break;
		case 8:
			string10001 = "TAG_String";
			break;
		case 9:
			string10001 = "TAG_List";
			break;
		case 10:
			string10001 = "TAG_Compound";
			break;
		default:
			string10001 = "UNKNOWN";
		}

		return stringBuilder10000.append(string10001).toString();
	}

	public final void setTag(NBTBase nbtBase) {
		this.tagType = nbtBase.getType();
		this.tagList.add(nbtBase);
	}

	public final NBTBase tagAt(int i1) {
		return (NBTBase)this.tagList.get(i1);
	}

	public final int tagCount() {
		return this.tagList.size();
	}
}