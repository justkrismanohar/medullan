package core.policy;

import core.policy.CharX;

public class CharLower implements CharX{
		@Override
		public boolean charIsWhatever(char c) {
			return Character.isLowerCase(c);
		}
	}
	
	