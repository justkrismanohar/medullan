package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({XMLConfigTest.class,ApiLayerTest.class,ModelsTest.class,PasswordPolicyTest.class,SecurityPolicyTest.class,XMLConfigTest.class})



public class AllTests {
	   
   private static final Logger LOGGER = LogManager.getLogger(AllTests.class);
   public static void main(String[] args) {
	      Result result = JUnitCore.runClasses(AllTests.class);

	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      System.out.println(result.wasSuccessful());
	      LOGGER.warn("Hi");
   }
}
