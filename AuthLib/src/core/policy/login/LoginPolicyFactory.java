package core.policy.login;

import java.util.HashMap;


public class LoginPolicyFactory {
	public static LoginPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Verification")) {
			String type = attr.get("type");
			if(type.equals("basic"))
				return new BasicLoginPolicy();
		}
		
		return null;
	}
}
