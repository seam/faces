package org.jboss.seam.faces.security;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.event.PostLoginEvent;
import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.view.action.NonContextual;
import org.jboss.seam.faces.view.action.NonContextual.Instance;
import org.jboss.seam.faces.view.action.ViewActionHandler;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.SecurityBindingType;
import org.jboss.solder.logging.Logger;

public class RestrictViewActionExtension implements Extension {
    private final Logger log = Logger.getLogger(RestrictViewActionExtension.class);
    private ViewConfigStore viewConfigStore;
    private BeanManager beanManager;
    private List<Instance<RestrictViewActionHandlerProvider>> nonContextualObjects = new ArrayList<Instance<RestrictViewActionHandlerProvider>>();

    public void setup(@Observes AfterDeploymentValidation event, javax.enterprise.inject.Instance<ViewConfigStore> viewConfigStore, BeanManager beanManager) {
        this.beanManager = beanManager;
        if (viewConfigStore.isUnsatisfied()) {
            // extension disabled : surely because we're in come UT context (i.e. ProjectStageExtensionTest)
            // not adding ViewConfigStore in ShrinkWrap archive
            log.warn("ViewConfigStore dependency missing - RestrictViewActionExtension disabled");
            return;
        }
        this.viewConfigStore = viewConfigStore.get();
        registerViewActions();
    }

    private void registerViewActions() {
        List<ViewConfigDescriptor> viewConfigDescriptors = viewConfigStore.getViewConfigDescriptors();
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigDescriptors) {
            List<? extends Annotation> allSecurityAnnotations = viewConfigDescriptor
                    .getAllQualifierData(SecurityBindingType.class);
            RestrictViewActionHandlerProvider provider = newViewActionProvider(allSecurityAnnotations,
                    RestrictViewActionUtils.getDefaultPhases(viewConfigDescriptor.getViewId(), viewConfigStore));
            for (ViewActionHandler viewActionHandler : provider.getActionHandlers()) {
                viewConfigDescriptor.addViewActionHandler(viewActionHandler);
            }
        }
    }

    /**
     * Monitor PreNavigationEvents, looking for a successful navigation from the Seam Security login button. When such a
     * navigation is encountered, redirect to the the viewId captured before the login redirect was triggered.
     * 
     * @param event
     */
    public void observePreNavigateEvent(@Observes PreNavigateEvent event) {
        log.debugf("PreNavigateEvent observed %s, %s", event.getOutcome(), event.getFromAction());
        if (Identity.RESPONSE_LOGIN_SUCCESS.equals(event.getOutcome()) && "#{identity.login}".equals(event.getFromAction())) {
            FacesContext context = event.getContext();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            beanManager.fireEvent(new PostLoginEvent(context, sessionMap));
        }
    }

    /**
     * Creates a viewActionProvider which can be injected using CDI annotations.
     * 
     * @param annotation
     * @param providerClazz
     * @return
     */
    private RestrictViewActionHandlerProvider newViewActionProvider(List<? extends Annotation> securedAnnotations,
            PhaseIdType[] defaultPhases) {
        NonContextual<RestrictViewActionHandlerProvider> nonContextual = new NonContextual<RestrictViewActionHandlerProvider>(
                beanManager, RestrictViewActionHandlerProvider.class);
        Instance<RestrictViewActionHandlerProvider> instance = nonContextual.newInstance();
        instance.produce();
        instance.inject();
        instance.postConstruct();
        nonContextualObjects.add(instance);
        RestrictViewActionHandlerProvider provider = instance.get();
        provider.initialize(securedAnnotations, defaultPhases);
        return provider;
    }
}
