package be.jeroenbellen.aspects;

import be.jeroenbellen.annotations.Memcacheable;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeoutException;

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
    @Autowired
    private XMemcachedClient xMemcachedClient;
    
    private String testKey = "be.jeroenbellen.aspects.MyTestService_greet";

    @Before
    public void clearCache() throws MemcachedException, TimeoutException, InterruptedException {
        this.xMemcachedClient.delete(testKey);
    }

    @Test
    public void testMethodExecution() throws Throwable {
        assertEquals("Hello, Jeroen", this.myTestService.greet("Jeroen"));
    }

    @Test
    public void testCacheKey() throws Throwable {
        this.myTestService.greet("Jeroen");
        assertTrue(this.annotatedMemcachedMonitor.getCache().containsKey(testKey));
    }

    @Test
    public void testCacheIsFilled() throws Throwable {
        this.myTestService.greet("Jeroen");
        assertEquals("Hello, Jeroen", this.annotatedMemcachedMonitor.getCache().get(testKey));
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
