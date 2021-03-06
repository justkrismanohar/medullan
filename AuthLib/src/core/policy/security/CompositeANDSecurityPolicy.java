package core.policy.security;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginRequest;
import core.policy.password.CompositeANDPasswordPolicy;

public class CompositeANDSecurityPolicy extends CompositeSecurityPolicy{
	
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
		return o instanceof CompositeANDSecurityPolicy && super.equals(o);
	}
	
}
