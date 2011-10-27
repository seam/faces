package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.component.UIViewAction;

/** 
 * Determines if viewAction is executed on postback.
 * 
 * @see UIViewAction#isOnPostback()
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnPostback {
	public Boolean onPostback = false;
}
