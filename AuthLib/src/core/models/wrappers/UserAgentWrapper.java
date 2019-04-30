package core.models.wrappers;

public class UserAgentWrapper {
	private String someAgent;
	
	public UserAgentWrapper(String someAgent) {
		this.someAgent = someAgent;
	}
	
	public boolean equals(UserAgentWrapper other) {
		return this.someAgent.equals(other.someAgent);
	}
}
