package core.utils;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.models.wrappers.UserAgentWrapper;

public class LoginRequestFactory extends Factory {
	
	public static LoginRequest getBasicLoginRequestWithEmailAs(String username) throws IPCreationFailed {
		if(username == null) {
			log.error("Requested LoginRequest with null username");
			username = "";
		}
		
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		LoginDetails detailsPass = new LoginDetails(username,"USername1");
		LoginRequest req = new LoginRequest(ip1,c1,a1,detailsPass);
		return req;
	}
}
