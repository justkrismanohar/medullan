package core.policy.password;

import java.util.ArrayList;

public class CompositeORPasswordPolicy extends CompositePasswordPolicy{
	
	@Override
	public boolean evaluatePassword(String password) {
		for(PasswordPolicy p : list)
			if(p.evaluatePassword(password))
				return true;
		return false;
	}
	
	public boolean equals(Object o) {
		return o instanceof CompositeORPasswordPolicy && super.equals(o);
	}
}
