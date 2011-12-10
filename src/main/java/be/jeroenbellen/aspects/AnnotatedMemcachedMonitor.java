package be.jeroenbellen.aspects;

import be.jeroenbellen.annotations.Memcacheable;
import be.jeroenbellen.cache.ICache;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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

    @Around("execution(* *..*(..)) && @annotation(memcacheable)")
    public Object cache(final ProceedingJoinPoint joinPoint, final Memcacheable memcacheable) throws Throwable {
        this.logger.debug("Doing magic!");
        final String key = argsKey(joinPoint);
        Object value = this.cache.get(key);
        if (value == null) {
            this.logger.debug("First time, lets cache!");
            value = joinPoint.proceed();
            if (memcacheable != null && memcacheable.expireTime() != 0) {
                cache.put(key, value, memcacheable.expireTime());
            } else {
                cache.put(key, value);
            }
        }
        return value;
    }

    private String argsKey(ProceedingJoinPoint joinPoint) {
        final StringBuilder stringBuilder = new StringBuilder(this.key(joinPoint));
        StringBuilder argKey = new StringBuilder();
        for (Object obj : joinPoint.getArgs()) {
            argKey.append(obj.toString()).append("|");
        }
        return stringBuilder.append("_").append(argKey.toString().hashCode()).toString();
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
