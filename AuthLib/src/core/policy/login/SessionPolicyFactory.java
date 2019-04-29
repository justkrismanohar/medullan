package core.policy.login;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

import org.w3c.dom.Element;

public class SessionPolicyFactory {
	
	public static SessionPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Timeout")) {
			int max = Integer.parseInt(attr.get("max"));
			return new TimeoutSession(max);
		}
		return null;
	}

}
