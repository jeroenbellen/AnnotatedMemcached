package be.jeroenbellen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 6:26 PM
 */
@Target({ElementType.METHOD})
public @interface Memcacheable {
}
