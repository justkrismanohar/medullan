package core.policy.password;

public class CharHasSometingPasswordPolicyFactory {
	public static PasswordPolicy lowerCase() {
		return new CharHasPasswordPolicy(CharHasPasswordPolicy.Type.LOWER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase() {
		return new CharHasPasswordPolicy(CharHasPasswordPolicy.Type.UPPER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit() {
		return new CharHasPasswordPolicy(CharHasPasswordPolicy.Type.DIGIT) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}
	
	public static PasswordPolicy lowerCase(int amt) {
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.LOWER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase(int amt) {
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.UPPER_CASE) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit(int amt) {
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.DIGIT) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}

	public static PasswordPolicy atLeastLowerCase(int amt) {
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.LOWER_CASE) {
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
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.UPPER_CASE) {
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
		return new CharHasPasswordPolicy(amt, CharHasPasswordPolicy.Type.DIGIT) {
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
		return new CharHasPasswordPolicy() {
			@Override
			public boolean charIsWhatever(char ch) {
				return false;
			}
			
		};
	}

}
