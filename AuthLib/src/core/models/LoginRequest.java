package core.models;

import java.net.InetAddress;
import javax.servlet.http.Cookie;

/**
 * @author krism
 * Internal model of a LoginRequest.
 * This is what the system will use to execute its policy logic. 
 * This was decoupled from the net work layer via wrappers so the core policy logic 
 * can be adapted to different / changing net works easily
 */

public class LoginRequest {
	//Maybe use hashMap or something if there are to be more properties later on
	public IPWrapper address;
	public CookieWrapper cookie;
	public UserAgentWrapper userAgent;
	
	public LoginRequest(IPWrapper address, CookieWrapper cookie, UserAgentWrapper userAgent) {
		this. address = address; this.cookie = cookie; this.userAgent = userAgent;
	}
	
	public boolean equals(LoginRequest req) {
		return address.equals(req.address) && cookie.equals(req.cookie) && userAgent.equals(req.userAgent);
	}

}
