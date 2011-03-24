package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
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
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.solder.core.Requires;

/**
 * Use the annotations stored in the ViewConfigStore to restrict view access
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("org.jboss.seam.security.extension.SecurityExtension")
public class SecurityPhaseListener
{
   private transient final Logger log = Logger.getLogger(SecurityPhaseListener.class);

   @Inject
   private ViewConfigStore viewConfigStore;
   @Inject
   private Event<AuthorizationCheckEvent> authorizationCheckEvent;

   public void observeRenderResponse(@Observes @Before @RenderResponse PhaseEvent event)
   {
      log.debug("Before Render Response event");
      performObservation(event, PhaseIdType.RENDER_RESPONSE);
    }

   public void observeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent event)
   {
      log.debug("Before Render Response event");
      performObservation(event, PhaseIdType.INVOKE_APPLICATION);
    }

   public void observeRestoreView(@Observes @After @RestoreView PhaseEvent event)
   {
      log.debug("After Restore View event");
      performObservation(event, PhaseIdType.RESTORE_VIEW);
    }

   private void performObservation(PhaseEvent event, PhaseIdType phaseIdType)
   {
       UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
      if (isRestrictPhase(phaseIdType, viewRoot.getViewId(), event.getFacesContext().isPostback()))
      {
         log.debugf("Enforcing on phase %s", phaseIdType);
         enforce(event.getFacesContext(), viewRoot);
      }
   }

   public boolean isRestrictPhase(PhaseIdType currentPhase, String viewId,  boolean isPostback)
   {
      RestrictAtPhase restrictAtPhase = viewConfigStore.getAnnotationData(viewId, RestrictAtPhase.class);
      PhaseIdType restrictAtPhaseType = null;
      if (restrictAtPhase != null)
      {
         restrictAtPhaseType = isPostback ? restrictAtPhase.postback() : restrictAtPhase.initial();
      }
      if (restrictAtPhaseType == null)
      {
         restrictAtPhaseType = isPostback ? RestrictAtPhaseDefault.RESTRICT_POSTBACK_DEFAULT : RestrictAtPhaseDefault.RESTRICT_INITIAL_DEFAULT;
      }
      return restrictAtPhaseType.equals(currentPhase);
   }

   private void enforce(FacesContext facesContext, UIViewRoot viewRoot)
   {
      List<? extends Annotation> annotations = viewConfigStore.getAllQualifierData(viewRoot.getViewId(), SecurityBindingType.class);
      if (annotations == null || annotations.isEmpty())
      {
         log.debug("Annotations is null/empty");
         return;
      }
      AuthorizationCheckEvent event = new AuthorizationCheckEvent(annotations);
      authorizationCheckEvent.fire(event);
      if (! event.isPassed())
      {
         if (facesContext.getExternalContext().getUserPrincipal() == null)
         {
            log.debug("Access denied - not logged in");
            redirectToLoginPage(facesContext, viewRoot);
            return;
         }
         else
         {
            log.debug("Access denied - not authorized");
            redirectToAccessDeniedView(facesContext, viewRoot);
            return;
         }
      }
      else
      {
        log.debug("Access granted");
      }
   }

   private void redirectToLoginPage(FacesContext facesContext, UIViewRoot viewRoot)
   {
      LoginView loginView = viewConfigStore.getAnnotationData(viewRoot.getViewId(), LoginView.class);
      if (loginView == null || loginView.value() == null || loginView.value().isEmpty())
      {
         log.debug("Returning 401 response (login required)");
         facesContext.getExternalContext().setResponseStatus(401);
         facesContext.responseComplete();
         return;
      }
      String loginViewId = loginView.value();
      log.debugf("Redirecting to configured LoginView %s", loginViewId);
      NavigationHandler navHandler = facesContext.getApplication().getNavigationHandler();
      navHandler.handleNavigation(facesContext, "", loginViewId);
      facesContext.renderResponse();
   }
   
   private void redirectToAccessDeniedView(FacesContext facesContext, UIViewRoot viewRoot)
   {
      AccessDeniedView accessDeniedView = viewConfigStore.getAnnotationData(viewRoot.getViewId(), AccessDeniedView.class);
      if (accessDeniedView == null || accessDeniedView.value() == null || accessDeniedView.value().isEmpty())
      {
         log.debug("Returning 401 response (access denied)");
         facesContext.getExternalContext().setResponseStatus(401);
         facesContext.responseComplete();
         return;
      }
      String accessDeniedViewId = accessDeniedView.value();
      log.debugf("Redirecting to configured AccessDenied %s", accessDeniedViewId);          
      NavigationHandler navHandler = facesContext.getApplication().getNavigationHandler();
      navHandler.handleNavigation(facesContext, "", accessDeniedViewId);
      facesContext.renderResponse();
   }
}
