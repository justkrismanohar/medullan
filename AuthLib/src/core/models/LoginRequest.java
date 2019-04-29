package core.models;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import javax.servlet.http.Cookie;

/**
 * @author krism
 * Internal model of a LoginRequest.
 * This is what the system will use to execute its policy logic. 
 * This was decoupled from the net work layer via wrappers so the core policy logic 
 * can be adapted to different / changing net works easily
 */

public class LoginRequest {
	public static enum RequestStatus {
		SUCCESSFUL,
		FAILED,
		PENDING
	}
	
	//Maybe use hashMap or something if there are to be more properties later on
	public IPWrapper address;
	public CookieWrapper cookie;
	public UserAgentWrapper userAgent;
	public LoginDetails loginDetails;
	public RequestStatus status;
	public Instant dateTime;
	public UUID requestID;
	public UUID sessionID;
	
	public LoginRequest(IPWrapper address, CookieWrapper cookie, UserAgentWrapper userAgent) {
		this. address = address; this.cookie = cookie; this.userAgent = userAgent;
		loginDetails = new LoginDetails("","");
		this.status = RequestStatus.PENDING;
		this.dateTime = Instant.now();
		this.requestID = UUID.randomUUID();
		sessionID = null;//empty
	}
	
	public LoginRequest(IPWrapper address, CookieWrapper cookie, UserAgentWrapper userAgent, LoginDetails loginDetails) {
		this. address = address; this.cookie = cookie; this.userAgent = userAgent; this.loginDetails = loginDetails;
		this.status = RequestStatus.PENDING;
		this.dateTime = Instant.now();
		this.requestID = UUID.randomUUID();
		sessionID = null;//empty
	}
	
	public boolean equals(LoginRequest req) {
		return address.equals(req.address) && cookie.equals(req.cookie) && userAgent.equals(req.userAgent);
	}

}
