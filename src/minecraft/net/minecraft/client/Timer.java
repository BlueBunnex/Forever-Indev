package net.minecraft.client;

public final class Timer {
	float ticksPerSecond = 20.0F;
	private double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	private float timerSpeed = 1.0F;
	private float elapsedPartialTicks = 0.0F;
	private long lastSyncSysClock = System.currentTimeMillis();
	private long lastSyncHRClock = System.nanoTime() / 1000000L;
	private double timeSyncAdjustment = 1.0D;

	public Timer(float var1) {
	}

	public final void updateTimer() {
		long var1 = System.currentTimeMillis();
		long var3 = var1 - this.lastSyncSysClock;
		long var5 = System.nanoTime() / 1000000L;
		double var9;
		if(var3 > 1000L) {
			long var7 = var5 - this.lastSyncHRClock;
			var9 = (double)var3 / (double)var7;
			this.timeSyncAdjustment += (var9 - this.timeSyncAdjustment) * (double)0.2F;
			this.lastSyncSysClock = var1;
			this.lastSyncHRClock = var5;
		}

		if(var3 < 0L) {
			this.lastSyncSysClock = var1;
			this.lastSyncHRClock = var5;
		}

		double var11 = (double)var5 / 1000.0D;
		var9 = (var11 - this.lastHRTime) * this.timeSyncAdjustment;
		this.lastHRTime = var11;
		if(var9 < 0.0D) {
			var9 = 0.0D;
		}

		if(var9 > 1.0D) {
			var9 = 1.0D;
		}

		this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + var9 * (double)this.timerSpeed * (double)this.ticksPerSecond);
		this.elapsedTicks = (int)this.elapsedPartialTicks;
		this.elapsedPartialTicks -= (float)this.elapsedTicks;
		if(this.elapsedTicks > 10) {
			this.elapsedTicks = 10;
		}

		this.renderPartialTicks = this.elapsedPartialTicks;
	}
}
