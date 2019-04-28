package core.policy.security;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginRequest;

public class ANDSecurityPolicy implements SecurityPolicy{
	
	private List<SecurityPolicy> policies;
	
	
	public ANDSecurityPolicy() {
		policies = new ArrayList<SecurityPolicy>();
	}
	
	public void add(SecurityPolicy p) {
		this.policies.add(p);
	}

	@Override
	public boolean handleRequest(LoginRequest req) {
		for(SecurityPolicy s : policies)
			if(!s.handleRequest(req))
				return false;
		return true;
		
	}
	
}
