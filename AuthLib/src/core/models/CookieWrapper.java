package core.models;

import javax.servlet.http.Cookie;

public class CookieWrapper {
	private Cookie cookie;
	
	public CookieWrapper(String name, String value) {
		this.cookie = new Cookie(name,value);
	}
	
	public boolean equals(CookieWrapper c) {
		return this.cookie.getName().equals(c.cookie.getName()) && 
			this.cookie.getValue().equals(c.cookie.getValue());
	}
	
}
