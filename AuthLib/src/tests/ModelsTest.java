package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import core.models.LoginRequest;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.UserAgentWrapper;
import core.utils.UnitTestHelper;
import core.models.wrappers.IPWrapper.IPCreationFailed;

public class ModelsTest {

	@Test
	public void IPWrapperEquals_2InstancesSameAddress_Pass() throws IPCreationFailed {
		IPWrapper other = new IPWrapper("127.0.0.1");
		IPWrapper address = new IPWrapper("127.0.0.1");
		assertThat("IP address strings are the same. IPWrapper.equals should return true.",address.equals(other),is(true));
	}

	
	@Test
	public void CookieWrapperEquals_SameValuesDifferentValues_PassFail() {
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		CookieWrapper c2 = new CookieWrapper("c1","val1");
		assertThat("Cookies have the smae values. CookieWrapper.equals should return true.",c1.equals(c2),is(true));
		CookieWrapper c3 = new CookieWrapper("c1","val2");
		assertThat("Cookies have differnent values. CookieWrapper.equals should return false.",c1.equals(c3),is(false));
	}
	
	@Test
	public void UserAgentWrapperEquals_SameValuesDifferntValues_PassFail() {
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		UserAgentWrapper a2 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		assertThat("UserAgent strings are the same. UserAgentWrapper.equals should return true.",a1.equals(a2),is(true));
		UserAgentWrapper a3 = new UserAgentWrapper("Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.3");
		assertThat("UserAgent string are different. UserAgent.euqals shoudl return false.",a1.equals(a3),is(false));
	}
	
	@Test
	public void LoginRequestEquals_SameValuesDifferntValues_PassFail() throws Exception {
		
		LoginRequest r1 = UnitTestHelper.createSignature("127.0.0.1","c1","val1","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		LoginRequest r2 = UnitTestHelper.createSignature("127.0.0.1","c1","val1","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		
		assertThat("LoginRequests have the same value. LoginRequest.equals should return true.",r1.equals(r2),is(true));
		
		LoginRequest r3 = UnitTestHelper.createSignature("192.168.1.9","c1","val1","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");

		assertThat("LoginRequests have differnt ip adrresses. LoginRequest.equals should return false.",r1.equals(r3),is(false));		
	}
}
