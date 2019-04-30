package core.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.policy.password.CharHasWhateverPasswordPolicyFactory;
import core.policy.password.PasswordPolicy;

import java.util.HashMap;

import org.w3c.dom.Element;
 
public class PasswordPolicyFactory {
	
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
		
		return null;
		
	}

}