package core.policy.password;

import java.util.ArrayList;

public class ANDCompositePasswordPolicy extends CompositePasswordPolicy {
	
	@Override
	public boolean evaluatePassword(String password) {
		for(PasswordPolicy p : list)
			if(!p.evaluatePassword(password))
				return false;
		return true;
	}
	
	public boolean equals(Object o) {
		return o instanceof ANDCompositePasswordPolicy && super.equals(o);		
	}
}
