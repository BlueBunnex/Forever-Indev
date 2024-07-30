package net.minecraft.client.sound;

import java.io.File;
import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import util.MathHelper;

public final class SoundManager {
	private SoundSystem sndSystem;
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private int latestSoundID = 0;
	private GameSettings options;
	private boolean loaded = false;

	public final void loadSoundSettings(GameSettings var1) {
		this.options = var1;
		if(!this.loaded && (var1.sound || var1.music)) {
			this.tryToSetLibraryAndCodecs();
		}

	}

	private void tryToSetLibraryAndCodecs() {
		try {
			boolean var1 = this.options.sound;
			boolean var2 = this.options.music;
			this.options.sound = false;
			this.options.music = false;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			this.sndSystem = new SoundSystem();
			this.options.sound = var1;
			this.options.music = var2;
			this.options.saveOptions();
		} catch (Throwable var3) {
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		this.loaded = true;
	}

	public final void onSoundOptionsChanged() {
		if(!this.loaded && (this.options.sound || this.options.music)) {
			this.tryToSetLibraryAndCodecs();
		}

		if(!this.options.music) {
			this.sndSystem.stop("BgMusic");
		}

	}

	public final void closeMinecraft() {
		if(this.loaded) {
			this.sndSystem.cleanup();
		}

	}

	public final void addSound(String var1, File var2) {
		this.soundPoolSounds.addSound(var1, var2);
	}

	public final void addMusic(String var1, File var2) {
		this.soundPoolMusic.addSound(var1, var2);
	}

	public final void playRandomMusicIfReady(float var1, float var2, float var3) {
		if(this.loaded && this.options.music) {
			if(!this.sndSystem.playing("BgMusic")) {
				SoundPoolEntry var4 = this.soundPoolMusic.getRandomSoundFromSoundPool("calm");
				this.sndSystem.newStreamingSource(true, "BgMusic", var4.soundUrl, var4.soundName, false, var1, var2, var3, 2, 32.0F);
				this.sndSystem.play("BgMusic");
			}

		}
	}

	public final void setListener(EntityLiving var1, float var2) {
		if(this.loaded && this.options.sound) {
			if(var1 != null) {
				float var3 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var2;
				float var4 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2;
				float var5 = var1.prevPosX + (var1.posX - var1.prevPosX) * var2;
				float var6 = var1.prevPosY + (var1.posY - var1.prevPosY) * var2;
				float var10 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var2;
				var2 = MathHelper.cos(-var4 * ((float)Math.PI / 180.0F) - (float)Math.PI);
				var4 = MathHelper.sin(-var4 * ((float)Math.PI / 180.0F) - (float)Math.PI);
				float var7 = MathHelper.cos(-var3 * ((float)Math.PI / 180.0F));
				var3 = MathHelper.sin(-var3 * ((float)Math.PI / 180.0F));
				float var8 = -var4 * var7;
				float var9 = -var2 * var7;
				var4 = -var4 * var3;
				var2 = -var2 * var3;
				this.sndSystem.setListenerPosition(var5, var6, var10);
				this.sndSystem.setListenerOrientation(var8, var3, var9, var4, var7, var2);
			}
		}
	}

	public final void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry var9 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var9 != null && var5 > 0.0F) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				String var7 = "sound_" + this.latestSoundID;
				float var8 = 16.0F;
				if(var5 > 1.0F) {
					var8 = 16.0F * var5;
				}

				this.sndSystem.newSource(var5 > 1.0F, var7, var9.soundUrl, var9.soundName, false, var2, var3, var4, 2, var8);
				this.sndSystem.setPitch(var7, var6);
				if(var5 > 1.0F) {
					var5 = 1.0F;
				}

				this.sndSystem.setVolume(var7, var5);
				this.sndSystem.play(var7);
			}

		}
	}

	public final void playSoundFX(String var1, float var2, float var3) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
			if(var4 != null) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				String var5 = "sound_" + this.latestSoundID;
				this.sndSystem.newSource(false, var5, var4.soundUrl, var4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
				this.sndSystem.setPitch(var5, 1.0F);
				this.sndSystem.setVolume(var5, 0.25F);
				this.sndSystem.play(var5);
			}

		}
	}
}
