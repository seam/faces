package org.jboss.seam.faces.examples.viewconfig.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.security.annotations.SecurityBindingType;

/**
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@SecurityBindingType
@RestrictAtPhase(PhaseIdType.RESTORE_VIEW)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Admin {
}
