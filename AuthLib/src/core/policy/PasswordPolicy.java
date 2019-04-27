package core.policy;

public interface PasswordPolicy {
	public boolean evaluatePassword(String password);
}
