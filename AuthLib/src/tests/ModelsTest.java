package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.models.LoginRequest;
import core.models.wrappers.CookieWrapper;
import core.models.wrappers.IPWrapper;
import core.models.wrappers.UserAgentWrapper;
import core.models.wrappers.IPWrapper.IPCreationFailed;

public class ModelsTest {

	@Test
	public void testIPCreation() throws IPCreationFailed {
		IPWrapper other = new IPWrapper("127.0.0.1");
		IPWrapper address = new IPWrapper("127.0.0.1");
		assertTrue(address.equals(other));
	}

	
	@Test
	public void testCookieCreation() {
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		CookieWrapper c2 = new CookieWrapper("c1","val1");
		assertTrue(c1.equals(c2));
		CookieWrapper c3 = new CookieWrapper("c1","val2");
		assertFalse(c1.equals(c3));
	}
	
	@Test
	public void testUserAgentCreation() {
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		UserAgentWrapper a2 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		assertTrue(a1.equals(a2));
		UserAgentWrapper a3 = new UserAgentWrapper("Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.3");
		assertFalse(a1.equals(a3));
	}
	
	@Test
	public void testLoginRequest() throws IPCreationFailed {
		
		IPWrapper ip1 = new IPWrapper("127.0.0.1");
		CookieWrapper c1 = new CookieWrapper("c1","val1");
		UserAgentWrapper a1 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		LoginRequest r1 = new LoginRequest(ip1,c1,a1);
		
		IPWrapper ip2 = new IPWrapper("127.0.0.1");
		CookieWrapper c2 = new CookieWrapper("c1","val1");
		UserAgentWrapper a2 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		LoginRequest r2 = new LoginRequest(ip2,c2,a2);
		
		assertTrue(r1.equals(r2));
		
		IPWrapper ip3 = new IPWrapper("192.168.1.9");
		CookieWrapper c3 = new CookieWrapper("c1","val1");
		UserAgentWrapper a3 = new UserAgentWrapper("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
		LoginRequest r3 = new LoginRequest(ip3,c3,a3);
		
		assertFalse(r1.equals(r3));
		
	}
}
