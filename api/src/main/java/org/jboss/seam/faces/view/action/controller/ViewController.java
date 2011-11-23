package org.jboss.seam.faces.view.action.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.jboss.seam.faces.view.action.ViewAction;

/**
 * This annotation must be used on a ViewConfig to specify its viewControllers.
 * 
 * <p>A viewController is a managed bean handling a specific view.
 * Some methods of the bean can be called during the lifecycle of the view.
 * Those methods must be annotated with {@link BeforeRenderResponse}, {@link AfterRenderResponse}, or a mixture of
 * {@link Before}, {@link After}, {@link ApplyRequestValues}, {@link ProcessValidations}, {@link UpdateModelValues},
 * {@link InvokeApplication} or {@link RenderResponse}.</p>
 * 
 * <p>Classic use case are :
 * <ul>
 * <li> {@link BeforeRenderResponse} for handling view initialization data (i.e. fetching data from database).</li>
 * <li> {@link AfterRenderResponse} for view cleanup.</li>
 * </ul></p>
 *
 * @author Adriàn Gonzalez
 */
@ViewAction(ViewControllerHandlerProvider.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ViewController {
    Class<?>[] value();
}
