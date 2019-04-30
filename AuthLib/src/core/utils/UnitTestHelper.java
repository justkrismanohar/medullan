package core.utils;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.models.wrappers.UserAgentWrapper;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.CompositeANDPasswordPolicy;
import core.policy.password.CompositeORPasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.CompositeANDSecurityPolicy;
import core.policy.security.CompositeORSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.queries.MockDB;

public class UnitTestHelper {
	
	public static CompositeORPasswordPolicy getAtleast2UppercaseORLowercaseOR1Digit() {
		CompositeORPasswordPolicy c = new CompositeORPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		return c;
	}
	
	public static CompositeANDPasswordPolicy getAtleast2UppercaseANDLowercaseAND1Digit() {
		CompositeANDPasswordPolicy c = new CompositeANDPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		return c;
	}
	
	public static CompositeANDPasswordPolicy getHasExactly2UpperandLowerCase() {
		CompositeANDPasswordPolicy c = new CompositeANDPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.upperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.lowerCase(2));
		return c;
	}
	
	public static LoginRequest createSignature(String ip, String cookiename, String cookievalue, String useragent) throws IPCreationFailed {
		IPWrapper ip1 = new IPWrapper(ip);
		CookieWrapper c1 = new CookieWrapper(cookiename,cookievalue);
		UserAgentWrapper a1 = new UserAgentWrapper(useragent);
		LoginRequest signature = new LoginRequest(ip1,c1,a1);
		return signature;
	}
	
	public static LoginRequest getSignature(int id) throws Exception {
		if(id == 1) {
			return createSignature("127.0.0.1","c1","val1","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");		
		}
		
		throw new Exception("Requesting to make unknown signature.");
		
	}
	
	public static LoginRequest getSignatureWithUsernameAndPassword(int signatureId,String username,String password) throws Exception {
		//set up a request
		LoginRequest signature = getSignature(signatureId);
		signature.loginDetails.userName = username;
		signature.loginDetails.encryptedPassword = password;
		return signature;
	}
	
	public static MockDB getMockDBForBruteForceTest() {
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
		return q;
	}
	
	public static MockDB getMockDBForConsecutiveFailedTest() {
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
		
		return q;
	}
	
	public static MockDB getMockDBForCompositeORSecurityTest() {
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
		return q;
	}
	
	public static MockDB getMockDBForCompositeANDSecurityPolicy() {
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
		
		return q;
	}
	
	public static CompositeORSecurityPolicy get3ConsecutiveFailedOR13BruteForceLockoutFor10() {
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		CompositeORSecurityPolicy or = new CompositeORSecurityPolicy();
		or.add(bruteForce);//keep order to work with tick fucntion in mock DB
		or.add(cFailed);
		
		return or;
	}
	
	public static CompositeANDSecurityPolicy get3ConsecutiveFailedAND13BruteForceLockoutFor10() {
		SecurityPolicy cFailed = new NConsecutiveFailedLoginsSecurityPolicy(3);
		SecurityPolicy bruteForce = new BasicBruteForceSecurityPolicy(10,13);
		
		CompositeANDSecurityPolicy and = new CompositeANDSecurityPolicy();
		and.add(bruteForce);//keep order to work with tick function in mock DB
		and.add(cFailed);
		return and;
	}
	
	public static MockDB getMockDBInstance() {
		MockDB q = new MockDB();
		MockDB.setMockDB(q);//quick and dirty something just for testing
		return q;
	}
}
