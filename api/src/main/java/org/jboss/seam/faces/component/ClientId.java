package org.jboss.seam.faces.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * @author Dan Allen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Qualifier
public @interface ClientId {
   /**
    * The search expression that matches one or more UIComponent instances by its clientId.
    */
   @Nonbinding String value() default "";
}
