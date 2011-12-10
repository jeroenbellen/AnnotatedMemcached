package be.jeroenbellen.aspects;

import be.jeroenbellen.cache.ICache;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 6:20 PM
 */
@Aspect
public class AnnotatedMemcachedMonitor {
    private final Logger logger = Logger.getLogger(getClass());
    private ICache cache;

    public AnnotatedMemcachedMonitor(ICache cache) {
        this.cache = cache;
    }

    @Around("execution(@be.jeroenbellen.annotations.Memcacheable * *..*(..))")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
        this.logger.info("Doing magic!");
        final String key = key(joinPoint);
        Object value = this.cache.get(key);
        if (value == null) {
            this.logger.info("First time, lets cache!");
            value = joinPoint.proceed();
            cache.put(key, value);
        }
        return value;
    }

    private String key(ProceedingJoinPoint joinPoint) {
        return new StringBuilder()
                .append(joinPoint.getSignature().getDeclaringTypeName())
                .append("_")
                .append(joinPoint.getSignature().getName())
                .toString();
    }

    public ICache getCache() {
        return cache;
    }
}
