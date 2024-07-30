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

	public final SoundPoolEntry addSound(String var1, File var2) {
		try {
			String var3 = var1;

			for(var1 = var1.substring(0, var1.indexOf(".")); Character.isDigit(var1.charAt(var1.length() - 1)); var1 = var1.substring(0, var1.length() - 1)) {
			}

			var1 = var1.replaceAll("/", ".");
			if(!this.nameToSoundPoolEntriesMapping.containsKey(var1)) {
				this.nameToSoundPoolEntriesMapping.put(var1, new ArrayList());
			}

			SoundPoolEntry var5 = new SoundPoolEntry(var3, var2.toURI().toURL());
			((List)this.nameToSoundPoolEntriesMapping.get(var1)).add(var5);
			++this.numberOfSoundPoolEntries;
			return var5;
		} catch (MalformedURLException var4) {
			var4.printStackTrace();
			throw new RuntimeException(var4);
		}
	}

	public final SoundPoolEntry getRandomSoundFromSoundPool(String var1) {
		List var2 = (List)this.nameToSoundPoolEntriesMapping.get(var1);
		return var2 == null ? null : (SoundPoolEntry)var2.get(this.rand.nextInt(var2.size()));
	}
}
