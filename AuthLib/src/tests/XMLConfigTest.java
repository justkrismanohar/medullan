package tests;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.api.Policies;
import core.api.XMLConfig;
import core.policy.login.BasicVerification;
import core.policy.login.TimeoutSession;
import core.policy.password.ANDCompositePasswordPolicy;
import core.policy.password.CharHasSometingPasswordPolicyFactory;
import core.policy.password.ORCompositePasswordPolicy;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.BasicBruteForce;
import core.policy.security.LockoutPolicy;
import core.policy.security.NConsecutiveFailedLogins;
import core.policy.security.ORSecurityPolicy;
import core.policy.security.UserAccountLockedPolicy;
import core.policy.username.EmailFormat;

public class XMLConfigTest {

	@Test
	public void testSimpleConfigFile() {
		//load app Polices to compare with 
		Policies appConfig = setupAppPolicies();
		XMLConfig xmlFile = new XMLConfig("config.xml");
		Policies fileConfig = xmlFile.parsePolicies();
		assertTrue(appConfig.timeoutSession.equals(fileConfig.timeoutSession));
		assertTrue(appConfig.basicVerification.equals(fileConfig.basicVerification));
		assertTrue(appConfig.passwordPolicy.equals(fileConfig.passwordPolicy));
		
		assertTrue(appConfig.postLoginPolicies.equals(fileConfig.postLoginPolicies));
		assertTrue(appConfig.preLoginPolicies.equals(fileConfig.preLoginPolicies));
	}

	private Policies setupAppPolicies() {
		Policies appConfig = new Policies();
		
		ANDSecurityPolicy preLoginPolicies = new ANDSecurityPolicy();
		ANDSecurityPolicy postLoginPolicies = new ANDSecurityPolicy();
		
		preLoginPolicies.add(new LockoutPolicy(20));
		preLoginPolicies.add(new UserAccountLockedPolicy());
		
		postLoginPolicies.add(new NConsecutiveFailedLogins(3));
		postLoginPolicies.add(new BasicBruteForce(10, 13));
		
		//set up password policies
		ANDCompositePasswordPolicy passwordPolicy = new ANDCompositePasswordPolicy();
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastUpperCase(2));
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastLowerCase(3));
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(1));
		
		//set up username policies 
		appConfig.usernamePolicy = new EmailFormat();
		
		//setup verification policies
		appConfig.basicVerification = new BasicVerification();
		
		//setup session policy
		appConfig.timeoutSession = new TimeoutSession(30);
		
		appConfig.preLoginPolicies = preLoginPolicies;
		appConfig.postLoginPolicies = postLoginPolicies;
		appConfig.passwordPolicy = passwordPolicy;
		
		return appConfig;
	}
	
	@Test
	public void testNestedConfigFile() {
		//load app Polices to compare with 
		Policies appConfig = setupNestedAppPolicies();
		XMLConfig xmlFile = new XMLConfig("configNested.xml");
		Policies fileConfig = xmlFile.parsePolicies();
		assertTrue(appConfig.timeoutSession.equals(fileConfig.timeoutSession));
		assertTrue(appConfig.basicVerification.equals(fileConfig.basicVerification));
		assertTrue(appConfig.passwordPolicy.equals(fileConfig.passwordPolicy));
		
		assertTrue(appConfig.postLoginPolicies.equals(fileConfig.postLoginPolicies));
		assertTrue(appConfig.preLoginPolicies.equals(fileConfig.preLoginPolicies));
	}
	
	private Policies setupNestedAppPolicies() {
		Policies appConfig = new Policies();
		
		ANDSecurityPolicy preLoginPolicies = new ANDSecurityPolicy();
		ANDSecurityPolicy postLoginPolicies = new ANDSecurityPolicy();
		
		preLoginPolicies.add(new LockoutPolicy(20));
		preLoginPolicies.add(new UserAccountLockedPolicy());
		
		postLoginPolicies.add(new NConsecutiveFailedLogins(3));
		postLoginPolicies.add(new BasicBruteForce(10, 13));
		ORSecurityPolicy postLoginPoliciesOR = new ORSecurityPolicy();
		postLoginPoliciesOR.add(new NConsecutiveFailedLogins(2));
		postLoginPoliciesOR.add(new BasicBruteForce(7, 9));
		postLoginPolicies.add(postLoginPoliciesOR);
		
		
		//set up password policies
		ANDCompositePasswordPolicy passwordPolicy = new ANDCompositePasswordPolicy();
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastUpperCase(2));
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastLowerCase(3));
		passwordPolicy.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(1));
		ORCompositePasswordPolicy passwordPolicyOR = new ORCompositePasswordPolicy();
		passwordPolicyOR.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(10));
		passwordPolicy.add(passwordPolicyOR);
		
		
		//set up username policies 
		appConfig.usernamePolicy = new EmailFormat();
		
		//setup verification policies
		appConfig.basicVerification = new BasicVerification();
		
		//setup session policy
		appConfig.timeoutSession = new TimeoutSession(30);
		
		appConfig.preLoginPolicies = preLoginPolicies;
		appConfig.postLoginPolicies = postLoginPolicies;
		appConfig.passwordPolicy = passwordPolicy;
		
		return appConfig;
	}

}
