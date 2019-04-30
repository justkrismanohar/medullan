package core.utils;

import java.util.HashMap;

import core.models.LoginRequest;
import core.policy.username.EmailFormatUsernamePolicy;
import core.policy.username.UsernamePolicy;

public class UsernamePolicyFactory {
	
	public static UsernamePolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Username")) {
			String type = attr.get("type");
			if(type.equals("email"))
				return new EmailFormatUsernamePolicy();
		}
	
		return new UsernamePolicy() {
			@Override
			public boolean evaluateUsername(LoginRequest req) {
				
				return false;
			}
			
		};	
	}

}
