package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class UserAccountLockedPolicy implements SecurityPolicy{

	@Override
	public boolean handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		return !q.isUserNameLocked(req.loginDetails.userName);
	}
	
	public boolean equals(Object o) {
		return o instanceof UserAccountLockedPolicy;
	}
}
