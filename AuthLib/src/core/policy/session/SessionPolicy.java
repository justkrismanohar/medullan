package core.policy.session;

import core.models.Session;

public interface SessionPolicy {
	public boolean isValid(String username);
}
