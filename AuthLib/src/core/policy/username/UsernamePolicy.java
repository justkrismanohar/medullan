package core.policy.username;

import core.models.LoginRequest;

public interface UsernamePolicy {
	public boolean evaluateUsername(LoginRequest req);
}
