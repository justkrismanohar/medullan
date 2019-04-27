package core.policy;

import core.policy.CharX;

public class CharUpper implements CharX{
	@Override
	public boolean charIsWhatever(char c) {
		return Character.isUpperCase(c);
	}
}

