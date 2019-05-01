package core.utils;

import java.util.HashMap;

import core.models.LoginRequest;
import core.policy.login.BasicLoginPolicy;
import core.policy.login.LoginPolicy;
import tests.RunAllUnitTests;



public class LoginPolicyFactory extends Factory{
	
	public static LoginPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Verification")) {
			String type = attr.get("type");
			if(type.equals("basic"))
				return new BasicLoginPolicy();
		}
		
		String err ="Invalid LoginPolicy "+name+" requested.";
		log.error(err);
		
		return new LoginPolicy() {
			@Override
			public boolean verifyLoginDetails(LoginRequest req) {
				log.info("Creating NULL LoginPolicy");
				return false;
			}
		};
		
	}
}
