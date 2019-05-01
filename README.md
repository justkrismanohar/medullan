# AuthLib

Implementation of Authenticaiton Libaray for MyHealthPass application
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Assumptions
* All input to the system is fed through the EndPontion method calls (register, login, autheticateSession
* One instance of EndPoint per region. Currenlty each EndPoint executes sever [AppPolicies](https://github.com/justkrismanohar/medullan/blob/master/AuthLib/src/core/api/AppPolicies.java) which are configurable via an xml file like [config.xml](https://github.com/justkrismanohar/medullan/blob/master/AuthLib/config.xml)

## Limitations
The following limitations are due mainly to lack to time on my part
* Only MockDB (in memory) is implemented. However the library was build against the [QueryLayer](https://github.com/justkrismanohar/medullan/blob/master/AuthLib/src/core/queries/QueryLayer.java) interface. So support for any database rolled out easily
* Logging. Logs input and output of high level calls and some essentail depper API calls to console. Need to more logging the core logic and write to logs to database or file (something persistent).
* Nulls, exceptions and Null Objects. Impelmented null objects at the factory level. Need to complete for the rest of code base.
* Document each method and how it works following the java docs convents (i.e., @param etc) 

### Prerequisites

Include the following dependenices in your classpath. They are include AuthLib folder for easy access.

* [hamcrest-all-1.3.jar](https://github.com/justkrismanohar/medullan/raw/master/AuthLib/hamcrest-all-1.3.jar) 
* [log4j-api-2.11.2.jar](https://github.com/justkrismanohar/medullan/raw/master/AuthLib/log4j-api-2.11.2.jar) 
* [log4j-core-2.11.2.jar](https://github.com/justkrismanohar/medullan/raw/master/AuthLib/log4j-core-2.11.2.jar) 
* [servlet-api-2.5.jar](https://github.com/justkrismanohar/medullan/raw/master/AuthLib/servlet-api-2.5.jar)
* [Junit4](https://github.com/junit-team/junit4/wiki/Download-and-Install)

### Installing

The following steps use eclipse. You can you whatever Java IDE you like.

1. Create a blank Java projec in Eclispe

2. Download latest build at
* [AuthLib.jar](https://github.com/justkrismanohar/medullan/blob/master/AuthLib/exported-jar/AuthLib.jar) 

3. Add the prerequisite jar files your build path. 
```
In Eclispe Project folder > Build Path > Configure Build Path > Add external jars
```
4. Add AuthLib.jar ot build path.

### Sending request

1. Create an EndPoint. The Policies for the EndPoint are specified in and xml file.
See [config.xml](https://github.com/justkrismanohar/medullan/blob/master/AuthLib/config.xml) for an example.
```
EndPoint end = new EndPoint("config.xml");
```
2. Create a request either with a default signature using a the UnitTestHelper
```
LoginRequest req = UnitTestHelper.getSignatureWithUsernameAndPassword(1, "justkrismanohar@gmail.com", "JuSTKris124");
```
OR Specify your own with 

```
LoginRequest req = UnitTestHelper.createSignature("ip_address","cookie_name","cookie_value","user_agent");
req.loginDetails.userName = "user_name";
req.loginDetails.encryptedPassword = "secure_password";
```
For example
```
LoginRequest req = UnitTestHelper.createSignature("127.0.0.1","c1","val1","Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html));");
req.loginDetails.userName = "justkrismanohar@gmail.com";
req.loginDetails.encryptedPassword = "JuSTKris124";
```
3. Send the request to the EndPoint. All EndPoints return a boolean which indicates if the transasction was completed or not
```
end.register(req);
end.login(req);
end.authenticateSession(req);
```

### Quick Example code. Check console for logs
```
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
		end.authenticateSession(reqFail);
		
		
	}
}
```

## Running the tests

1. Git clone
 
```
git clone https://github.com/justkrismanohar/medullan.git
```
2. Import Java project AuthLib into eclispe

3. Add pre-requisite jars to build path

4. In test package compile and run RunAllUnitTests.java as unit test (or can run it as main)
This executes all the Junit test in the test directory. These profrom component and End to End tests.

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Kris Manohar** 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

