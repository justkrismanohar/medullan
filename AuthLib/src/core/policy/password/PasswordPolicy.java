package core.policy.password;

public interface PasswordPolicy {
	public boolean evaluatePassword(String password);
}
