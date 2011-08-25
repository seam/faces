/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.PostLoginEvent;
import org.jboss.seam.faces.event.PreLoginEvent;
import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.logging.Logger;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.seam.security.events.AuthorizationCheckEvent;
import org.jboss.seam.security.events.NotAuthorizedEvent;
import org.jboss.seam.solder.core.Requires;
import org.jboss.seam.solder.reflection.AnnotationInspector;

/**
 * Use the annotations stored in the ViewConfigStore to restrict view access.
 * Authorization is delegated to Seam Security through by firing a AuthorizationCheckEvent.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Requires("org.jboss.seam.security.extension.SecurityExtension")
public class SecurityPhaseListener {

    private transient final Logger log = Logger.getLogger(SecurityPhaseListener.class);

    @Inject
    private ViewConfigStore viewConfigStore;
    @Inject
    private Event<AuthorizationCheckEvent> authorizationCheckEvent;
    @Inject
    private Event<PreLoginEvent> preLoginEvent;
    @Inject
    private Event<PostLoginEvent> postLoginEvent;
    @Inject
    private Event<NotAuthorizedEvent> notAuthorizedEventEvent;
    @Inject
    private BeanManager beanManager;
    @Inject
    private Identity identity;

    /**
     * Enforce any security annotations applicable to the RestoreView phase
     *
     * @param event
     */
    public void observeRestoreView(@Observes @After @RestoreView PhaseEvent event) {
        log.debug("After Restore View event");
        performObservation(event, PhaseIdType.RESTORE_VIEW);
    }

    /**
     * Enforce any security annotations applicable to the ApplyRequestValues phase
     *
     * @param event
     */
    public void observeApplyRequestValues(@Observes @Before @ApplyRequestValues PhaseEvent event) {
        log.debug("After Apply Request Values event");
        performObservation(event, PhaseIdType.APPLY_REQUEST_VALUES);
    }

    /**
     * Enforce any security annotations applicable to the ProcessValidations phase
     *
     * @param event
     */
    public void observeProcessValidations(@Observes @Before @ProcessValidations PhaseEvent event) {
        log.debug("After Process Validations event");
        performObservation(event, PhaseIdType.PROCESS_VALIDATIONS);
    }

    /**
     * Enforce any security annotations applicable to the UpdateModelValues phase
     *
     * @param event
     */
    public void observeUpdateModelValues(@Observes @Before @UpdateModelValues PhaseEvent event) {
        log.debug("After Update Model Values event");
        performObservation(event, PhaseIdType.UPDATE_MODEL_VALUES);
    }

    /**
     * Enforce any security annotations applicable to the InvokeApplication phase
     *
     * @param event
     */
    public void observeInvokeApplication(@Observes @Before @InvokeApplication PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.INVOKE_APPLICATION);
    }

    /**
     * Enforce any security annotations applicable to the RenderResponse phase
     *
     * @param event
     */
    public void observeRenderResponse(@Observes @Before @RenderResponse PhaseEvent event) {
        log.debug("Before Render Response event");
        performObservation(event, PhaseIdType.RENDER_RESPONSE);
    }

    /**
     * Inspect the annotations in the ViewConfigStore, enforcing any restrictions applicable to this phase
     *
     * @param event
     * @param phaseIdType
     */
    private void performObservation(PhaseEvent event, PhaseIdType phaseIdType) {
        UIViewRoot viewRoot = (UIViewRoot) event.getFacesContext().getViewRoot();
        List<? extends Annotation> restrictionsForPhase = getRestrictionsForPhase(phaseIdType, viewRoot.getViewId());
        if (restrictionsForPhase != null) {
            log.debugf("Enforcing on phase %s", phaseIdType);
            enforce(event.getFacesContext(), viewRoot, restrictionsForPhase);
        }
    }

    /**
     * Retrieve all annotations from the ViewConfigStore for a given a JSF phase, and a view id,
     * and where the annotation is qualified by @SecurityBindingType
     *
     * @param currentPhase
     * @param viewId
     * @return list of restrictions applicable to this viewId and PhaseTypeId
     */
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

    /**
     * Inspect an annotation to see if it specifies a view in which it should be.  Fall back on default view otherwise.
     *
     * @param annotation
     * @param currentPhase
     * @param defaultPhases
     * @return true if the annotation is applicable to this view and phase, false otherwise
     */
    public boolean isAnnotationApplicableToPhase(Annotation annotation, PhaseIdType currentPhase, PhaseIdType[] defaultPhases) {
        Method restrictAtViewMethod = getRestrictAtViewMethod(annotation);
        PhaseIdType[] phasedIds = null;
        if (restrictAtViewMethod != null) {
            log.warnf("Annotation %s is using the restrictAtViewMethod. Use a @RestrictAtPhase qualifier on the annotation instead.");
            phasedIds = getRestrictedPhaseIds(restrictAtViewMethod, annotation);
        }
        RestrictAtPhase restrictAtPhaseQualifier = AnnotationInspector.getAnnotation(annotation.annotationType(), RestrictAtPhase.class, beanManager);
        if (restrictAtPhaseQualifier != null) {
            log.debug("Using Phases found in @RestrictAtView qualifier on the annotation.");
            phasedIds = restrictAtPhaseQualifier.value();
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

    /**
     * Get the default phases at which restrictions should be applied, by looking for a @RestrictAtPhase on a matching
     *
     * @param viewId
     * @return default phases for a view
     * @ViewPattern, falling back on global defaults if none are found
     */
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

    /**
     * Utility method to extract the "restrictAtPhase" method from an annotation
     *
     * @param annotation
     * @return restrictAtViewMethod if found, null otherwise
     */
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

    /**
     * Retrieve the default PhaseIdTypes defined by the restrictAtViewMethod in the annotation
     *
     * @param restrictAtViewMethod
     * @param annotation
     * @return PhaseIdTypes from the restrictAtViewMethod, null if empty
     */
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

    /**
     * Enforce the list of applicable annotations, by firing an AuthorizationCheckEvent.  The event is then inspected to
     * determine if access is allowed.  Faces navigation is then re-routed to the @LoginView if the user is not logged in,
     * otherwise to the @AccessDenied view.
     *
     * @param context
     * @param viewRoot
     * @param annotations
     */
    private void enforce(FacesContext context, UIViewRoot viewRoot, List<? extends Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            log.debug("Annotations is null/empty");
            return;
        }
        AuthorizationCheckEvent event = new AuthorizationCheckEvent(annotations);
        authorizationCheckEvent.fire(event);
        if (!event.isPassed()) {
            if (!identity.isLoggedIn()) {
                log.debug("Access denied - not logged in");
                redirectToLoginPage(context, viewRoot);
                return;
            } else {
                log.debug("Access denied - not authorized");
                notAuthorizedEventEvent.fire(new NotAuthorizedEvent());
                redirectToAccessDeniedView(context, viewRoot);
                return;
            }
        } else {
            log.debug("Access granted");
        }
    }

    /**
     * Perform the navigation to the @LoginView.  If not @LoginView is defined, return a 401 response.
     * The original view id requested by the user is stored in the session map, for use after a successful login.
     *
     * @param context
     * @param viewRoot
     */
    private void redirectToLoginPage(FacesContext context, UIViewRoot viewRoot) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        preLoginEvent.fire(new PreLoginEvent(context, sessionMap));
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
     * Perform the navigation to the @AccessDeniedView.  If not @AccessDeniedView is defined, return a 401 response
     *
     * @param context
     * @param viewRoot
     */
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

    /**
     * Monitor PreNavigationEvents, looking for a successful navigation from the Seam Security login button.  When such a
     * navigation is encountered, redirect to the the viewId captured before the login redirect was triggered.
     *
     * @param event
     */
    public void observePreNavigateEvent(@Observes PreNavigateEvent event) {
        log.debugf("PreNavigateEvent observed %s, %s", event.getOutcome(), event.getFromAction());
        if (Identity.RESPONSE_LOGIN_SUCCESS.equals(event.getOutcome())
                && "#{identity.login}".equals(event.getFromAction())) {
            FacesContext context = event.getContext();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            postLoginEvent.fire(new PostLoginEvent(context, sessionMap));
        }
    }
}
