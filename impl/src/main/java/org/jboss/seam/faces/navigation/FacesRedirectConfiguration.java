package org.jboss.seam.faces.navigation;

import javax.enterprise.event.Observes;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.seam.faces.event.PreNavigateEvent;
import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.view.config.ViewConfigStore;

/**
 * Intercept JSF navigations, and check for @FacesRedirect in the @ViewConfig.
 * 
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class FacesRedirectConfiguration {
    @Inject
    private ViewConfigStore store;
    
    public void observePreNavigateEvent(@Observes PreNavigateEvent event) {
        FacesContext facesContext = event.getContext();
        NavigationCase navCase = event.getNavigationCase();
        if (navCase == null) {
            return;
        }
        String viewId = navCase.getToViewId(facesContext);
        FacesRedirect facesRedirect = store.getAnnotationData(viewId, FacesRedirect.class);
        if (facesRedirect == null || facesRedirect.value() == navCase.isRedirect()) {
            return;
        }
        String newOutcome = viewId;
        if (facesRedirect.value()) {
            newOutcome = newOutcome + "?faces-redirect=true";
        }
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, event.getFromAction(), newOutcome);
    }
}
