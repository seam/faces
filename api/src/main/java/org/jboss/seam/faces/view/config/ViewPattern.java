package org.jboss.seam.faces.view.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is applied to an enum constant to configure view specific information
 *
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ViewPattern {
    String value();
}
