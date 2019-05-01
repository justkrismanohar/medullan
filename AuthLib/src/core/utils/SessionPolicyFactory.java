package core.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.models.LoginRequest;
import core.policy.session.SessionPolicy;
import core.policy.session.TimeoutSessionSessionPolicy;

import java.util.HashMap;

import org.w3c.dom.Element;

public class SessionPolicyFactory extends Factory{
	
	public static SessionPolicy getInstance(String name, HashMap<String,String> attr) {
		if(name.equals("Timeout")) {
			int max = Integer.parseInt(attr.get("max"));
			return new TimeoutSessionSessionPolicy(max);
		}
		
		String err ="Invalid SessionPolicy "+name+" requested.";
		log.error(err);
		
		return new SessionPolicy() {
			@Override
			public boolean isValid(LoginRequest req) {
				log.info("Creating NULL SessionPolicy");
				return false;
			}
		};
	}

}
