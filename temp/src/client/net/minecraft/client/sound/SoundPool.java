package net.minecraft.client.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SoundPool {
	private Random rand = new Random();
	private Map nameToSoundPoolEntriesMapping = new HashMap();
	private int numberOfSoundPoolEntries = 0;

	public final SoundPoolEntry addSound(String string1, File file2) {
		try {
			String string3 = string1;

			for(string1 = string1.substring(0, string1.indexOf(".")); Character.isDigit(string1.charAt(string1.length() - 1)); string1 = string1.substring(0, string1.length() - 1)) {
			}

			string1 = string1.replaceAll("/", ".");
			if(!this.nameToSoundPoolEntriesMapping.containsKey(string1)) {
				this.nameToSoundPoolEntriesMapping.put(string1, new ArrayList());
			}

			SoundPoolEntry soundPoolEntry5 = new SoundPoolEntry(string3, file2.toURI().toURL());
			((List)this.nameToSoundPoolEntriesMapping.get(string1)).add(soundPoolEntry5);
			++this.numberOfSoundPoolEntries;
			return soundPoolEntry5;
		} catch (MalformedURLException malformedURLException4) {
			malformedURLException4.printStackTrace();
			throw new RuntimeException(malformedURLException4);
		}
	}

	public final SoundPoolEntry getRandomSoundFromSoundPool(String string1) {
		List list2;
		return (list2 = (List)this.nameToSoundPoolEntriesMapping.get(string1)) == null ? null : (SoundPoolEntry)list2.get(this.rand.nextInt(list2.size()));
	}
}