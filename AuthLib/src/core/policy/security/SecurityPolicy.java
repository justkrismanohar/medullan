package core.policy.security;

import core.models.LoginRequest;

public interface SecurityPolicy {
	public void handleRequest(LoginRequest req);
}
