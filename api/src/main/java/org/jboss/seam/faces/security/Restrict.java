package org.jboss.seam.faces.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jboss.seam.security.annotations.SecurityBindingType;

/**
 * Configuration annotation for securing view access
 * 
 * @author bleathem
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@SecurityBindingType
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Restrict
{
    String value();
}
