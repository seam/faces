package org.jboss.seam.faces.security;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.PreLoginEvent;
import org.jboss.seam.faces.view.action.PhaseInstant;
import org.jboss.seam.faces.view.action.ViewActionHandler;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.security.events.NotAuthorizedEvent;
import org.jboss.solder.logging.Logger;

public class RestrictViewActionHandlerProvider {

    private final Logger log = Logger.getLogger(RestrictViewActionHandlerProvider.class);
    public static int ORDER = 10;
    private List<ViewActionHandler> viewActionHandlers;
    private List<? extends Annotation> securedAnnotations;
    @Inject
    private BeanManager beanManager;
    @Inject
    private Instance<Identity> identity;
    @Inject
    private ViewConfigStore viewConfigStore;

    public RestrictViewActionHandlerProvider() {
    }

    /**
     * This method must be called just after instanciation.
     * 
     * @param securedAnnotations
     */
    public void initialize(List<? extends Annotation> securedAnnotations, PhaseIdType[] defaultPhases) {
        this.securedAnnotations = securedAnnotations;
        this.viewActionHandlers = viewActionHandlers(defaultPhases);
    }

    private List<ViewActionHandler> viewActionHandlers(PhaseIdType[] defaultPhases) {
        List<ViewActionHandler> viewActionHandlers = new ArrayList<ViewActionHandler>();
        for (PhaseIdType phase : PhaseIdType.values()) {
            List<Annotation> applicableSecurityAnnotations = new ArrayList<Annotation>();
            for (Annotation annotation : securedAnnotations) {
                if (RestrictViewActionUtils.isAnnotationApplicableToPhase(annotation, phase, defaultPhases, beanManager)) {
                    if (applicableSecurityAnnotations == null) { // avoid spawning arrays at all phases of the lifecycle
                        applicableSecurityAnnotations = new ArrayList<Annotation>();
                    }
                    applicableSecurityAnnotations.add(annotation);
                }
            }
            if (applicableSecurityAnnotations.size() > 0) {
                viewActionHandlers.add(new RestrictViewActionHandler(applicableSecurityAnnotations, new PhaseInstant(
                        PhaseIdType.convert(phase), true)));
            }
        }
        return viewActionHandlers;
    }

    public List<ViewActionHandler> getActionHandlers() {
        return viewActionHandlers;
    }

    /**
     * Invokes method on a CDI bean.
     * 
     * Note : copy from Seam Security's SecurityExtension class. Should be extracted into common utility.
     */
    public class RestrictViewActionHandler implements ViewActionHandler {

        private PhaseInstant phaseInstant;
        private List<Annotation> annotations;

        public RestrictViewActionHandler(List<Annotation> securedAnnotations, PhaseInstant phaseInstant) {
            this.phaseInstant = phaseInstant;
            this.annotations = securedAnnotations;
        }

        @Override
        public boolean handles(PhaseInstant phaseInstant) {
            return this.phaseInstant.equals(phaseInstant);
        }

        @Override
        public Integer getOrder() {
            return ORDER;
        }

        @Override
        public Object execute() {
            if (annotations == null || annotations.isEmpty()) {
                log.debug("Annotations is null/empty");
                return null;
            }
            AuthorizationCheckEvent event = new AuthorizationCheckEvent(annotations);
            beanManager.fireEvent(event);
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot = context.getViewRoot();
            if (!event.isPassed()) {
                if (!identity.get().isLoggedIn()) {
                    log.debug("Access denied - not logged in");
                    redirectToLoginPage(context, viewRoot);
                    return null;
                } else {
                    log.debug("Access denied - not authorized");
                    beanManager.fireEvent(new NotAuthorizedEvent());
                    redirectToAccessDeniedView(context, viewRoot);
                    return null;
                }
            } else {
                log.debug("Access granted");
            }
            return null;
        }

        /**
         * Perform the navigation to the @LoginView. If not @LoginView is defined, return a 401 response. The original view id
         * requested by the user is stored in the session map, for use after a successful login.
         * 
         * @param context
         * @param viewRoot
         */
        private void redirectToLoginPage(FacesContext context, UIViewRoot viewRoot) {
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            beanManager.fireEvent(new PreLoginEvent(context, sessionMap));
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

        /**
         * Perform the navigation to the @AccessDeniedView. If not @AccessDeniedView is defined, return a 401 response
         * 
         * @param context
         * @param viewRoot
         */
        private void redirectToAccessDeniedView(FacesContext context, UIViewRoot viewRoot) {
            // If a user has already done a redirect and rendered the response (possibly in an observer) we cannot do this
            // output
            if (!(context.getResponseComplete() || context.getRenderResponse())) {
                AccessDeniedView accessDeniedView = viewConfigStore.getAnnotationData(viewRoot.getViewId(),
                        AccessDeniedView.class);
                if (accessDeniedView == null || accessDeniedView.value() == null || accessDeniedView.value().isEmpty()) {
                    log.warn("No AccessDeniedView is configured, returning 401 response (access denied). Please configure an AccessDeniedView in the ViewConfig.");
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
        }
    }

}
