package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.policy.Has;
import core.policy.PasswordPolicy;;

class PasswordPolicyTest {

	
	@Test
	void testUppercase() {
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
	void testlowercase() {
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

}
