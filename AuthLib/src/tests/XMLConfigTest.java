package tests;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.api.AppPolicies;
import core.api.XMLConfigParser;
import core.policy.login.BasicLoginPolicy;
import core.policy.password.CompositeANDPasswordPolicy;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.CompositeORPasswordPolicy;
import core.policy.security.CompositeANDSecurityPolicy;
import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.CompositeORSecurityPolicy;
import core.policy.security.UserAccountLockedSecurityPolicy;
import core.policy.session.TimeoutSessionSessionPolicy;
import core.policy.username.EmailFormatUsernamePolicy;

public class XMLConfigTest {

	@Test
	public void testSimpleConfigFile() {
		//load app Polices to compare with 
		AppPolicies appConfig = setupAppPolicies();
		XMLConfigParser xmlFile = new XMLConfigParser("config.xml");
		AppPolicies fileConfig = xmlFile.parsePolicies();
		assertTrue(appConfig.timeoutSession.equals(fileConfig.timeoutSession));
		assertTrue(appConfig.basicVerification.equals(fileConfig.basicVerification));
		assertTrue(appConfig.passwordPolicy.equals(fileConfig.passwordPolicy));
		
		assertTrue(appConfig.postLoginPolicies.equals(fileConfig.postLoginPolicies));
		assertTrue(appConfig.preLoginPolicies.equals(fileConfig.preLoginPolicies));
	}

	private AppPolicies setupAppPolicies() {
		AppPolicies appConfig = new AppPolicies();
		
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
	
	@Test
	public void testNestedConfigFile() {
		//load app Polices to compare with 
		AppPolicies appConfig = setupNestedAppPolicies();
		XMLConfigParser xmlFile = new XMLConfigParser("configNested.xml");
		AppPolicies fileConfig = xmlFile.parsePolicies();
		assertTrue(appConfig.timeoutSession.equals(fileConfig.timeoutSession));
		assertTrue(appConfig.basicVerification.equals(fileConfig.basicVerification));
		assertTrue(appConfig.passwordPolicy.equals(fileConfig.passwordPolicy));
		
		assertTrue(appConfig.postLoginPolicies.equals(fileConfig.postLoginPolicies));
		assertTrue(appConfig.preLoginPolicies.equals(fileConfig.preLoginPolicies));
	}
	
	private AppPolicies setupNestedAppPolicies() {
		AppPolicies appConfig = new AppPolicies();
		
		CompositeANDSecurityPolicy preLoginPolicies = new CompositeANDSecurityPolicy();
		CompositeANDSecurityPolicy postLoginPolicies = new CompositeANDSecurityPolicy();
		
		preLoginPolicies.add(new LockoutSecurityPolicy(20));
		preLoginPolicies.add(new UserAccountLockedSecurityPolicy());
		
		postLoginPolicies.add(new NConsecutiveFailedLoginsSecurityPolicy(3));
		postLoginPolicies.add(new BasicBruteForceSecurityPolicy(10, 13));
		CompositeORSecurityPolicy postLoginPoliciesOR = new CompositeORSecurityPolicy();
		postLoginPoliciesOR.add(new NConsecutiveFailedLoginsSecurityPolicy(2));
		postLoginPoliciesOR.add(new BasicBruteForceSecurityPolicy(7, 9));
		postLoginPolicies.add(postLoginPoliciesOR);
		
		
		//set up password policies
		CompositeANDPasswordPolicy passwordPolicy = new CompositeANDPasswordPolicy();
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		passwordPolicy.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		CompositeORPasswordPolicy passwordPolicyOR = new CompositeORPasswordPolicy();
		passwordPolicyOR.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(10));
		passwordPolicy.add(passwordPolicyOR);
		
		
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
