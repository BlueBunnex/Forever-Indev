package net.minecraft.game.level.block;

public class StepSound {
	private String sound;
	public final float soundVolume;
	public final float soundPitch;

	public StepSound(String sound, float soundVolume, float soundPitch) {
		this.sound = sound;
		this.soundVolume = soundVolume;
		this.soundPitch = soundPitch;
	}

	public String stepSoundDir() {
		return "step." + this.sound;
	}

	public final String stepSoundDir2() {
		return "step." + this.sound;
	}
}