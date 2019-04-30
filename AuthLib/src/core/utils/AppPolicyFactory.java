package core.utils;

import core.api.AppPolicies;
import core.policy.login.BasicLoginPolicy;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.CompositeANDPasswordPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.CompositeANDSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.UserAccountLockedSecurityPolicy;
import core.policy.session.TimeoutSessionSessionPolicy;
import core.policy.username.EmailFormatUsernamePolicy;

public class AppPolicyFactory {
	
	public static AppPolicies getDefault() {
		AppPolicies appConfig = new AppPolicies();
		
		//set up security policies
		CompositeANDSecurityPolicy preLoginPolicies = new CompositeANDSecurityPolicy();
		CompositeANDSecurityPolicy postLoginPolicies = new CompositeANDSecurityPolicy();
		
		preLoginPolicies.add(new LockoutSecurityPolicy(20));
		preLoginPolicies.add(new UserAccountLockedSecurityPolicy());
		
		postLoginPolicies.add(new NConsecutiveFailedLoginsSecurityPolicy(3));
		postLoginPolicies.add(new BasicBruteForceSecurityPolicy(10, 13));
		
		//set up password policies
		CompositeANDPasswordPolicy passwordPolicy = new CompositeANDPasswordPolicy();
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		
		//set up username policies 
		appConfig.usernamePolicy = new EmailFormatUsernamePolicy();
		
		//setup verification policies
		appConfig.basicVerification = new BasicLoginPolicy();
		
		//setup session policy
		appConfig.timeoutSession = new TimeoutSessionSessionPolicy(30);
		
		appConfig.preLoginPolicies = preLoginPolicies;
		appConfig.postLoginPolicies = postLoginPolicies;
		appConfig.passwordPolicy = passwordPolicy;
		
		return appConfig;
	}
}
