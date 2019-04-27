package core.policy.security;

import core.models.LoginRequest;

public interface SecurityPolicy {
	/*
	 * This implements security checks and the associated actions.
	 * The actions and checks can be decoupled later to make them 
	 * configurable via xml or something...
	 */
	public void handleRequest(LoginRequest req);
}
