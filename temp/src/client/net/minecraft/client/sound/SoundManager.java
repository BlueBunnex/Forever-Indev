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

	public final void loadSoundSettings(GameSettings gameSettings) {
		this.options = gameSettings;
		if(!this.loaded && (gameSettings.sound || gameSettings.music)) {
			this.tryToSetLibraryAndCodecs();
		}

	}

	private void tryToSetLibraryAndCodecs() {
		try {
			boolean z1 = this.options.sound;
			boolean z2 = this.options.music;
			this.options.sound = false;
			this.options.music = false;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			this.sndSystem = new SoundSystem();
			this.options.sound = z1;
			this.options.music = z2;
			this.options.saveOptions();
		} catch (Throwable throwable3) {
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

	public final void addSound(String string1, File file2) {
		this.soundPoolSounds.addSound(string1, file2);
	}

	public final void addMusic(String string1, File file2) {
		this.soundPoolMusic.addSound(string1, file2);
	}

	public final void playRandomMusicIfReady(float f1, float f2, float f3) {
		if(this.loaded && this.options.music) {
			if(!this.sndSystem.playing("BgMusic")) {
				SoundPoolEntry soundPoolEntry4 = this.soundPoolMusic.getRandomSoundFromSoundPool("calm");
				this.sndSystem.newStreamingSource(true, "BgMusic", soundPoolEntry4.soundUrl, soundPoolEntry4.soundName, false, f1, f2, f3, 2, 32.0F);
				this.sndSystem.play("BgMusic");
			}

		}
	}

	public final void setListener(EntityLiving entityLiving, float f2) {
		if(this.loaded && this.options.sound) {
			if(entityLiving != null) {
				float f3 = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * f2;
				float f4 = entityLiving.prevRotationYaw + (entityLiving.rotationYaw - entityLiving.prevRotationYaw) * f2;
				float f5 = entityLiving.prevPosX + (entityLiving.posX - entityLiving.prevPosX) * f2;
				float f6 = entityLiving.prevPosY + (entityLiving.posY - entityLiving.prevPosY) * f2;
				float entityLiving1 = entityLiving.prevPosZ + (entityLiving.posZ - entityLiving.prevPosZ) * f2;
				f2 = MathHelper.cos(-f4 * 0.017453292F - (float)Math.PI);
				f4 = MathHelper.sin(-f4 * 0.017453292F - (float)Math.PI);
				float f7 = MathHelper.cos(-f3 * 0.017453292F);
				f3 = MathHelper.sin(-f3 * 0.017453292F);
				float f8 = -f4 * f7;
				float f9 = -f2 * f7;
				f4 = -f4 * f3;
				f2 = -f2 * f3;
				this.sndSystem.setListenerPosition(f5, f6, entityLiving1);
				this.sndSystem.setListenerOrientation(f8, f3, f9, f4, f7, f2);
			}
		}
	}

	public final void playSound(String string1, float f2, float f3, float f4, float f5, float f6) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry soundPoolEntry9;
			if((soundPoolEntry9 = this.soundPoolSounds.getRandomSoundFromSoundPool(string1)) != null && f5 > 0.0F) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				String string7 = "sound_" + this.latestSoundID;
				float f8 = 16.0F;
				if(f5 > 1.0F) {
					f8 = 16.0F * f5;
				}

				this.sndSystem.newSource(f5 > 1.0F, string7, soundPoolEntry9.soundUrl, soundPoolEntry9.soundName, false, f2, f3, f4, 2, f8);
				this.sndSystem.setPitch(string7, f6);
				if(f5 > 1.0F) {
					f5 = 1.0F;
				}

				this.sndSystem.setVolume(string7, f5);
				this.sndSystem.play(string7);
			}

		}
	}

	public final void playSoundFX(String string1, float f2, float f3) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry soundPoolEntry4;
			if((soundPoolEntry4 = this.soundPoolSounds.getRandomSoundFromSoundPool(string1)) != null) {
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				String string5 = "sound_" + this.latestSoundID;
				this.sndSystem.newSource(false, string5, soundPoolEntry4.soundUrl, soundPoolEntry4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
				this.sndSystem.setPitch(string5, 1.0F);
				this.sndSystem.setVolume(string5, 0.25F);
				this.sndSystem.play(string5);
			}

		}
	}
}