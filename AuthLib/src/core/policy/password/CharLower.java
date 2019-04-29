package core.policy.password;

import core.policy.password.CharX;

public class CharLower implements CharX{
		@Override
		public boolean charIsWhatever(char c) {
			return Character.isLowerCase(c);
		}
	}
	
	