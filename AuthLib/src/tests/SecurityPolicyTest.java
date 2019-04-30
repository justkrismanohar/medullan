package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.UserAgentWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.policy.security.CompositeANDSecurityPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.CompositeORSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.queries.MockDB;
import core.queries.QueryLayer;

public class SecurityPolicyTest {

	@Test
	public void testBasicBruteForce() throws IPCreationFailed {
		//Set up mockDB specifics for this test 
		MockDB q = new MockDB() {
			private int [] simulatedFailuresInLastXMins = {23,0};//force fail login, then force pass login
			private int count = 0;
			@Override
			public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins) {
				int failures = simulatedFailuresInLastXMins[count];
				count++;
				return failures;
			}
		};
		
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		
		LoginRequest signature = new LoginRequest(ip1,c1,a1);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		assertFalse(q.isSignaturetInBlockList(signature));
	
		/**
		 * Due to the MockDB config, first handleRequest(...) should faild and activate the policy to block request signature
		 */
		bruteForce.handleRequest(signature);
		assertTrue(q.isSignaturetInBlockList(signature));
		
		
		/**
		 * Due to the MockDB config, second handleRequest(...) simulates 20 mins had passed. This should pass security check.
		 * That is the block request action should not be activated
		 */
		q.resetBlockSignatures();//simulate 20 mins passed
		bruteForce.handleRequest(signature);
		assertFalse(q.isSignaturetInBlockList(signature));
		
	}

	
	@Test
	public void testConsecutiveFailedLogins() throws IPCreationFailed {
		//Set up mockDB specifics for this test 
		MockDB q = new MockDB() {
			private int [] simulatedFailedConsecutiveLogins = {0,3};//force pass login, then force fail login
			private int count = 0;
			@Override
			public int getNumFailedConnsecutiveByUser(String userName) {
				int failed = simulatedFailedConsecutiveLogins[count];
				count++;
				return failed;
			}
						
		};
		
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		String userName = "user1";
		LoginDetails details = new LoginDetails(userName,"PAass1");
		
		LoginRequest signature = new LoginRequest(ip1,c1,a1,details);
		q.registerUser(signature);
		
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		
		assertFalse(q.isUserNameLocked(userName));
	
		/**
		 * Due to the MockDB config, first handleRequest(...) should pass and NOT activate the policy to block user
		 */
		cFailed.handleRequest(signature);
		assertFalse(q.isUserNameLocked(userName));
		
		/**
		 * Due to the MockDB config, second handleRequest(...) simulates 3 consecutive failed logins. This should FAIL security check.
		 * That is the block request action should not be activated
		 */
		cFailed.handleRequest(signature);
		assertTrue(q.isUserNameLocked(userName));
	}
	
	
	@Test
	public void testORSecurityPolicy() throws IPCreationFailed {
		//Set up mockDB specifics for this test 
		MockDB q = new MockDB() {
			private int [] simulatedFailedConsecutiveLogins = {3,0,0};//Fail, Pass, Pass
			private int [] simulatedFailuresInLastXMins = {23,23,0};   //Fail, Fail, Pass 
			
			private int count = 0;
			private int tick = 0;
			private void updateCount() {
				tick++;
				count = tick/2;
			}
			@Override
			public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins) {
				int failures = simulatedFailuresInLastXMins[count];
				updateCount();
				return failures;
			}
			
			@Override
			public int getNumFailedConnsecutiveByUser(String userName) {
				int failed = simulatedFailedConsecutiveLogins[count];
				updateCount();
				return failed;
			}
		};
		
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		String userName = "user1";
		LoginDetails details = new LoginDetails(userName,"pass1");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,details);
		
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		CompositeORSecurityPolicy or = new CompositeORSecurityPolicy();
		or.add(bruteForce);//keep order to work with tick fucntion in mock DB
		or.add(cFailed);
		
		
		assertFalse(or.handleRequest(req));//Fail OR Fail is Fail	
		assertTrue(or.handleRequest(req));// Fail OR Pass is Pass
		assertTrue(or.handleRequest(req));// Pass OR Pass is Pass
		
	}
	
	@Test
	public void testANDSecurityPolicy() throws IPCreationFailed {
		//Set up mockDB specifics for this test 
		MockDB q = new MockDB() {
			private int [] simulatedFailedConsecutiveLogins = {0,3,3};//Pass, Fail, Fail
			private int [] simulatedFailuresInLastXMins = {0,0,23};   //Pass, Pass, Fail 
			
			private int count = 0;
			private int tick = 0;
			private void updateCount() {
				tick++;
				count = tick/2;
			}
			@Override
			public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins) {
				int failures = simulatedFailuresInLastXMins[count];
				updateCount();
				return failures;
			}
			
			@Override
			public int getNumFailedConnsecutiveByUser(String userName) {
				int failed = simulatedFailedConsecutiveLogins[count];
				updateCount();
				return failed;
			}
		};
		
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		String userName = "user1";
		LoginDetails details = new LoginDetails(userName,"pass1");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,details);
		
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		CompositeANDSecurityPolicy or = new CompositeANDSecurityPolicy();
		or.add(bruteForce);//keep order to work with tick function in mock DB
		or.add(cFailed);
		
		assertTrue(or.handleRequest(req));//Pass AND Pass is Pass	
		assertFalse(or.handleRequest(req));//Pass AND Fail is Fail
		assertFalse(or.handleRequest(req));// Fail AND Fail is Fail
		
	}
	
	@Test
	public void testLockoutPolicy() throws IPCreationFailed {
		
		MockDB q = new MockDB();
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		//set up a request
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		String userName = "user1";
		LoginDetails details = new LoginDetails(userName,"pass1");
		
		LoginRequest req = new LoginRequest(ip1,c1,a1,details);
		LockoutSecurityPolicy lp = new LockoutSecurityPolicy(20);
		
		assertTrue(lp.handleRequest(req));//Not blocked should pass
		
		q.blockSignature(req);//Say something caused it to be locked out
		assertFalse(lp.handleRequest(req));//Blocked so should fail
		
		req.dateTime = req.dateTime.minus(100,ChronoUnit.MINUTES);//pretend  100 mins passed
		assertTrue(lp.handleRequest(req));//handleRequest should unblock the signature
		assertFalse(q.isSignaturetInBlockList(req));//just double checking
	
	}
			
}
