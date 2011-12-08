package be.jeroenbellen.aspects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 8:10 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:be/jeroenbellen/system-test-config.xml"})
public class AnnotatedMemcachedIntegrationTest {
    @Autowired
    private MyTestService myTestService;

    @Test
    public void testMethodExecution() {
        System.out.println(myTestService.greet("Jeroen"));
    }

}

@Component
class MyTestService {

    public String greet(String name) {
        return "Hello, " + name;
    }
}
