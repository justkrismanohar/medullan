package core.policy;

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
}
