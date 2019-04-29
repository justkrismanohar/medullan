package core.policy.security;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginRequest;

public class ORSecurityPolicy implements SecurityPolicy{
	
	private List<SecurityPolicy> policies;
	
	
	public ORSecurityPolicy() {
		policies = new ArrayList<SecurityPolicy>();
	}
	
	public void add(SecurityPolicy p) {
		this.policies.add(p);
	}

	@Override
	public boolean handleRequest(LoginRequest req) {
		for(SecurityPolicy s : policies)
			if(s.handleRequest(req))
				return true;
		return false;
	}
	
	public boolean equals(Object o) {
		if(o instanceof ORSecurityPolicy) {
			ORSecurityPolicy other = (ORSecurityPolicy)o;
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
