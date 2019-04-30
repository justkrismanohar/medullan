package tests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.time.temporal.ChronoUnit;

import org.junit.Test;

import core.api.EndPoint;
import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.UserAgentWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.queries.MockDB;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;
import core.utils.UnitTestHelper;

public class ApiLayerTest {

	@Test
	public void EndPoint_EndToEndTest() throws Exception {
		
		MockDB q = UnitTestHelper.getMockDBInstance();
		
		LoginRequest validLogin = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user1@users.com","USername1");
		LoginRequest invalidLogin = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user1@users.com","USername11");
		
		EndPoint end = new EndPoint("jUnitTestConfig.xml");
		
		end.register(validLogin);//registers user1
		
		assertThat("Username and password meets policy requirements in jUnitTestConfig.xml. So .login should return true.",end.login(validLogin),is(true));
		
		//Simulate 3 login failures. This should block registered user
		for(int i =0; i <3; i++)
			end.login(invalidLogin);
		
		assertThat("3 invalid logins. User should be locked. .login should return false.",end.login(invalidLogin),is(false));//check fail login
		
		assertThat("With valid credentails locked user should not be able to login in. .login should return false.",end.login(validLogin),is(false));//Should still fail, registered user is blocked
		assertTrue("At data layer, isUserNameLocked should return true for a locked user.",q.isUserNameLocked(validLogin.loginDetails.userName));//verify at the data layer too
		
		//another user from the same signature
		LoginRequest otherUser = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"other","user");

		for(int i= 0; i < 10; i++)//simulate additional 10 to total 13 requests from same signature
			end.login(otherUser);
		
		
		assertThat("After 13 faild logins from the same signature. Signature is blocked and .login should return false.",end.login(otherUser),is(false));//this signature should be blocked now

		LoginRequest anotherUser = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"another","another");

		assertThat("Signature is blocked. .login for a completely new user form the same signature should return false.", end.login(anotherUser),is(false));//verify with another user (new but from same signature)
		assertThat("Verify at the data layer that the signature is blocked. .isSignatureInBlockedList should return true",q.isSignaturetInBlockList(anotherUser),is(true));//verify at data layer
		
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
		userSession.creationTime = userSession.creationTime.minus(100,ChronoUnit.MINUTES);
		
		assertFalse(end.autheticateSession(username));//session should timeout
		assertTrue(q.getUserSession(username) == null);//verify at data layer, that session was removed
	}
}
