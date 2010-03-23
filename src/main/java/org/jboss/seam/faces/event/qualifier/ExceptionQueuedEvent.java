package org.jboss.seam.faces.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Qualifies observer method parameters to select JSF exception queued events
 * The event parameter is a {@link javax.faces.event.ExceptionQueuedEvent}.
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ExceptionQueuedEvent {}