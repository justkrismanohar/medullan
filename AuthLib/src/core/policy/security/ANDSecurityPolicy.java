package core.policy.security;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginRequest;
import core.policy.password.ANDPasswordPolicy;

public class ANDSecurityPolicy implements SecurityPolicy{
	
	private List<SecurityPolicy> policies;
	
	
	public ANDSecurityPolicy() {
		policies = new ArrayList<SecurityPolicy>();
	}
	
	public void add(SecurityPolicy p) {
		this.policies.add(p);
	}

	/*
	 * Execute all policies to update stats but the RESULT is logical AND
	 * 
	 */
	@Override
	public boolean handleRequest(LoginRequest req) {
		boolean result = true;
		for(SecurityPolicy s : policies)
			if(!s.handleRequest(req))
				result = false;
		return result;
	}
	
	public boolean equals(Object o) {
		if(o instanceof ANDSecurityPolicy) {
			ANDSecurityPolicy other = (ANDSecurityPolicy)o;
			int len = policies.size();
			for(int i =0; i < len; i++) {
				if(!policies.get(i).equals(other.policies.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}
	
}
