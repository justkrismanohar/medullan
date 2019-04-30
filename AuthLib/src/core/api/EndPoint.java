package core.api;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;
import core.policy.login.BasicLoginPolicy;
import core.policy.login.LoginPolicy;
import core.policy.password.ANDCompositePasswordPolicy;
import core.policy.password.CharHasSometingPasswordPolicyFactory;
import core.policy.password.PasswordPolicy;
import core.policy.security.ANDCompositeSecurityPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.policy.security.UserAccountLockedSecurityPolicy;
import core.policy.session.SessionPolicy;
import core.policy.session.TimeoutSessionSessionPolicy;
import core.policy.username.EmailFormatUsernamePolicy;
import core.policy.username.UsernamePolicy;
import core.queries.QueryLayerFactory;

public class EndPoint {
	
	/**
	 * EndPoints are boolean for now, they can be refactored to return custom messages etc later.
	 * For now testing the core logic.
	 * 
	 */
		AppPolicies appConfig;
		
		public EndPoint(String configFile) {
			XMLConfigParser config = new XMLConfigParser(configFile);
			appConfig = config.parsePolicies();
		}
		
		public EndPoint() {
			appConfig = new AppPolicies();
			
			//should really load execute the following based on .xml config
			//set up security policies
			ANDCompositeSecurityPolicy preLoginPolicies = new ANDCompositeSecurityPolicy();
			ANDCompositeSecurityPolicy postLoginPolicies = new ANDCompositeSecurityPolicy();
			
			preLoginPolicies.add(new LockoutSecurityPolicy(20));
			preLoginPolicies.add(new UserAccountLockedSecurityPolicy());
			
			postLoginPolicies.add(new NConsecutiveFailedLoginsSecurityPolicy(3));
			postLoginPolicies.add(new BasicBruteForceSecurityPolicy(10, 13));
			
			//set up password policies
			ANDCompositePasswordPolicy passwordPolicy = new ANDCompositePasswordPolicy();
			passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastUpperCase(2));
			passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastLowerCase(3));
			passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(1));
			
			//set up username policies 
			appConfig.usernamePolicy = new EmailFormatUsernamePolicy();
			
			//setup verification policies
			appConfig.basicVerification = new BasicLoginPolicy();
			
			//setup session policy
			appConfig.timeoutSession = new TimeoutSessionSessionPolicy(30);
			
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
