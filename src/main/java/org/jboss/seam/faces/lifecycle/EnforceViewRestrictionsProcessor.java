package org.jboss.seam.faces.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.inject.Inject;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.jboss.seam.faces.component.UIRestrictView;
import org.jboss.seam.security.Identity;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * <p>
 * A JSF metadata facet processor which enforces restrictions and permissions in
 * the Render Response phase of the JSF life cycle, immediately prior to the
 * view being rendered.
 * </p>
 * 
 * <p>
 * The prerequisites are provided in the form of an EL ValueExpression and are
 * evaluated by the Identity component of the Seam security module. If the
 * Identity component indicates the security is disabled, the restrictions are
 * not enforced.
 * </p>
 * 
 * <p>
 * If the user is not logged in, a NotLoggedInException is passed to the JSF
 * exception handler. If the user is logged in and the restriction or permission
 * is not met, an AuthorizationException is passed to the JSF exception handler.
 * </p>
 * 
 * @author Dan Allen
 */
public class EnforceViewRestrictionsProcessor extends AbstractViewMetadataProcesssor
{
   @Logger Log log;

   @Inject FacesContext facesContext;
   @Inject Identity identity;

   @Override
   public boolean execute()
   {
      UIViewRoot viewRoot = facesContext.getViewRoot();

      // collect first so as not to introduce a hard dependency on Identity if
      // tag is not in use
      Collection<UIRestrictView> restrictions = collectionViewRestrictions(viewRoot);
      if (restrictions.isEmpty() || !Identity.isSecurityEnabled())
      {
         return true;
      }

      if (log.isTraceEnabled())
      {
         log.trace("Processing view restrictions before render view");
      }

      try
      {
         for (UIRestrictView restriction : restrictions)
         {
            if (restriction.getRequire() != null)
            {
               identity.checkRestriction(restriction.getRequire());
            }
            else
            {
               identity.checkPermission(viewRoot.getViewId(), "render");
            }
         }
      }
      // FIXME damn this is ugly, but JCDI is wrapping exceptions
      catch (Exception e)
      {
         Throwable cause = e;
         if (e instanceof InvocationTargetException)
         {
            cause = e.getCause();
         }

         facesContext.getApplication().publishEvent(facesContext, ExceptionQueuedEvent.class, new ExceptionQueuedEventContext(facesContext, cause));
         // FIXME this is lame; there should be some other way to stop view
         // rendering
         facesContext.getViewRoot().setRendered(false);
         throw new AbortProcessingException("View restriction criteria was not met.");
      }

      return true;
   }

   /**
    * Pick out the UIRestrictView components from the metadata facet's children.
    * If no matches are found, an unmodifiable empty list is returned.
    */
   protected Collection<UIRestrictView> collectionViewRestrictions(UIViewRoot viewRoot)
   {
      return collectMetadataComponents(viewRoot, new UIComponentFilter<UIRestrictView>()
      {

         @Override
         public boolean accepts(UIComponent candidate)
         {
            return candidate instanceof UIRestrictView;
         }

      });
   }
}
