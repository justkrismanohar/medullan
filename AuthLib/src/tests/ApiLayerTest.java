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
		
		end.register(validLogin);
		
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
	public void Register_VariousLogins() throws Exception {
		MockDB q = UnitTestHelper.getMockDBInstance();
		
		LoginRequest invalidUserNameAndPassword = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user","user1");
		LoginRequest invalidUserName = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user","PaSSword5");
		LoginRequest validLogin = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"use1@domain.com","PaSSword5");
		
		EndPoint end = new EndPoint("jUnitTestConfig.xml");
				
		assertThat("Invaild username and password. .register should return false.",end.register(invalidUserNameAndPassword),is(false));
		assertThat("Invaild username. .register should return false.",end.register(invalidUserName),is(false));
		assertThat("Vaild username and password. .register should return true.",end.register(validLogin),is(true));
	}
	
	@Test
	public void Session_LoginInThenSessionExpires() throws Exception {
		MockDB q = UnitTestHelper.getMockDBInstance();
		LoginRequest validLogin = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"session1@users.com","USername1");
		
		EndPoint end = new EndPoint("jUnitTestConfig.xml");
		end.register(validLogin);
		
		assertThat("User has not logged in as yet. There should be no session for him. .autheticateSession should return false.",end.authenticateSession(validLogin),is(false));//no session user needs to log in
		end.login(validLogin);
		assertThat("User logged in as yet. There should be a session for him. .autheticateSession should return true.",end.authenticateSession(validLogin),is(true));//no session user needs to log in
		
		//Simulate some time passing 
		Session userSession = q.getUserSession(validLogin.loginDetails.userName);
		userSession.creationTime = userSession.creationTime.minus(100,ChronoUnit.MINUTES);
		
		assertThat("100 mins passed since last log on. User should be logged out. .autheticateSession should return false.",end.authenticateSession(validLogin),is(false));//no session user needs to log in
		assertThat("Verify at the datalayer there is no session. .getUserSession should return null.",q.getUserSession(validLogin.loginDetails.userName),is(nullValue()));//verify at data layer, that session was removed
	}
}
