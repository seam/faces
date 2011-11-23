package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This optionnal annotation can be used to modify the execution order of viewActions.
 * 
 * <p>viewActions will be executed from lowest to highest order. If order is not specified,
 * The default value {@link OrderDefault#DEFAULT} is assumed.</p>  
 *   
 * @author Adriàn Gonzalez
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {
    int value();
}
