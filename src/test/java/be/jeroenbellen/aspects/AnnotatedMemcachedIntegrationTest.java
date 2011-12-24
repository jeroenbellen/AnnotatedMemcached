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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 8:10 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:system-test-config.xml"})
public class AnnotatedMemcachedIntegrationTest {
    @Autowired
    private MyTestService myTestService;
    @Autowired
    private AnnotatedMemcachedMonitor annotatedMemcachedMonitor;
    @Autowired
    private XMemcachedClient xMemcachedClient;

    private String testKey = "be.jeroenbellen.aspects.MyTestService_greet_" + "Jeroen|".hashCode();

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
        assertNotSame("Hello, Jeroen", this.myTestService.greet("Een andere naam"));
    }

    @Test
    public void testExpireTime() throws InterruptedException {
        Date now = this.myTestService.getCurrentTime();
        Thread.sleep(1000);
        assertEquals(now.getTime(), this.myTestService.getCurrentTime().getTime());
        Thread.sleep(2000);
        assertTrue(now.getTime() != this.myTestService.getCurrentTime().getTime());
    }

    @Test
    public void testCacheIgnoreArgs() throws Throwable {
        this.myTestService.greetIgnoreArgs("Jeroen");
        assertEquals("Hello, Jeroen", this.myTestService.greetIgnoreArgs("Een andere naam"));
    }

}

@Component
class MyTestService {

    @Memcacheable
    public String greet(String name) {
        return "Hello, " + name;
    }

    @Memcacheable(ignoreArguments = true)
    public String greetIgnoreArgs(String name) {
        return "Hello, " + name;
    }

    @Memcacheable(expireTime = 3)
    public Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }
}
