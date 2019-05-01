# AuthLib

Implementation of Authenticaiton Libaray for MyHealthPass application
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

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
3. Add AuthLib.jar ot build path.

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

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why
 
```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
