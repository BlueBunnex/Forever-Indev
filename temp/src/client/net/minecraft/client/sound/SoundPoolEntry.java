package net.minecraft.client.sound;

import java.net.URL;

public final class SoundPoolEntry {
	public String soundName;
	public URL soundUrl;

	public SoundPoolEntry(String string1, URL url) {
		this.soundName = string1;
		this.soundUrl = url;
	}
}