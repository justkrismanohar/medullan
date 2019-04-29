package core.api;

import core.policy.login.SessionPolicy;
import core.policy.login.VerificationPolicy;
import core.policy.password.ANDPasswordPolicy;
import core.policy.password.EmailFormat;
import core.policy.password.PasswordPolicy;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.SecurityPolicy;

public class Policies {
	public SessionPolicy timeoutSession;
	
	public SecurityPolicy preLoginPolicies;

	public VerificationPolicy basicVerification;
	public PasswordPolicy passwordPolicy;
	public EmailFormat usernamePolicy;
		
	public SecurityPolicy postLoginPolicies;
	
}
