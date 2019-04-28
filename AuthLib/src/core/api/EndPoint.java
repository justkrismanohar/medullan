package core.api;

import core.models.LoginRequest;

public class EndPoint {

		/**
		 * External APIs will use an EndPoint to access the library
		 */
		public boolean login(LoginRequest req) {
			//Check if the request is blocked
			//Determine if the UN + PWD pair match
			//Apply security policies
			//If pass checks return true
			return true;
		}
}
