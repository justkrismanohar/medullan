package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.api.EndPoint;
import core.models.CookieWrapper;
import core.models.IPWrapper;
import core.models.IPWrapper.IPCreationFailed;
import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.UserAgentWrapper;
import core.queries.QueryLayerFactory;

class ApiLayerTest {

	@Test
	void testEndPoint() throws IPCreationFailed {
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		
		LoginDetails detailsPass = new LoginDetails("user1","user1");
		LoginDetails detailsFail = new LoginDetails("user1","user11");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,detailsPass);
		EndPoint end = new EndPoint();
		
		assertTrue(end.login(req));
		
		req.loginDetails = detailsFail;
		
		//Simulate 3 login failures. This should block user
		for(int i =0; i <3; i++)
			end.login(req);
		
		assertFalse(end.login(req));//check fail login
		
		req.loginDetails = detailsPass;
		assertFalse(end.login(req));//Should still fail, user is blocked
		assertTrue(QueryLayerFactory.getInstance().isUserNameLocked(req.loginDetails.userName));//verify at the data layer too
		
		LoginDetails otherUser = new LoginDetails("other","user");
		
		req.loginDetails = otherUser;
		for(int i= 0; i < 10; i++)//simulate additional 10 to total 13 requests from same signature
			end.login(req);
		
		assertFalse(end.login(req));//this signature should be blocked now
		LoginDetails anotherUser = new LoginDetails("another","another");
		req.loginDetails = anotherUser;
		assertFalse(end.login(req));//verify with another user (new but from same signature)
		assertTrue(QueryLayerFactory.getInstance().isSignaturetInBlockList(req));//verify at data layer
		
	}

	
	@Test
	void testRegister() {
		EndPoint end = new EndPoint();
		LoginDetails newUser = new LoginDetails("user","user1");
		assertFalse(end.register(newUser));
		
		newUser.encryptedPassword = "PaSSword5";
		assertFalse(end.register(newUser));
		
		newUser.userName = "user@domain.com";
		assertTrue(end.register(newUser));
	}
}
