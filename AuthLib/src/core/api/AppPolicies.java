package core.api;

import core.policy.login.LoginPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.security.SecurityPolicy;
import core.policy.session.SessionPolicy;
import core.policy.username.UsernamePolicy;

public class AppPolicies {
	public SessionPolicy timeoutSession;
	
	public SecurityPolicy preLoginPolicies;

	public LoginPolicy basicVerification;
	public PasswordPolicy passwordPolicy;
	public UsernamePolicy usernamePolicy;
		
	public SecurityPolicy postLoginPolicies;
	
}
