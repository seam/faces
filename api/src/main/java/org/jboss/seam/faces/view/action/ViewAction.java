package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.event.qualifier.RenderResponse;

/**
 * The EL MethodExpression is executed when this annotation is applied to a ViewConfig.
 * 
 * The MethodExpression is called by default before RENDER_RESPONSE phase. You can change this
 * behaviour by using phase and before fields.
 *
 * @author Adriàn Gonzalez
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewAction {
    /**
     * El MethodExpression
     */
    String value();
    
    /**
     * On which JSF phase must this viewAction be executed ? 
     */
    Class<?> phase() default RenderResponse.class;
    
    /**
     * Is this viewAction executed before phase ?
     */
    boolean before() default true;
}
