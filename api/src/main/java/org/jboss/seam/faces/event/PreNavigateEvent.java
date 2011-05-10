package org.jboss.seam.faces.event;

import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * An event that is fired when JSF navigation is invoked via the {@link NavigationHandler}, but before any redirecting or
 * non-redirecting navigation is completed, giving consumers of this event a chance to take action.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class PreNavigateEvent {

    private final FacesContext context;
    private final String fromAction;
    private final String outcome;
    private final NavigationCase navigationCase;

    public PreNavigateEvent(final FacesContext context, final String fromAction, final String outcome,
                            final NavigationCase navigationCase) {
        this.context = context;
        this.fromAction = fromAction;
        this.outcome = outcome;
        this.navigationCase = navigationCase;
    }

    public FacesContext getContext() {
        return context;
    }

    public String getFromAction() {
        return fromAction;
    }

    public String getOutcome() {
        return outcome;
    }

    public NavigationCase getNavigationCase() {
        return navigationCase;
    }

}
