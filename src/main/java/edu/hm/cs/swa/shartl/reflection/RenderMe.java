package edu.hm.cs.swa.shartl.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation RenderMe.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RenderMe {
    /**
     * if set, use not default renderer.
     * need to be a renderer with its canonical name.
     * @return canonical name of renderer which should be used.
     */
    String with() default "";
}
