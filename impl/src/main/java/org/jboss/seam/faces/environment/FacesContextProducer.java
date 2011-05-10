package org.jboss.seam.faces.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A producer which retrieves the {@link FacesContext} for the current request of the JavaServer Faces application by calling
 * {@link FacesContext#getCurrentInstance()} and stores the result as a request-scoped bean instance.
 * </p>
 * <p/>
 * <p>
 * This producer allows the {@link FacesContext} to be injected:
 * </p>
 * <p/>
 * <pre>
 * &#064;Inject
 * FacesContext ctx;
 * </pre>
 * <p/>
 * <p>
 * QUESTION is it correct to use a @RequestScoped producer? If it is @Dependent, then a developer could unknowingly bind it to a
 * wider-scoped bean
 * </p>
 *
 * @author Gavin King
 * @author Dan Allen
 */
public class FacesContextProducer {
    @Produces
    @RequestScoped
    public FacesContext getFacesContext() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) {
            throw new ContextNotActiveException("FacesContext is not active");
        }
        return ctx;
    }
}
