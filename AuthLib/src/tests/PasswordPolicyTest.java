package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.policy.password.AND;
import core.policy.password.Has;
import core.policy.password.OR;
import core.policy.password.PasswordPolicy;;

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
		AND c = new AND();
		c.add(Has.upperCase(2));
		c.add(Has.lowerCase(2));
		String password = "TiHs";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
		
	}
	
	@Test
	void testAtLeastNWithAND() {
		AND c = new AND();
		c.add(Has.atLeastUpperCase(2));
		c.add(Has.atLeastLowerCase(3));
		c.add(Has.atLeastDigit(1));
		
		String password = "ThisP1sP2";
		assertTrue(c.evaluatePassword(password));
		
		password = "This";
		assertFalse(c.evaluatePassword(password));
	}
	
	@Test
	void testAtLeastNWithOR() {
		OR c = new OR();
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

}
