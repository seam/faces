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
   public PhaseIdType initial() default PhaseIdType.RENDER_RESPONSE; // also stored as a static in ViewConfigSecurityEnforcer (impl)
   public PhaseIdType postback() default PhaseIdType.INVOKE_APPLICATION;
}
