package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to an annotation to indicate that it is a faces action binding type
 * 
 * Additionally, you can customize your view annotation follogin one of those approaches :
 * <ul>
 * <li> any other annotations in this package can also be applied to this annotation 
 * to customize view action behaviour (phase, postback, ...). This enables to customize behaviour 
 * per annotation definition</li>
 * <li> those annotations can be replaced by methods of the same name in the view action annotation.
 * This enables to customize behaviour per annotation usage.</li>
 * </ul>
 *
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewActionBindingType {
}
