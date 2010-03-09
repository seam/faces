package org.jboss.seam.faces.application;

import java.util.List;
import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.lifecycle.ConvertStatusMessagesProcessor;

/**
 * Wrap the standard JSF view handler to capture the
 * request for a redirect URL so that the JSF messages
 * can be preserved.
 *
 * @author Dan Allen
 */
public class SeamViewHandler extends ViewHandlerWrapper
{
   private ViewHandler delegate;
   
   public SeamViewHandler(ViewHandler delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public ViewHandler getWrapped()
   {
      return delegate;
   }

   /**
    * If JSF is requesting a redirect URL, then likely a redirect is about to be performed.
    * Take this opportunity to store the JSF messages in the flash scope so that they
    * live past the redirect.
    *
    * @see ViewHandler#getRedirectURL(javax.faces.context.FacesContext, java.lang.String, java.util.Map, boolean)
    */
   @Override
   public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams)
   {
      // TODO temporary check if session has been killed be Web Beans throws an exception trying to access a session-scoped bean
      if (context.getExternalContext().getSession(false) != null)
      {
         // QUESTION hmmm, we have to convert to faces messages now to leverage JSF's flash feature...I suppose that is okay
//         BeanManagerHelper.getInstanceByType(ManagerBridge.getProvider().getCurrentManager(), 
//                 ConvertStatusMessagesProcessor.class).execute();
         // should I move this next step into TransferStatusMessagesListener?
         if (context.getMessages().hasNext())
         {
            context.getExternalContext().getFlash().setKeepMessages(true);
         }
      }
      
      return super.getRedirectURL(context, viewId, parameters, includeViewParams);
   }

}
