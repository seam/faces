//$Id: FacesContext.java 5350 2007-06-20 17:53:19Z gavin $
package org.jboss.seam.faces.environment;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * <p>
 * A producer which retrieves the current JSF ExternalContext by calling
 * {@link FacesContext#getCurrentInstance#getExternalContext()}, thus allowing
 * it to be injected.
 * </p>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ExternalContextProducer
{
   @Inject
   FacesContext context;

   public @Produces
   @RequestScoped
   ExternalContext getExternalContext()
   {
      if (context != null)
      {
         return context.getExternalContext();
      }
      return null;
   }
}
