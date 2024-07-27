package net.minecraft.client.sound;

import java.net.URL;

public final class SoundPoolEntry {
	public String soundName;
	public URL soundUrl;

	public SoundPoolEntry(String var1, URL var2) {
		this.soundName = var1;
		this.soundUrl = var2;
	}
}
