package ca.triumf.mis.jsf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jboss.seam.security.annotations.SecurityBindingType;

/**
 *
 * @author bleathem
 */
@SecurityBindingType
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Owner {
}
