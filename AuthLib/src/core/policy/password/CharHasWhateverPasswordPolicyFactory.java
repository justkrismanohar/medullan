package core.policy.password;

public class CharHasWhateverPasswordPolicyFactory {
	public static PasswordPolicy lowerCase() {
		return new CharHasWhateverPasswordPolicy(CharHasWhateverPasswordPolicy.Type.LOWER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase() {
		return new CharHasWhateverPasswordPolicy(CharHasWhateverPasswordPolicy.Type.UPPER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit() {
		return new CharHasWhateverPasswordPolicy(CharHasWhateverPasswordPolicy.Type.DIGIT) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}
	
	public static PasswordPolicy lowerCase(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.LOWER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.UPPER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.DIGIT) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}

	public static PasswordPolicy atLeastLowerCase(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.LOWER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
			
			@Override
			public boolean hasRequiredAmt(int count) {
				return count >= this.amt;
			}
		};
	}
	
	public static PasswordPolicy atLeastUpperCase(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.UPPER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
			
			@Override
			public boolean hasRequiredAmt(int count) {
				return count >= this.amt;
			}
		};
	}
	
	public static PasswordPolicy atLeastDigit(int amt) {
		return new CharHasWhateverPasswordPolicy(amt, CharHasWhateverPasswordPolicy.Type.DIGIT) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
			
			@Override
			public boolean hasRequiredAmt(int count) {
				return count >= this.amt;
			}
		};
	}
	
	public static PasswordPolicy nullPolicy() {
		return new CharHasWhateverPasswordPolicy() {
			@Override
			public boolean charIsWhatever(char ch) {
				return false;
			}
			
		};
	}

}
