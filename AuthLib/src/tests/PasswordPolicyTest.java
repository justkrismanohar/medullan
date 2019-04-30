package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.policy.password.CompositeANDPasswordPolicy;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.CompositeORPasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.username.EmailFormatUsernamePolicy;;

public class PasswordPolicyTest {

	
	@Test
	public void testUppercaseExact() {
		String password = "ThisHasUpperCase";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.upperCase(4);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNoUppercase() {
		String password = "no_upper_case";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.upperCase(0);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testlowercaseExact() {
		String password = "ThisHasLowerCase";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.lowerCase(12);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNolowercase() {
		String password = "NO_LOWER_CASE";
		
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.lowerCase(0);
		assertTrue(p.evaluatePassword(password));
	
	}
	
	@Test
	public void testLowerAndUpperExact() {
		CompositeANDPasswordPolicy c = new CompositeANDPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.upperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.lowerCase(2));
		String password = "TiHs";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
		
	}
	
	@Test
	public void testAtLeastNWithAND() {
		CompositeANDPasswordPolicy c = new CompositeANDPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		
		String password = "ThisP1sP2";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
	}
	
	@Test
	public void testAtLeastNWithOR() {
		CompositeORPasswordPolicy c = new CompositeORPasswordPolicy();
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(2));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(3));
		c.add(CharHasWhateverPasswordPolicyFactory.atLeastDigit(1));
		
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
