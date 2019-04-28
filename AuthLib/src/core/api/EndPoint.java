package core.api;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.policy.password.ANDPasswordPolicy;
import core.policy.password.EmailFormat;
import core.policy.password.Has;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.BasicBruteForce;
import core.policy.security.LockoutPolicy;
import core.policy.security.NConsecutiveFailedLogins;
import core.policy.security.UserAccountLockedPolicy;

public class EndPoint {
	
	/**
	 * EndPoints are boolean for now, they can be refactored to return custom messages etc later.
	 * For now testing the core logic.
	 * 
	 */
	
		private ANDSecurityPolicy blockPolicies;
		private ANDSecurityPolicy postLoginPolicies;
		
		private ANDPasswordPolicy passwordPolicy;
		private EmailFormat usernamePolicy;
		
		public EndPoint() {
			//should really load execute the following based on .xml config
			//set up security policies
			blockPolicies = new ANDSecurityPolicy();
			postLoginPolicies = new ANDSecurityPolicy();
			
			blockPolicies.add(new LockoutPolicy(20));
			blockPolicies.add(new UserAccountLockedPolicy());
			
			postLoginPolicies.add(new NConsecutiveFailedLogins(3));
			postLoginPolicies.add(new BasicBruteForce(10, 13));
			
			//set up password policies
			passwordPolicy = new ANDPasswordPolicy();
			passwordPolicy.add(Has.atLeastUpperCase(2));
			passwordPolicy.add(Has.atLeastLowerCase(3));
			passwordPolicy.add(Has.atLeastDigit(1));
			
			//set up username policies 
			usernamePolicy = new EmailFormat();
		}
		
		/**
		 * External APIs will use an EndPoint to access the library
		 */
		public boolean login(LoginRequest req) {
			//Check if the request is blocked
			boolean blocked = blockPolicies.handleRequest(req);
			//Determine if the UN + PWD pair match
			boolean verified = LoginDetails.verifyLoginDetails(req);
			//Apply security post security policies
			boolean passedPostPolicies = postLoginPolicies.handleRequest(req);
			//If pass checks return true
			return blocked && verified && passedPostPolicies;
		}
		
		public boolean register(LoginDetails details) {
			boolean passPolicies = usernamePolicy.evaluatePassword(details.userName) && passwordPolicy.evaluatePassword(details.encryptedPassword);
			
			return passPolicies;
		}
		
		
}
