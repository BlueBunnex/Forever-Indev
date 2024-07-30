package net.minecraft.client;

public final class Session {
	
	public String username;
	public String sessionId;

	public Session(String username, String sessionId) {
		this.username = username;
		this.sessionId = sessionId;
	}

}
