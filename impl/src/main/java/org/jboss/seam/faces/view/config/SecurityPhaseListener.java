package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.solder.core.Requires;

/**
 * Use the annotations stored in the ViewConfigStore to restrict view access
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("org.jboss.seam.security.extension.SecurityExtension")
public class SecurityPhaseListener {

    private transient final Logger log = Logger.getLogger(SecurityPhaseListener.class);
    
    private static final String PRE_LOGIN_VIEW = SecurityPhaseListener.class.getName() + "_PRE_LOGIN_VIEW";
   
    @Inject
    private ViewConfigStore viewConfigStore;
    @Inject
    private Event<AuthorizationCheckEvent> authorizationCheckEvent;

    public void observeRenderResponse(@Observes @Before @RenderResponse PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.RENDER_RESPONSE);
    }

    public void observeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.INVOKE_APPLICATION);
    }

    public void observeRestoreView(@Observes @After @RestoreView PhaseEvent event) {
        log.debug("After Restore View event");
        performObservation(event, PhaseIdType.RESTORE_VIEW);
    }

    private void performObservation(PhaseEvent event, PhaseIdType phaseIdType) {
        UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
        List<? extends Annotation> restrictionsForPhase = getRestrictionsForPhase(phaseIdType, viewRoot.getViewId());
        if (restrictionsForPhase != null) {
            log.debugf("Enforcing on phase %s", phaseIdType);
            enforce(event.getFacesContext(), viewRoot, restrictionsForPhase);
        }
    }

    public List<? extends Annotation> getRestrictionsForPhase(PhaseIdType currentPhase, String viewId) {
        List<? extends Annotation> allSecurityAnnotations = viewConfigStore.getAllQualifierData(viewId, SecurityBindingType.class);
        List<Annotation> applicableSecurityAnnotations = null;
        for (Annotation annotation : allSecurityAnnotations) {
            PhaseIdType[] defaultPhases = getDefaultPhases(viewId);
            if (isAnnotationApplicableToPhase(annotation, currentPhase, defaultPhases)) {
                if (applicableSecurityAnnotations == null) { // avoid spawning arrays at all phases of the lifecycle
                    applicableSecurityAnnotations = new ArrayList<Annotation>();
                }
                applicableSecurityAnnotations.add(annotation);
            }
        }
        return applicableSecurityAnnotations;
    }
    
    public boolean isAnnotationApplicableToPhase(Annotation annotation, PhaseIdType currentPhase, PhaseIdType[] defaultPhases) {
        Method restrictAtViewMethod = getRestrictAtViewMethod(annotation);
        PhaseIdType[] phasedIds = null;
        if (restrictAtViewMethod != null) {
            phasedIds = getRestrictedPhaseIds(restrictAtViewMethod, annotation);
        }
        if (phasedIds == null) {
            log.debug("Falling back on default phase ids");
            phasedIds = defaultPhases;
        }
        if (Arrays.binarySearch(phasedIds, currentPhase) >= 0) {
            return true;
        }
        return false;
    }
    
    public PhaseIdType[] getDefaultPhases(String viewId) {
        PhaseIdType[] defaultPhases = null;
        RestrictAtPhase restrictAtPhase = viewConfigStore.getAnnotationData(viewId, RestrictAtPhase.class);
        if (restrictAtPhase != null) {
            defaultPhases = restrictAtPhase.value();
        }
        if (defaultPhases == null) {
            defaultPhases = RestrictAtPhaseDefault.DEFAULT_PHASES;
        }
        return defaultPhases;
    }

    public Method getRestrictAtViewMethod(Annotation annotation) {
        Method restrictAtViewMethod;
        try {
            restrictAtViewMethod = annotation.annotationType().getDeclaredMethod("restrictAtPhase");
        } catch (NoSuchMethodException ex) {
            restrictAtViewMethod = null;
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("restrictAtView method must be accessible", ex);
        }
        return restrictAtViewMethod;
    }
    
    public PhaseIdType[] getRestrictedPhaseIds(Method restrictAtViewMethod, Annotation annotation) {
        PhaseIdType[] phaseIds;
        try {
            phaseIds = (PhaseIdType[]) restrictAtViewMethod.invoke(annotation);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("restrictAtView method must be accessible", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        return phaseIds;
    }

    private void enforce(FacesContext context, UIViewRoot viewRoot, List<? extends Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            log.debug("Annotations is null/empty");
            return;
        }
        AuthorizationCheckEvent event = new AuthorizationCheckEvent(annotations);
        authorizationCheckEvent.fire(event);
        if (!event.isPassed()) {
            if (context.getExternalContext().getUserPrincipal() == null) {
                log.debug("Access denied - not logged in");
                redirectToLoginPage(context, viewRoot);
                return;
            } else {
                log.debug("Access denied - not authorized");
                redirectToAccessDeniedView(context, viewRoot);
                return;
            }
        } else {
            log.debug("Access granted");
        }
    }

    private void redirectToLoginPage(FacesContext context, UIViewRoot viewRoot) {
        context.getExternalContext().getSessionMap().put(PRE_LOGIN_VIEW, viewRoot.getViewId());
        LoginView loginView = viewConfigStore.getAnnotationData(viewRoot.getViewId(), LoginView.class);
        if (loginView == null || loginView.value() == null || loginView.value().isEmpty()) {
            log.debug("Returning 401 response (login required)");
            context.getExternalContext().setResponseStatus(401);
            context.responseComplete();
            return;
        }
        String loginViewId = loginView.value();
        log.debugf("Redirecting to configured LoginView %s", loginViewId);
        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
        navHandler.handleNavigation(context, "", loginViewId);
        context.renderResponse();
    }

    private void redirectToAccessDeniedView(FacesContext context, UIViewRoot viewRoot) {
        AccessDeniedView accessDeniedView = viewConfigStore.getAnnotationData(viewRoot.getViewId(), AccessDeniedView.class);
        if (accessDeniedView == null || accessDeniedView.value() == null || accessDeniedView.value().isEmpty()) {
            log.debug("Returning 401 response (access denied)");
            context.getExternalContext().setResponseStatus(401);
            context.responseComplete();
            return;
        }
        String accessDeniedViewId = accessDeniedView.value();
        log.debugf("Redirecting to configured AccessDenied %s", accessDeniedViewId);
        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
        navHandler.handleNavigation(context, "", accessDeniedViewId);
        context.renderResponse();
    }
    
    public void observePreNavigateEvent(@Observes PreNavigateEvent event) {
        log.debugf("PreNavigateEvent observed %s, %s", event.getOutcome(), event.getFromAction());
        if (Identity.RESPONSE_LOGIN_SUCCESS.equals(event.getOutcome())
                && "#{identity.login}".equals(event.getFromAction())) {
            FacesContext context = event.getContext();
            if (context.getExternalContext().getSessionMap().get(PRE_LOGIN_VIEW) != null) {
                String oldViewId = (String) context.getExternalContext().getSessionMap().get(PRE_LOGIN_VIEW);
                NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                navHandler.handleNavigation(context, "", oldViewId);
                context.renderResponse();
                context.getExternalContext().getSessionMap().remove(PRE_LOGIN_VIEW);
            }
        }
    }
}
