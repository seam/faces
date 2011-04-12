package org.jboss.seam.faces.event;

import java.util.Map;
import javax.faces.context.FacesContext;

/**
 * An event fired after a successful authentication.  The session map is provided to retrieve values stored during
 * the {@link PostLoginEvent}.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class PostLoginEvent {
    private final FacesContext context;
    private final Map<String,Object> sessionMap;

    public PostLoginEvent(FacesContext context, Map<String, Object> sessionMap) {
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