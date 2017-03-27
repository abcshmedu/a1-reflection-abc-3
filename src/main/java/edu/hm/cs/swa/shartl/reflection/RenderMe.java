package edu.hm.cs.swa.shartl.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation RenderMe.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RenderMe {
    /**
     * Currently not used.
     *
     * @return don' know
     */
    String with() default "";
}
