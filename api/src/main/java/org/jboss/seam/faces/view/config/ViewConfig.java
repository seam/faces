package org.jboss.seam.faces.view.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is applied to an enum to signify that it contains
 * configuration for view specific data
 * 
 * @author Stuart Douglas
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ViewConfig
{

}
