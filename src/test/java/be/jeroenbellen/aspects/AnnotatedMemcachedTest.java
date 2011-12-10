package be.jeroenbellen.aspects;

import be.jeroenbellen.cache.ICache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 6:43 PM
 */
public class AnnotatedMemcachedTest {
    private final String failMessage = "Not supposed to be called";

    private ProceedingJoinPoint testPoint;
    private ICache cache;

    @Before
    public void setUp() {
        this.cache = new CacheStub();
        this.testPoint = new ProceedingJoinPoint() {
            private int count = 0;

            public void set$AroundClosure(AroundClosure arc) {
                fail(failMessage);
            }

            public Object proceed() throws Throwable {
                return new BigDecimal(++count);
            }

            public Object proceed(Object[] args) throws Throwable {
                fail(failMessage);
                return null;
            }

            public String toShortString() {
                fail(failMessage);
                return null;
            }

            public String toLongString() {
                fail(failMessage);
                return null;
            }

            public Object getThis() {
                fail(failMessage);
                return null;
            }

            public Object getTarget() {
                fail(failMessage);
                return null;
            }

            public Object[] getArgs() {
                fail(failMessage);
                return null;
            }

            public Signature getSignature() {
                return new Signature() {
                    public String toShortString() {
                        fail(failMessage);
                        return null;
                    }

                    public String toLongString() {
                        fail(failMessage);
                        return null;
                    }

                    public String getName() {
                        return "myName";
                    }

                    public int getModifiers() {
                        fail(failMessage);
                        return 0;
                    }

                    public Class getDeclaringType() {
                        fail(failMessage);
                        return null;
                    }

                    public String getDeclaringTypeName() {
                        return "myType";
                    }
                };
            }

            public SourceLocation getSourceLocation() {
                fail(failMessage);
                return null;
            }

            public String getKind() {
                fail(failMessage);
                return null;
            }

            public StaticPart getStaticPart() {
                fail(failMessage);
                return null;
            }
        };
    }

    @Test
    public void testMethodExecution() throws Throwable {
        AnnotatedMemcachedMonitor amm = new AnnotatedMemcachedMonitor(this.cache);
        assertTrue(BigDecimal.ONE.equals(amm.cache(testPoint, null)));
    }

    @Test
    public void testCacheKey() throws Throwable {
        AnnotatedMemcachedMonitor amm = new AnnotatedMemcachedMonitor(this.cache);
        amm.cache(testPoint, null);
        String expectedKey = new StringBuilder()
                .append(testPoint.getSignature().getDeclaringTypeName())
                .append("_")
                .append(testPoint.getSignature().getName())
                .toString();
        assertTrue(amm.getCache().containsKey(expectedKey));
    }

    @Test
    public void testCacheIsFilled() throws Throwable {
        AnnotatedMemcachedMonitor amm = new AnnotatedMemcachedMonitor(this.cache);
        amm.cache(testPoint, null);
        String expectedKey = new StringBuilder()
                .append(testPoint.getSignature().getDeclaringTypeName())
                .append("_")
                .append(testPoint.getSignature().getName())
                .toString();
        final Object value = amm.getCache().get(expectedKey);
        assertTrue(BigDecimal.ONE.equals(value));
    }

    @Test
    public void testCache() throws Throwable {
        AnnotatedMemcachedMonitor amm = new AnnotatedMemcachedMonitor(this.cache);
        amm.cache(testPoint, null);
        amm.cache(testPoint, null);
        assertTrue(BigDecimal.ONE.equals(amm.cache(testPoint, null)));
    }

    private class CacheStub implements ICache {
        private Map<String, Object> cache = new HashMap<String, Object>();


        public Object get(String key) {
            return this.cache.get(key);
        }

        public void put(String key, Object data) {
            this.cache.put(key, data);
        }

        @Override
        public void put(String key, Object data, int exp) {
           this.cache.put(key, data);
        }

        public boolean containsKey(String key) {
            return this.cache.containsKey(key);
        }
    }
}
