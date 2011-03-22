package org.jboss.seam.faces.view.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jboss.seam.faces.event.PhaseIdType;

/**
 * The phase when security restrictions are applied
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface RestrictAtPhase
{
   public static PhaseIdType RESTRICT_INITIAL_DEFAULT = PhaseIdType.RENDER_RESPONSE;
   public static PhaseIdType RESTRICT_POSTBACK_DEFAULT = PhaseIdType.INVOKE_APPLICATION;
   
   public PhaseIdType initial() default PhaseIdType.RENDER_RESPONSE;
   public PhaseIdType postback() default PhaseIdType.INVOKE_APPLICATION;
}
