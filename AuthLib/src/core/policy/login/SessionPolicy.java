package core.policy.login;

import core.models.Session;

public interface SessionPolicy {
	public boolean isValid(String username);
}
