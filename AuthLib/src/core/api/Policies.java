package core.api;

import core.policy.login.SessionPolicy;
import core.policy.login.VerificationPolicy;
import core.policy.password.ANDPasswordPolicy;
import core.policy.password.EmailFormat;
import core.policy.security.ANDSecurityPolicy;

public class Policies {
	public SessionPolicy timeoutSession;
	
	public ANDSecurityPolicy preLoginPolicies;

	public VerificationPolicy basicVerification;
	public ANDPasswordPolicy passwordPolicy;
	public EmailFormat usernamePolicy;
		
	public ANDSecurityPolicy postLoginPolicies;
	
}
