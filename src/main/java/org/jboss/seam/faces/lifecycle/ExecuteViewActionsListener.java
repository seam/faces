package org.jboss.seam.faces.lifecycle;

import java.util.Collection;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.inject.Inject;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.component.UIViewAction;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * <p>
 * A JSF metadata facet processor which executes action expressions in the
 * Render Response phase of the JSF life cycle, immediate prior to the view
 * being rendered.
 * </p>
 * 
 * <p>
 * Before the first action is processed, the processor first checks if this is
 * an initial (non-faces) request and the FacesContext reports that validation
 * has failed. This situation occurs when validation has failed on a view
 * parameter. The processor will execute the navigation handler using a null
 * "from action" and the built-in logical outcome
 * "org.jboss.seam.ViewParameterValidationFailed".
 * </p>
 * 
 * <p>
 * View actions are executed in the order that they appear in the view template.
 * After each action is executed, the navigation handler is fired. If a
 * navigation case is pursued, it short-circuits the remaining actions. It also
 * instructs the processor chain to abort. Otherwise, the remaining actions are
 * processed in the same manner.
 * </p>
 * 
 * @author Dan Allen
 */
public class ExecuteViewActionsListener extends AbstractViewMetadataProcesssor
{
   public static final String VIEW_PARAMETER_VALIDATION_FAILED_OUTCOME = "org.jboss.seam.ViewParameterValidationFailed";
   
   @Logger Log log;
   
   @Inject FacesContext facesContext;

   @Override
   public boolean execute()
   {
      UIViewRoot initialViewRoot = facesContext.getViewRoot();

      if (log.isTraceEnabled())
      {
         log.trace("Processing view actions before render view");
      }
      
      NavigationHandler navHandler = facesContext.getApplication().getNavigationHandler();
      boolean postback = facesContext.isPostback();

      // check if any view parameters failed validation and if so, fire the navigation handler
      if (!postback && facesContext.isValidationFailed())
      {
         if (log.isTraceEnabled())
         {
            log.trace("Validation of view parameters failed. Calling navigation handler without executing view actions.");
         }
         // QUESTION is this a good idea to use a built-in logical outcome?
         navHandler.handleNavigation(facesContext, null, VIEW_PARAMETER_VALIDATION_FAILED_OUTCOME);
         return !facesContext.getResponseComplete() && initialViewRoot.getViewId().equals(facesContext.getViewRoot().getViewId());
      }

      boolean proceed = true;
      Collection<UIViewAction> actions = collectViewActions(initialViewRoot, postback);
      for (UIViewAction action : actions)
      {
         String outcome = null;
         String fromAction = null;

         MethodExpression execute = action.getExecute();
         // QUESTION shouldn't this be an illegal state otherwise??
         if (execute != null)
         {
            if (log.isDebugEnabled())
            {
               log.debug("Executing view action expression {0}", execute.getExpressionString());
            }
            try
            {
               Object returnVal = execute.invoke(facesContext.getELContext(), null);
               outcome = (returnVal != null ? returnVal.toString() : null);
               fromAction = execute.getExpressionString();
            }
            catch (ELException e)
            {
               if (log.isErrorEnabled())
               {
                  log.error(e.getMessage(), e);
               }
               throw new FacesException(execute.getExpressionString() + ": " + e.getMessage(), e);
            }
         }

         navHandler.handleNavigation(facesContext, fromAction, outcome);
         
         // short-circuit actions if response has been marked complete
         if (facesContext.getResponseComplete())
         {
            if (log.isDebugEnabled())
            {
               log.debug("Response marked as complete during view action processing. Short-circuiting remaining actions.");
            }
            // FIXME this is lame; there should be some other way to stop view rendering
            facesContext.getViewRoot().setRendered(false);
            proceed = false;
            break;
         }
         // short-circuit actions if a navigation case was pursued
         else if (!initialViewRoot.getViewId().equals(facesContext.getViewRoot().getViewId()))
         {
            if (log.isDebugEnabled())
            {
               log.debug("Detected change in view ID during view action processing. Short-circuiting remaining actions.");
            }
            proceed = false;
            break;
         }
      }
      
      return proceed;
   }
   
   /**
    * Pick out the UIViewAction components from the metadata facet's children. If this is a postback,
    * only select UIViewAction components that are to be executed on a postback. If no matches
    * are found, an unmodifiable empty list is returned.
    */
   protected Collection<UIViewAction> collectViewActions(UIViewRoot viewRoot, final boolean postback)
   {
      return collectMetadataComponents(viewRoot, new UIComponentFilter<UIViewAction>() {

         @Override
         public boolean accepts(UIComponent candidate)
         {
            return candidate instanceof UIViewAction && ((UIViewAction) candidate).isIf() && (!postback || ((UIViewAction) candidate).isOnPostback());
         }
         
      });
   }

}
