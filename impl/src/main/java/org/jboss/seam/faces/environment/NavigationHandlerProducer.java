package org.jboss.seam.faces.environment;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * <p>
 * A producer which retrieves the current JSF NavigationHandler by calling {@link Application#getNavigationHandler()}, thus
 * allowing it to be injected.
 * </p>
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class NavigationHandlerProducer {
    @Inject
    FacesContext context;

    @Produces
    @RequestScoped
    public NavigationHandler getNavigationHandler() {
        if (context != null) {
            Application application = context.getApplication();
            if (application != null) {
                return application.getNavigationHandler();
            }
        }
        return null;
    }
}
