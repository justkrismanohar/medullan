package core.policy.security;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

import org.w3c.dom.Element;
 
public class SecurityPolicyFactory {
	
	public static SecurityPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Lockout")) {
			int duration = Integer.parseInt(attr.get("duration"));
			return new LockoutPolicy(duration);
		}
		
		if(name.equals("UserAccountLocked")) {
			return new UserAccountLockedPolicy();
		}
		
		if(name.equals("ConsecutiveFailed")) {
			int max = Integer.parseInt(attr.get("max"));
			return new NConsecutiveFailedLogins(max);
		}
	
		if(name.equals("BasicBruteForce")) {
			int span = Integer.parseInt(attr.get("span"));
			int threshold = Integer.parseInt(attr.get("threshold"));
			return new BasicBruteForce(span, threshold);
		}
		
		return null;
		
	}

}
