package org.jboss.seam.faces.event.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Qualifies observer method parameters to select events that occur in a "before"
 * phase in the JSF lifecycle 
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Before {}