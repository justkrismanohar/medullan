package core.policy.login;

import core.models.LoginRequest;

public interface LoginPolicy {
	public boolean verifyLoginDetails(LoginRequest req);
}
