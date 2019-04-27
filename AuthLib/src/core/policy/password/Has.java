package core.policy.password;

public class Has {
	public static PasswordPolicy lowerCase() {
		return new HasCheck() {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase() {
		return new HasCheck() {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit() {
		return new HasCheck() {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}
	
	public static PasswordPolicy lowerCase(int amt) {
		return new HasCheck(amt) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isLowerCase(ch);
			}
		};
	}
	
	public static PasswordPolicy upperCase(int amt) {
		return new HasCheck(amt) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isUpperCase(ch);
			}
		};
	}
	
	public static PasswordPolicy digit(int amt) {
		return new HasCheck(amt) {
			@Override
			public boolean charIsWhatever(char ch) {
				return Character.isDigit(ch);
			}
		};
	}

	public static PasswordPolicy atLeastLowerCase(int amt) {
		return new HasCheck(amt) {
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
		return new HasCheck(amt) {
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
		return new HasCheck(amt) {
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

}
