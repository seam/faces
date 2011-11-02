package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The EL MethodExpression is executed when this annotation is applied to a ViewConfig.
 * 
 * The MethodExpression is called before RENDER_RESPONSE phase.
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
}
