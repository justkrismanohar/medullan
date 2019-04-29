package core.policy.password;

import core.policy.password.CharX;

public class CharUpper implements CharX{
	@Override
	public boolean charIsWhatever(char c) {
		return Character.isUpperCase(c);
	}
}

