package org.jboss.seam.faces.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The viewId to redirect to when login is required
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface LoginView {
    String value();
}
