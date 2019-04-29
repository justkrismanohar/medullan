package core.policy.security;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginRequest;

public class ORSecurityPolicy extends CompositeSecurityPolicy{
	
	@Override
	public boolean handleRequest(LoginRequest req) {
		for(SecurityPolicy s : policies)
			if(s.handleRequest(req))
				return true;
		return false;
	}
	
	public boolean equals(Object o) {
		return o instanceof ORSecurityPolicy && super.equals(o);
	}
}
