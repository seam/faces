package org.jboss.seam.faces.lifecycle;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Binding type that identifies a PhaseEvent which is raised
 * after a JSF life-cycle phase.
 * 
 * @author Dan Allen
 */
@Target( { PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
@Qualifier
@Inherited
public @interface AfterPhase
{
}
