package core.policy.session;

import core.models.LoginRequest;
import core.models.Session;

public interface SessionPolicy {
	public boolean isValid(LoginRequest req);
}
