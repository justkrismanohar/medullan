package core.api;

import core.policy.login.SessionPolicy;
import core.policy.login.VerificationPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.security.SecurityPolicy;
import core.policy.username.UsernamePolicy;

public class Policies {
	public SessionPolicy timeoutSession;
	
	public SecurityPolicy preLoginPolicies;

	public VerificationPolicy basicVerification;
	public PasswordPolicy passwordPolicy;
	public UsernamePolicy usernamePolicy;
		
	public SecurityPolicy postLoginPolicies;
	
}
