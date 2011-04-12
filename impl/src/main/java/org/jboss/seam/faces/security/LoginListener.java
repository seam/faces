package org.jboss.seam.faces.security;

import javax.enterprise.event.Observes;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import org.jboss.seam.faces.event.PostLoginEvent;
import org.jboss.seam.faces.event.PreLoginEvent;

/**
 * Listen for {@link PreLoginEvent} and {@link PostLoginEvent} events, storing the viewId requested pre login,
 * and re-instating the navigation post login.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class LoginListener {

    private static final String PRE_LOGIN_VIEW = LoginListener.class.getName() + "_PRE_LOGIN_VIEW";

    public void observePreLoginEvent(@Observes PreLoginEvent event) {
        String viewId = event.getFacesContext().getViewRoot().getViewId();
        event.getSessionMap().put(PRE_LOGIN_VIEW, viewId);
    }

    public void observePostLoginEvent(@Observes PostLoginEvent event) {
        FacesContext context = event.getFacesContext();
        if (context.getExternalContext().getSessionMap().get(PRE_LOGIN_VIEW) != null) {
            String oldViewId = (String) context.getExternalContext().getSessionMap().get(PRE_LOGIN_VIEW);
            NavigationHandler navHandler = context.getApplication().getNavigationHandler();
            navHandler.handleNavigation(context, "", oldViewId);
            context.renderResponse();
            context.getExternalContext().getSessionMap().remove(PRE_LOGIN_VIEW);
        }
    }
}
