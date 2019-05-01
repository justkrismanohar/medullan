package core.models;

import java.time.Instant;
import java.util.UUID;

public class Session {
	
	public UUID sessionID;
	public Instant creationTime;
	public String username;
	
	public Session(LoginRequest req) {
		sessionID = UUID.randomUUID();
		this.creationTime = Instant.now();
		this.username = req.loginDetails.userName;
	}

}
