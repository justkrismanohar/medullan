package core.utils;

import java.util.HashMap;

import core.models.LoginRequest;
import core.policy.username.EmailFormatUsernamePolicy;
import core.policy.username.UsernamePolicy;

public class UsernamePolicyFactory extends Factory {
	
	public static UsernamePolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Username")) {
			String type = attr.get("type");
			if(type.equals("email"))
				return new EmailFormatUsernamePolicy();
		}
	
		String err ="Invalid SessionPolicy "+name+" requested.";
		log.error(err);
		
		return new UsernamePolicy() {
			@Override
			public boolean evaluateUsername(LoginRequest req) {
				log.info("Creating NULL SessionPolicy");
				return false;
			}
		};	
	}

}
