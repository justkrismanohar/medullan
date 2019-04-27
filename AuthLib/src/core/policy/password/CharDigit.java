package core.policy.password;

import core.policy.password.CharX;

public class CharDigit implements CharX{
		@Override
		public boolean charIsWhatever(char c) {
			return Character.isDigit(c);
		}
	}