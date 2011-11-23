package org.jboss.seam.faces.view.action.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to an annotation to indicate that it is a faces action binding type.
 * 
 * By default, this method will be called before RENDER_RESPONSE phase.
 * You can change the jsf phase by using the annotations from org.jboss.seam.faces.event.qualifier package
 * on your custom annotation.  
 *
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewActionBindingType {
}
