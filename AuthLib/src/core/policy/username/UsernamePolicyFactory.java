package core.policy.username;

import java.util.HashMap;

public class UsernamePolicyFactory {
	
	public static UsernamePolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Username")) {
			String type = attr.get("type");
			if(type.equals("email"))
				return new EmailFormatUsernamePolicy();
		}
	
		return null;	
	}

}
