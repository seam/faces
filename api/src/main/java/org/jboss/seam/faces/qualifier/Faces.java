package org.jboss.seam.faces.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A @{@link Qualifier} for an object or @{@link Produces} method that depends on the presence of an active JSF life-cycle in
 * order to be injected successfully. This means that JSF must currently be servicing an active Request in order for an object
 * qualified with <b>this</b> annotation in order to be available.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Qualifier
@Target({TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Faces {

}
