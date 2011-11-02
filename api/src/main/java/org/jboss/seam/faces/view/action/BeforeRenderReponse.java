package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This lifecycle annotation can be used on viewController methods.
 * 
 * These methods will be called before JSF RENDER_VIEW phase.  
 * Typically used for view initialization purposes.
 * Shortcut for &#064;Before &#064;RenderResponse.
 * 
 * @author Adriàn Gonzalez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface BeforeRenderReponse {
}
