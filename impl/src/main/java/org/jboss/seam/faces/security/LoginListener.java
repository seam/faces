package org.jboss.seam.faces.security;

import java.io.IOException;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.jboss.seam.faces.event.PostLoginEvent;
import org.jboss.seam.faces.event.PreLoginEvent;

/**
 * Listen for {@link PreLoginEvent} and {@link PostLoginEvent} events, storing the viewId requested pre login,
 * and re-instating the navigation post login.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class LoginListener {

    private static final String PRE_LOGIN_URL = LoginListener.class.getName() + "_PRE_LOGIN_URL";

    public void observePreLoginEvent(@Observes PreLoginEvent event) {
        if (event.getFacesContext().getExternalContext().getRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) event.getFacesContext().getExternalContext().getRequest();
            StringBuffer sb = request.getRequestURL();
            // build the querystring out of the request parameters, because Reqeust#getQueryString is often null
            Map<String,String> requestParameterMap = event.getFacesContext().getExternalContext().getRequestParameterMap();
            if (requestParameterMap != null) {
                boolean first = true;
                for (Map.Entry<String, String> entry : requestParameterMap.entrySet()) {
                    if (first) {
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            String requestedUrl = sb.toString();
            event.getSessionMap().put(PRE_LOGIN_URL, requestedUrl);
        }
    }

    public void observePostLoginEvent(@Observes PostLoginEvent event) {
        FacesContext context = event.getFacesContext();
        if (context.getExternalContext().getSessionMap().get(PRE_LOGIN_URL) != null) {
            String oldUrl = (String) context.getExternalContext().getSessionMap().get(PRE_LOGIN_URL);
            context.getExternalContext().getSessionMap().remove(PRE_LOGIN_URL);
            try {
                event.getFacesContext().getExternalContext().redirect(oldUrl);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
