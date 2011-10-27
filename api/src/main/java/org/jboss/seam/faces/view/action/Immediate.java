package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.component.UIViewAction;

/** 
 * Can be used instead of Phase.
 * 
 * @see UIViewAction#isImmediate()
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Immediate {
	public Boolean immediate = null;
}
