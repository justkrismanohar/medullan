package core.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.models.LoginRequest;
import core.policy.login.LoginPolicy;
import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.PasswordPolicy;

import java.util.HashMap;

import org.w3c.dom.Element;
 
public class PasswordPolicyFactory extends Factory{
	
	public static PasswordPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("UpperCase")) {
			String type = attr.get("type");
			int n = Integer.parseInt(attr.get("n"));			
			if(type.equals("atleast"))
				return CharHasWhateverPasswordPolicyFactory.atLeastUpperCase(n);
		}
		
		if(name.equals("LowerCase")) {
			String type = attr.get("type");
			int n = Integer.parseInt(attr.get("n"));			
			if(type.equals("atleast"))
				return CharHasWhateverPasswordPolicyFactory.atLeastLowerCase(n);
		}
	
		if(name.equals("Digit")) {
			String type = attr.get("type");
			int n = Integer.parseInt(attr.get("n"));			
			if(type.equals("atleast"))
				return CharHasWhateverPasswordPolicyFactory.atLeastDigit(n);
		}
		
		String err ="Invalid PasswordPolicy "+name+" requested.";
		log.error(err);
		
		return new PasswordPolicy() {
			@Override
			public boolean evaluatePassword(String password) {
				log.info("Creating NULL PasswordPolicy");
				return false;
			}
		};
		
	}

}
