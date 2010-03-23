package org.jboss.seam.faces.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@Qualifier
@Target( { FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface AnyPhase
{
}