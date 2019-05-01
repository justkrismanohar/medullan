package core.models.wrappers;

public class UserAgentWrapper {
	private String someAgent;
	
	public UserAgentWrapper(String someAgent) {
		this.someAgent = (someAgent == null)? "":someAgent;
		
	}
	
	public boolean equals(UserAgentWrapper other) {
		return this.someAgent.equals(other.someAgent);
	}
	
	public String toString() {
		return someAgent;
	}
}
