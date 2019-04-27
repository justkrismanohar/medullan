package core.policy;

public abstract class HasAtLeastN implements PasswordPolicy{
	private int amt;
	
	
	
	public HasAtLeastN() {
		this.amt = 0;
	}
	
	public HasAtLeastN(int amt) {
		this.amt = amt;
		if(this.amt <0 ) {
			this.amt = 0;
		}
	}
	
	public abstract boolean charIsWhatever(char ch);
	
	/**
	 * Verifies if password has at least a certain amt of letters specified by 
	 * the charIsWhatever test.
	 * Default is 0
	 */
	@Override
	public boolean evaluatePassword(String password) {
		if(password == null) return false;
		
		int count = 0;
		for(int i = 0; i < password.length(); i ++) {
			if(charIsWhatever(password.charAt(i)))
				count++;
		}
		
		return count >= amt;
	}

}
