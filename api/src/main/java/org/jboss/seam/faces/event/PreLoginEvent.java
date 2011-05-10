package org.jboss.seam.faces.event;

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * An event fired when the {@link SecurityPhaseListener} is about to redirect to the login page.  The session map is
 * provided as a place to store values to be retrieved during the {@link PostLoginEvent}.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class PreLoginEvent {
    private final FacesContext context;
    private final Map<String, Object> sessionMap;

    public PreLoginEvent(FacesContext context, Map<String, Object> sessionMap) {
        this.context = context;
        this.sessionMap = sessionMap;
    }

    public FacesContext getFacesContext() {
        return context;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }
}
