package be.jeroenbellen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 6:26 PM
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Memcacheable {
    int expireTime() default 0;
}
