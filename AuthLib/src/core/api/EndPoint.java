package core.api;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;
import core.policy.login.BasicLoginPolicy;
import core.policy.login.LoginPolicy;
import core.policy.password.CompositeANDPasswordPolicy;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.PasswordPolicy;
import core.policy.security.CompositeANDSecurityPolicy;
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
import core.utils.AppPolicyFactory;

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
			appConfig = AppPolicyFactory.getDefault();
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
			boolean passedPolicies = appConfig.usernamePolicy.evaluateUsername(req) && 
									 appConfig.passwordPolicy.evaluatePassword(details.encryptedPassword);
			if(passedPolicies)
				QueryLayerFactory.getInstance().registerUser(req);
				
			return passedPolicies;
		}
		
		public boolean autheticateSession(String username) {
			return appConfig.timeoutSession.isValid(username);
		}
		
}
