package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.solder.core.Requires;

/**
 * Use the annotations stored in the ViewConfigStore to restrict view access
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("org.jboss.seam.security.extension.SecurityExtension")
public class ViewConfigSecurityEnforcer
{
   private transient final Logger log = Logger.getLogger(ViewConfigSecurityEnforcer.class);

   @Inject
   private ViewConfigStore viewConfigStore;
   @Inject
   private Event<SecurityCheckEvent> securityCheckEvent;

   public void observeRenderResponse(@Observes @Before @RenderResponse PhaseEvent event)
   {
      log.info("Before Render Response event");
      UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
      
      if (isRestrictPhase(PhaseIdType.RENDER_RESPONSE, viewRoot.getViewId(), event.getFacesContext().isPostback()))
      {
         enforce(viewRoot);
      }
    }
   
   public void observeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent event)
   {
      log.info("Before Render Response event");
      UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
      if (isRestrictPhase(PhaseIdType.INVOKE_APPLICATION, viewRoot.getViewId(), event.getFacesContext().isPostback()))
      {
         enforce(viewRoot);
      }
    }
   
   public void observeRestoreView(@Observes @After @RestoreView PhaseEvent event)
   {
      log.info("After Restore View event");
      UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
      if (isRestrictPhase(PhaseIdType.RESTORE_VIEW, viewRoot.getViewId(), event.getFacesContext().isPostback()))
      {
         enforce(viewRoot);
      }
    }
   
   public boolean isRestrictPhase(PhaseIdType currentPhase, String viewId,  boolean isPostback)
   {
      RestrictAtPhase restrictAtPhase = viewConfigStore.getAnnotationData(viewId, RestrictAtPhase.class);
      PhaseIdType restrictAtPhaseType = null;
      if (restrictAtPhase != null)
      {
         restrictAtPhaseType = isPostback ? restrictAtPhase.postback() : RestrictAtPhase.RESTRICT_INITIAL_DEFAULT;
      }
      if (restrictAtPhaseType == null)
      {
         restrictAtPhaseType = isPostback ? RestrictAtPhase.RESTRICT_POSTBACK_DEFAULT : RestrictAtPhase.RESTRICT_INITIAL_DEFAULT;
      }
      return restrictAtPhaseType.equals(currentPhase);
   }
   
   private void enforce(UIViewRoot viewRoot)
   {
      List<? extends Annotation> annotations = viewConfigStore.getAllQualifierData(viewRoot.getViewId(), SecurityBindingType.class);
      if (annotations == null || annotations.isEmpty())
      {
         log.info("Annotations is null/empty");
         return;
      }
      SecurityCheckEvent securityEvent = new SecurityCheckEvent(annotations);
      securityCheckEvent.fire(securityEvent);
      if (! securityEvent.isAuthorized())
      {
         throw new AbortProcessingException("Access denied");
      }
      log.info("Access allowed");
   }
}
