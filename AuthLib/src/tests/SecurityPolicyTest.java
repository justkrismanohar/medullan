package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.models.CookieWrapper;
import core.models.IPWrapper;
import core.models.IPWrapper.IPCreationFailed;
import core.models.LoginRequest;
import core.models.UserAgentWrapper;
import core.policy.security.BasicBruteForce;
import core.policy.security.SecurityPolicy;
import core.queries.MockDB;
import core.queries.QueryLayer;

class SecurityPolicyTest {

	@Test
	void testBasicBruteForce() throws IPCreationFailed {
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
		SecurityPolicy bruteForce = new BasicBruteForce(10,13);
		
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

}
