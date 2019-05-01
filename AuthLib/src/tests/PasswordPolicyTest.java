package tests;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;


import core.policy.password.CompositeANDPasswordPolicy;
import core.models.LoginRequest;
import core.models.wrappers.IPWrapper.IPCreationFailed;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.CompositeORPasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.username.EmailFormatUsernamePolicy;
import core.utils.LoginRequestFactory;
import core.utils.UnitTestHelper;;

public class PasswordPolicyTest {

	
	@Test
	public void CharHasWateverPasswordPolicyExactUppercase_4Uppercase_EvaluatesTrue() {
		String password = "ThisHasUpperCase";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.upperCase(4);
		assertThat("Has 4 uppercase. Should evaluate true",p.evaluatePassword(password),is(true));
	}
	
	@Test
	public void CharHasWhateverPasswordPolicyExtactUppercase_ZeroUppercase_EvaluatesTrue(){
		String password = "no_upper_case";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.upperCase(0);
		assertThat("Has zero uppercase. Should evaluate true",p.evaluatePassword(password),is(true));
	}
	
	@Test
	public void CharHasWhateverExactLowercase_Exactly12_EvaluatesTrue() {
		String password = "ThisHasLowerCase";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.lowerCase(12);
		assertThat("Has 12 lowercase. Should evaluate true",p.evaluatePassword(password),is(true));
	}
	
	@Test
	public void CharHasWhateverExtactLowercase_ZeroLowercase_EvaluetesTrue() {
		String password = "NO_LOWER_CASE";
		PasswordPolicy p = CharHasWhateverPasswordPolicyFactory.lowerCase(0);
		assertThat("All uppercase. PasswordPolicy that checks for 0 lowercase should pass.",p.evaluatePassword(password),is(true));
	}
	
	@Test
	public void CompositeANDPasswordPolicy_HasComponents_PassIfAllComponentsEvaluateTrue() {
		CompositeANDPasswordPolicy c = UnitTestHelper.getHasExactly2UpperandLowerCase();
		String password = "TiHs";
		assertThat("Password has 2 uppercase and 2 lowercase. CompositeANDPassword should pass.",c.evaluatePassword(password),is(true));
		
		password = "This";
		assertThat("Password has 3 lowercase. CompositeANDPassword should fail.",c.evaluatePassword(password),is(false));
	}
	
	@Test
	public void CompositeANDPasswordPolicy_AtLeastComponets_PassIfAllComponentsEvaluateTrue() {
		CompositeANDPasswordPolicy c = UnitTestHelper.getAtleast2UppercaseANDLowercaseAND1Digit();
		
		String password = "ThisP1sP2";
		assertThat("Password has atleast 2 uppercase, 2 lowercase, and 1 digit. CompositeANDPassword should pass.",c.evaluatePassword(password),is(true));
		
		password = "This";
		assertThat("Password does not have 1 digit. CompositeANDPassword should fail.",c.evaluatePassword(password),is(false));
		
	}
	
	@Test
	public void CompositeORPasswordPolicy_AtLeastComponents_FailsOnlyIfAllCompoentsEvaluateFalse() {
		
		CompositeORPasswordPolicy c = UnitTestHelper.getAtleast2UppercaseORLowercaseOR1Digit();
		
		String password = "ThPd";
		assertThat("Only 1st component is true. CompositeORPassword should pass.",c.evaluatePassword(password),is(true));
		
		password = "Thsedee";
		assertThat("Only 2nd component is true. CompositeORPassword should pass.",c.evaluatePassword(password),is(true));
		
		password = "1Pw";
		assertThat("Only 3rd component is true. CompositeORPassword should pass.",c.evaluatePassword(password),is(true));
		
		password = "Tp";
		assertThat("All components are false. CompositeORPassword should fail.",c.evaluatePassword(password),is(false));
	}
	
	@Test
	public void EmailFormatUsernamePolicyWith_ValidEmail_EvaluatesTrue() throws IPCreationFailed{
		LoginRequest emailPass = LoginRequestFactory.getBasicLoginRequestWithEmailAs("justkrismanohar@gmail.com");	
		EmailFormatUsernamePolicy emailPolicy = new EmailFormatUsernamePolicy();
		assertThat("Email is justkrismanohar@gamil.com. EmailFormatUsername should pass.", emailPolicy.evaluateUsername(emailPass),is(true));
	}
	
	@Test
	public void EmailFormatUsernamePolicyWith_ValidEmail_EvaluatesFalse() throws IPCreationFailed{
		LoginRequest emailFail = LoginRequestFactory.getBasicLoginRequestWithEmailAs("@gmail.com");
		EmailFormatUsernamePolicy emailPolicy = new EmailFormatUsernamePolicy();
		assertThat("Email is @gamil.com. EmailFormatUsername should fail.", emailPolicy.evaluateUsername(emailFail),is(false));		
	}

}
