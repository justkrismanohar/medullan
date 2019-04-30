package tests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.api.EndPoint;
import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.UserAgentWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class ApiLayerTest {

	@Test
	public void testEndPoint() throws IPCreationFailed {
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		
		LoginDetails detailsPass = new LoginDetails("user1@users.com","USername1");
		LoginDetails detailsFail = new LoginDetails("user1@users.com","USername11");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,detailsPass);
		EndPoint end = new EndPoint();
		
		end.register(req);//registers user1
		
		assertTrue(end.login(req));
		
		req.loginDetails = detailsFail;
		
		//Simulate 3 login failures. This should block registered user
		for(int i =0; i <3; i++)
			end.login(req);
		
		assertFalse(end.login(req));//check fail login
		
		req.loginDetails = detailsPass;
		assertFalse(end.login(req));//Should still fail, registered user is blocked
		assertTrue(QueryLayerFactory.getInstance().isUserNameLocked(req.loginDetails.userName));//verify at the data layer too
		
		LoginDetails otherUser = new LoginDetails("other","user");//Unknown user from same signatrue
		
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
	public void testRegister() throws IPCreationFailed {
		EndPoint end = new EndPoint();
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		
		LoginDetails newUser = new LoginDetails("user","user1");
		LoginRequest req = new LoginRequest(ip1,c1,a1,newUser);
		
		assertFalse(end.register(req));
		
		newUser.encryptedPassword = "PaSSword5";
		assertFalse(end.register(req));
		
		newUser.userName = "user@domain.com";
		assertTrue(end.register(req));
	}
	
	@Test
	public void testSession() throws IPCreationFailed {
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		String username = "session1@users.com";
		LoginDetails detailsPass = new LoginDetails(username,"USername1");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,detailsPass);
		
		EndPoint end = new EndPoint();
		
		end.register(req);//registers user1
		
		assertFalse(end.autheticateSession(username));//no session user needs to log in
		end.login(req);
		assertTrue(end.autheticateSession(username));//user logged in should now have a valid session
		
		//Simulate some time passing 
		QueryLayer q = QueryLayerFactory.getInstance();
		Session userSession = q.getUserSession(username);
		userSession.lastRequest.dateTime = userSession.lastRequest.dateTime.minusMinutes(100);
		
		assertFalse(end.autheticateSession(username));//session should timeout
		assertTrue(q.getUserSession(username) == null);//verify at data layer, that session was removed
	}
}
