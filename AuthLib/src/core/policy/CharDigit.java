package core.policy;

import core.policy.CharX;

public class CharDigit implements CharX{
		@Override
		public boolean charIsWhatever(char c) {
			return Character.isDigit(c);
		}
	}