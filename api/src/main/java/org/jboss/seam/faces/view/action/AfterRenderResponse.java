package org.jboss.seam.faces.view.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This lifecycle annotation can be used on viewController methods.
 * 
 * These methods will be called after JSF RENDER_VIEW phase.
 * Typically used for cleanup purposes
 * Shortcut for &#064;After &#064;RenderResponse.
 *
 * @author Adriàn Gonzalez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AfterRenderResponse {
}
