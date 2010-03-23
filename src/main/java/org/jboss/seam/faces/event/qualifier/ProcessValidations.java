package org.jboss.seam.faces.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.faces.event.PhaseEvent;
import javax.inject.Qualifier;

/**
 * Qualifies observer method parameters to select events in one of the "process validations"
 * phase in the JSF lifecycle. The selection can further be refined by combining it with the 
 * qualifiers {@link @Before} or {@link @After}. The event parameter is a {@link PhaseEvent}. 
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ProcessValidations {}