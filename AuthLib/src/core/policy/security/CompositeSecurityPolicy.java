package core.policy.security;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeSecurityPolicy implements SecurityPolicy {
	
	public static CompositeSecurityPolicy getInstanceOf(String type) {
		//These checks could be automated using java reflections and a file
		//or whatever to map String type to a class name
		//Not necessary right now for only AND and OR types
		if(type.equals("OR")) {
			return new ORSecurityPolicy();
		}
		
		//Default...maybe throw and error or something later...
		return new ANDSecurityPolicy();
	}
	
	protected List<SecurityPolicy> policies;
	
	public CompositeSecurityPolicy() {
		policies = new ArrayList<SecurityPolicy>();
	}
	
	public void add(SecurityPolicy p) {
		this.policies.add(p);
	}
	
	public boolean equals(Object o) {
		if(o instanceof CompositeSecurityPolicy) {
			CompositeSecurityPolicy other = (CompositeSecurityPolicy)o;
			int len = policies.size();
			if(len == other.policies.size()) {
				for(int i =0; i < len; i++) {
					if(!policies.get(i).equals(other.policies.get(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	

}
