package core.models;

import java.time.Instant;
import java.util.UUID;

public class Session {
	
	public UUID sessionID;
	public Instant requestTime;
	public String username;

	public Session(LoginRequest req) {
		sessionID = UUID.randomUUID();
		this.username = req.loginDetails.userName;
		this.requestTime = req.dateTime;
	}

}
