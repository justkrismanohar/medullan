package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.policy.CompositePolicy;
import core.policy.Has;
import core.policy.PasswordPolicy;;

class PasswordPolicyTest {

	
	@Test
	void testUppercaseExact() {
		String password = "ThisHasUpperCase";
		PasswordPolicy p = Has.upperCase(4);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	void testNoUppercase() {
		String password = "no_upper_case";
		PasswordPolicy p = Has.upperCase(0);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	void testlowercaseExact() {
		String password = "ThisHasLowerCase";
		PasswordPolicy p = Has.lowerCase(12);
		assertTrue(p.evaluatePassword(password));
	}
	
	@Test
	void testNolowercase() {
		String password = "NO_LOWER_CASE";
		
		PasswordPolicy p = Has.lowerCase(0);
		assertTrue(p.evaluatePassword(password));
	
	}
	
	@Test
	void testLowerAndUpperExact() {
		CompositePolicy c = new CompositePolicy();
		c.add(Has.upperCase(2));
		c.add(Has.lowerCase(2));
		String password = "TiHs";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
		
	}
	
	@Test
	void testAtLeastN() {
		CompositePolicy c = new CompositePolicy();
		c.add(Has.atLeastUpperCase(2));
		c.add(Has.atLeastLowerCase(3));
		c.add(Has.atLeastDigit(1));
		
		String password = "ThisP1sP2";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
	}

}
