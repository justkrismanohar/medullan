package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.policy.password.ANDCompositePasswordPolicy;
import core.policy.password.Has;
import core.policy.password.ORCompositePasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.username.EmailFormat;;

public class PasswordPolicyTest {

	
	@Test
	public void testUppercaseExact() {
		String password = "ThisHasUpperCase";
		PasswordPolicy p = Has.upperCase(4);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNoUppercase() {
		String password = "no_upper_case";
		PasswordPolicy p = Has.upperCase(0);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testlowercaseExact() {
		String password = "ThisHasLowerCase";
		PasswordPolicy p = Has.lowerCase(12);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	public void testNolowercase() {
		String password = "NO_LOWER_CASE";
		
		PasswordPolicy p = Has.lowerCase(0);
		assertTrue(p.evaluatePassword(password));
	
	}
	
	@Test
	public void testLowerAndUpperExact() {
		ANDCompositePasswordPolicy c = new ANDCompositePasswordPolicy();
		c.add(Has.upperCase(2));
		c.add(Has.lowerCase(2));
		String password = "TiHs";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
		
	}
	
	@Test
	public void testAtLeastNWithAND() {
		ANDCompositePasswordPolicy c = new ANDCompositePasswordPolicy();
		c.add(Has.atLeastUpperCase(2));
		c.add(Has.atLeastLowerCase(3));
		c.add(Has.atLeastDigit(1));
		
		String password = "ThisP1sP2";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
	}
	
	@Test
	public void testAtLeastNWithOR() {
		ORCompositePasswordPolicy c = new ORCompositePasswordPolicy();
		c.add(Has.atLeastUpperCase(2));
		c.add(Has.atLeastLowerCase(3));
		c.add(Has.atLeastDigit(1));
		
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
		
		EmailFormat emailPolicy = new EmailFormat();
		assertTrue(emailPolicy.evaluateUsername(emailPass));
		assertFalse(emailPolicy.evaluateUsername(emailFail));		
	}

}
