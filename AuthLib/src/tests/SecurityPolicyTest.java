package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.models.LoginDetails;
import core.models.LoginRequest;

import core.policy.security.CompositeANDSecurityPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.CompositeORSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.queries.MockDB;
import core.queries.QueryLayer;
import core.utils.UnitTestHelper;

public class SecurityPolicyTest {

	@Test
	public void BasicBruteForceLockout_NewUserThen23FailedThenTimeoutAfter20min_PassFailPass() throws Exception {
		//Set up mockDB specifics for this test 
		MockDB q = UnitTestHelper.getMockDBForBruteForceTest();
		MockDB.setMockDB(q);//quick and dirty something just for testing
		
		LoginRequest signature = UnitTestHelper.getSignature(1);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		assertThat("New signature. Should not be in signature block list",q.isSignaturetInBlockList(signature),is(false));
	
		/**
		 * Due to the MockDB config, first handleRequest(...) should fail and activate the policy to block request signature
		 */
		bruteForce.handleRequest(signature);
		assertThat("Threhsold is 20 getNumFailedByRequestInLastXMins returns 23. Signature should be in blocked list.",q.isSignaturetInBlockList(signature),is(true));
		 
		/**
		 * Due to the MockDB config, second handleRequest(...) simulates 20 mins had passed. This should pass security check.
		 * That is the block request action should not be activated
		 */
		q.resetBlockSignatures();//simulate 20 mins passed
		bruteForce.handleRequest(signature);
		assertThat("Threhsold is 20 getNumFailedByRequestInLastXMins returns 0. Signature should NOT be in blocked list.",q.isSignaturetInBlockList(signature),is(false));
	}

	@Test
	public void ConsecutiveFailedLoginsBlocksUser_0Then3ConsecutiveFailedLogin_isBlockedThenIsNotBlocked() throws Exception {
		//Set up mockDB specifics for this test 
		MockDB q = UnitTestHelper.getMockDBForConsecutiveFailedTest();
		MockDB.setMockDB(q);//quick and dirty something just for testing
		//set up a request
		String userName = "user1";
		LoginRequest signature = UnitTestHelper.getSignatureWithUsernameAndPassword(1, userName, "PAss1");
		
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		
		q.registerUser(signature);
	
		/**
		 * Due to the MockDB config, first handleRequest(...) should pass and NOT activate the policy to block user
		 */
		cFailed.handleRequest(signature);
		assertThat("Threshold is 3 getNumFailedConnsecutiveByUser returns 0. username should not be in blocked list.",q.isUserNameLocked(userName),is(false));

		/**
		 * Due to the MockDB config, second handleRequest(...) simulates 3 consecutive failed logins. This should FAIL security check.
		 * That is the block request action should not be activated
		 */
		cFailed.handleRequest(signature);
		assertThat("Threshold is 3 getNumFailedConnsecutiveByUser returns 3. username should be in blocked list.",q.isUserNameLocked(userName),is(true));
	}
	
	@Test
	public void CompositeORSecurityPolicy_2CompoentsVariousTruthValues_FailPassPass() throws Exception {
		//Set up mockDB specifics for this test 
		MockDB q = UnitTestHelper.getMockDBForCompositeORSecurityTest();
		
		//set up a request
		LoginRequest req = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user1", "pass1");
		
		CompositeORSecurityPolicy or = UnitTestHelper.get3ConsecutiveFailedOR13BruteForceLockoutFor10();
		
		//Fail OR Fail is Fail
		assertThat("Both componets fail. CompositeORPolicy should fail.",or.handleRequest(req),is(false));	
		//Fail OR Pass is Pass
		assertThat("One componet passes. CompositeORPolicy should pass.",or.handleRequest(req),is(true));
		// Pass OR Pass is Pass
		assertThat("Both componets pass. CompositeORPolicy should pass.",or.handleRequest(req),is(true));
	}
	
	@Test
	public void CompositeANDSecurityPolicy_2ComponetsVariousTruthValues_PassFailFail() throws Exception {
		//Set up mockDB specifics for this test 
		MockDB q =UnitTestHelper.getMockDBForCompositeANDSecurityPolicy();
		
		//set up a request
		LoginRequest req = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user1", "pass1");
				
		CompositeANDSecurityPolicy and = UnitTestHelper.get3ConsecutiveFailedAND13BruteForceLockoutFor10();
		
		//Pass AND Pass is Pass	
		assertThat("Both components pass. CompositeANDPolicy should pass.",and.handleRequest(req),is(true));	
		//Pass AND Fail is Fail
		assertThat("One component fails. CompositeANDPolicy should fail.",and.handleRequest(req),is(false));	
		// Fail AND Fail is Fail
		assertThat("Both components fail. CompositeANDPolicy should fail.",and.handleRequest(req),is(false));	
	}
	
	@Test
	public void LockoutSecurityPolicyFLow_NewUserLockedUser100MinsAfterLockedUser_PassFailPass() throws Exception {
		
		MockDB q = UnitTestHelper.getMockDBInstance();
		
		//set up a request
		LoginRequest req = UnitTestHelper.getSignatureWithUsernameAndPassword(1,"user1", "pass1");
					
		LockoutSecurityPolicy lp = new LockoutSecurityPolicy(20);
		
		assertThat("New user. LockoutSecurityPolicy should pass.",lp.handleRequest(req),is(true));
		
		q.blockSignature(req);//Say something caused it to be locked out
		assertThat("Simulated blocked signature. LockoutSecurityPolicy should fail.",lp.handleRequest(req),is(false));
		
		req.dateTime = req.dateTime.minus(100,ChronoUnit.MINUTES);//pretend  100 mins passed
		assertThat("100 mins after signature was blocked. LockoutSecurityPolicy should pass.",lp.handleRequest(req),is(true));
		assertThat("100 mins after signature was blocked. Signature should not be in blocked list.",q.isSignaturetInBlockList(req),is(false));
	}
			
}
