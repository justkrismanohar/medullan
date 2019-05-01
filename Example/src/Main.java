import core.api.EndPoint;
import core.models.LoginRequest;
import core.utils.LoginRequestFactory;
import core.utils.UnitTestHelper;

public class Main {
	public static void main(String[] args) throws Exception {
		EndPoint end = new EndPoint();
		
		
		LoginRequest req = UnitTestHelper.getSignatureWithUsernameAndPassword(1, "justkrismanohar@gmail.com", "JuSTKris124");
		end.register(req);
		end.login(req);
		
		LoginRequest reqFail = UnitTestHelper.getSignatureWithUsernameAndPassword(1, "justkrismanohar@gmail.com", "JuSTKris12");
		
		for(int i = 0; i < 10; i++)
			end.login(reqFail);
		
		end.login(req);
		
		
	}
}
