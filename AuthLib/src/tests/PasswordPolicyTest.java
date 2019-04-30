package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.policy.password.ANDCompositePasswordPolicy;
import core.policy.password.CharHasSometingPasswordPolicyFactory;
import core.policy.password.ORCompositePasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.username.EmailFormatUsernamePolicy;;

public class PasswordPolicyTest {

	
	@Test
	public void testUppercaseExact() {
		String password = "ThisHasUpperCase";
		PasswordPolicy p = CharHasSometingPasswordPolicyFactory.upperCase(4);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNoUppercase() {
		String password = "no_upper_case";
		PasswordPolicy p = CharHasSometingPasswordPolicyFactory.upperCase(0);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testlowercaseExact() {
		String password = "ThisHasLowerCase";
		PasswordPolicy p = CharHasSometingPasswordPolicyFactory.lowerCase(12);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNolowercase() {
		String password = "NO_LOWER_CASE";
		
		PasswordPolicy p = CharHasSometingPasswordPolicyFactory.lowerCase(0);
		assertTrue(p.evaluatePassword(password));
	
	}
	
	@Test
	public void testLowerAndUpperExact() {
		ANDCompositePasswordPolicy c = new ANDCompositePasswordPolicy();
		c.add(CharHasSometingPasswordPolicyFactory.upperCase(2));
		c.add(CharHasSometingPasswordPolicyFactory.lowerCase(2));
		String password = "TiHs";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
		
	}
	
	@Test
	public void testAtLeastNWithAND() {
		ANDCompositePasswordPolicy c = new ANDCompositePasswordPolicy();
		c.add(CharHasSometingPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasSometingPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(1));
		
		String password = "ThisP1sP2";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
	}
	
	@Test
	public void testAtLeastNWithOR() {
		ORCompositePasswordPolicy c = new ORCompositePasswordPolicy();
		c.add(CharHasSometingPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasSometingPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasSometingPasswordPolicyFactory.atLeastDigit(1));
		
		String password = "ThPd";
		assertTrue(c.evaluatePassword(password));
		
		password = "Thsedee";
		assertTrue(c.evaluatePassword(password));
		
		password = "1Pw";
		assertTrue(c.evaluatePassword(password));
		
		password = "Tp";
		assertFalse(c.evaluatePassword(password));
	}
	
	@Test
	public void testEmailPattern(){
		String emailPass = "justkrismanohar@gmail.com";
		String emailFail = "@gmail.com";
		
		EmailFormatUsernamePolicy emailPolicy = new EmailFormatUsernamePolicy();
		assertTrue(emailPolicy.evaluateUsername(emailPass));
		assertFalse(emailPolicy.evaluateUsername(emailFail));		
	}

}
