package be.jeroenbellen.aspects;

import be.jeroenbellen.annotations.Memcacheable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    @Autowired
    private AnnotatedMemcachedMonitor annotatedMemcachedMonitor;

    @Test
    public void testMethodExecution() throws Throwable {
        assertEquals("Hello, Jeroen", this.myTestService.greet("Jeroen"));
    }

    @Test
    public void testCacheKey() throws Throwable {
        this.myTestService.greet("Jeroen");
        assertTrue(this.annotatedMemcachedMonitor.getCache().containsKey("be.jeroenbellen.aspects.MyTestService_greet"));
    }

    @Test
    public void testCacheIsFilled() throws Throwable {
        this.myTestService.greet("Jeroen");
        assertEquals("Hello, Jeroen", this.annotatedMemcachedMonitor.getCache().get("be.jeroenbellen.aspects.MyTestService_greet"));
    }

    @Test
    public void testCache() throws Throwable {
        this.myTestService.greet("Jeroen");
        assertEquals("Hello, Jeroen", this.myTestService.greet("Een andere naam"));
    }

}

@Component
class MyTestService {

    @Memcacheable
    public String greet(String name) {
        return "Hello, " + name;
    }
}
