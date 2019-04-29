package core.policy.password;

public abstract class HasCheck implements PasswordPolicy{
	protected int amt;
	public static enum Type {UPPER_CASE,LOWER_CASE,DIGIT,OTHER};
	
	private Type type = Type.OTHER;
	
	
	public HasCheck(Type t) {
		this.amt = 0;
		type =t;
	}
	public HasCheck() {
		this.amt = 0;
	}
	
	public HasCheck(int amt) {
		this.amt = amt;
		if(this.amt <0 ) {
			this.amt = 0;
		}
	}
	
	public HasCheck(int amt,Type t) {
		this.amt = amt;
		this.type = t;
		if(this.amt <0 ) {
			this.amt = 0;
		}
	}
	
	public abstract boolean charIsWhatever(char ch);
	
	public boolean hasRequiredAmt(int count) {
		return count == amt;
	}
	
	/**
	 * Verifies if password has a certain amt (determined by hasRequiredAmt) of letters that pass 
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
		
		return hasRequiredAmt(count);
	}
	
	public boolean equals(Object other) {
		if(other instanceof HasCheck) {
			HasCheck t = (HasCheck)other;
			return t.amt == this.amt && t.type == this.type;
		}
		return false;
	}

}
