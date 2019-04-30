package core.api;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;
import core.policy.login.BasicVerification;
import core.policy.login.SessionPolicy;
import core.policy.login.TimeoutSession;
import core.policy.login.VerificationPolicy;
import core.policy.password.ANDCompositePasswordPolicy;
import core.policy.password.Has;
import core.policy.password.PasswordPolicy;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.BasicBruteForce;
import core.policy.security.LockoutPolicy;
import core.policy.security.NConsecutiveFailedLogins;
import core.policy.security.SecurityPolicy;
import core.policy.security.UserAccountLockedPolicy;
import core.policy.username.EmailFormat;
import core.policy.username.UsernamePolicy;
import core.queries.QueryLayerFactory;

public class EndPoint {
	
	/**
	 * EndPoints are boolean for now, they can be refactored to return custom messages etc later.
	 * For now testing the core logic.
	 * 
	 */
		Policies appConfig;
		
		public EndPoint(String configFile) {
			XMLConfig config = new XMLConfig(configFile);
			appConfig = config.parsePolicies();
		}
		
		public EndPoint() {
			appConfig = new Policies();
			
			//should really load execute the following based on .xml config
			//set up security policies
			ANDSecurityPolicy preLoginPolicies = new ANDSecurityPolicy();
			ANDSecurityPolicy postLoginPolicies = new ANDSecurityPolicy();
			
			preLoginPolicies.add(new LockoutPolicy(20));
			preLoginPolicies.add(new UserAccountLockedPolicy());
			
			postLoginPolicies.add(new NConsecutiveFailedLogins(3));
			postLoginPolicies.add(new BasicBruteForce(10, 13));
			
			//set up password policies
			ANDCompositePasswordPolicy passwordPolicy = new ANDCompositePasswordPolicy();
			passwordPolicy.add(Has.atLeastUpperCase(2));
			passwordPolicy.add(Has.atLeastLowerCase(3));
			passwordPolicy.add(Has.atLeastDigit(1));
			
			//set up username policies 
			appConfig.usernamePolicy = new EmailFormat();
			
			//setup verification policies
			appConfig.basicVerification = new BasicVerification();
			
			//setup session policy
			appConfig.timeoutSession = new TimeoutSession(30);
			
			appConfig.preLoginPolicies = preLoginPolicies;
			appConfig.postLoginPolicies = postLoginPolicies;
			appConfig.passwordPolicy = passwordPolicy;
		}
		
		/**
		 * External APIs will use an EndPoint to access the library
		 */
		public boolean login(LoginRequest req) {
			//Check if the request is blocked
			boolean blocked = appConfig.preLoginPolicies.handleRequest(req);
			//Determine if the UN + PWD pair match
			boolean verified = appConfig.basicVerification.verifyLoginDetails(req);
			//Apply security post security policies
			boolean passedPostPolicies = appConfig.postLoginPolicies.handleRequest(req);
			//If pass checks return true
			return blocked && verified && passedPostPolicies;
		}
		
		public boolean register(LoginRequest req) {
			LoginDetails details = req.loginDetails;
			boolean passedPolicies = appConfig.usernamePolicy.evaluateUsername(details.userName) && 
									 appConfig.passwordPolicy.evaluatePassword(details.encryptedPassword);
			if(passedPolicies)
				QueryLayerFactory.getInstance().registerUser(req);
				
			return passedPolicies;
		}
		
		public boolean autheticateSession(String username) {
			return appConfig.timeoutSession.isValid(username);
		}
		
}
