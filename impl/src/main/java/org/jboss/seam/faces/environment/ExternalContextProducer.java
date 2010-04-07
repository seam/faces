//$Id: FacesContext.java 5350 2007-06-20 17:53:19Z gavin $
package org.jboss.seam.faces.environment;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A producer which retrieves the {@link ExternalContext} for the current
 * request of the JavaServer Faces application by calling
 * {@link FacesContext#getExternalContext()} and stores the result as a
 * request-scoped bean instance.
 * </p>
 * 
 * <p>
 * This producer allows the {@link ExternalContext} to be injected:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * ExternalContext ctx;
 * </pre>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Dan Allen
 */
public class ExternalContextProducer
{
   public @Produces @RequestScoped ExternalContext getExternalContext(final FacesContext context)
   {
      return context.getExternalContext();
   }
}
