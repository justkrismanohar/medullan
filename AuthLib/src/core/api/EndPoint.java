package core.api;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.BasicBruteForce;
import core.policy.security.LockoutPolicy;
import core.policy.security.NConsecutiveFailedLogins;
import core.policy.security.UserAccountLockedPolicy;

public class EndPoint {
	
	
		private ANDSecurityPolicy blockPolicies;
		private ANDSecurityPolicy postLoginPolicies;
		
		public EndPoint() {
			
			blockPolicies = new ANDSecurityPolicy();
			postLoginPolicies = new ANDSecurityPolicy();
			//should really load execute the following based on .xml config
			blockPolicies.add(new LockoutPolicy(20));
			blockPolicies.add(new UserAccountLockedPolicy());
			
			postLoginPolicies.add(new NConsecutiveFailedLogins(3));
			postLoginPolicies.add(new BasicBruteForce(10, 13));
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
			boolean passPostPolicies = postLoginPolicies.handleRequest(req);
			//If pass checks return true
			return blocked && verified && passPostPolicies;
		}
		
		
}
