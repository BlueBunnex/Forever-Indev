package net.minecraft.client;

final class ThreadSleepForever extends Thread {
	ThreadSleepForever(Minecraft minecraft, String string2) {
		super(string2);
		this.setDaemon(true);
		this.start();
	}

	public final void run() {
		while(true) {
			try {
				Thread.sleep(2147483647L);
			} catch (InterruptedException interruptedException1) {
			}
		}
	}
}