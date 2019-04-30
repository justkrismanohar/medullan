package core.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.policy.security.BasicBruteForceSecurityPolicy;
import core.policy.security.LockoutSecurityPolicy;
import core.policy.security.NConsecutiveFailedLoginsSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.policy.security.UserAccountLockedSecurityPolicy;

import java.util.HashMap;

import org.w3c.dom.Element;
 
public class SecurityPolicyFactory {
	
	public static SecurityPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Lockout")) {
			int duration = Integer.parseInt(attr.get("duration"));
			return new LockoutSecurityPolicy(duration);
		}
		
		if(name.equals("UserAccountLocked")) {
			return new UserAccountLockedSecurityPolicy();
		}
		
		if(name.equals("ConsecutiveFailed")) {
			int max = Integer.parseInt(attr.get("max"));
			return new NConsecutiveFailedLoginsSecurityPolicy(max);
		}
	
		if(name.equals("BasicBruteForce")) {
			int span = Integer.parseInt(attr.get("span"));
			int threshold = Integer.parseInt(attr.get("threshold"));
			return new BasicBruteForceSecurityPolicy(span, threshold);
		}
		
		return null;
		
	}

}
