package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.component.UIViewAction;
import org.jboss.seam.faces.event.PhaseIdType;

/** 
 * Phase on which a viewAction is executed.
 * 
 * @see UIViewAction#getPhase()
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Phase {
	public PhaseIdType value() default PhaseIdType.RENDER_RESPONSE;
}
