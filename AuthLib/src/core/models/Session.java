package core.models;

import java.util.UUID;

public class Session {
	
	public UUID sessionID;
	public LoginRequest lastRequest;

	public Session(LoginRequest lastRequest) {
		sessionID = UUID.randomUUID();
		this.lastRequest = lastRequest;
	}

}
