package org.jboss.seam.faces.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * <p>
 * A producer which retrieves the {@link Flash} for the current request of the JavaServer Faces application by calling
 * {@link FacesContext#getCurrentInstance()} and stores the result as a request-scoped bean instance.
 * </p>
 * 
 * <p>
 * This producer allows the {@link Flash} to be injected:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * Flash flash;
 * </pre>
 * 
 * @author Lincoln Baxter
 */
public class FacesFlashProducer
{
   @Produces
   @RequestScoped
   public Flash getFlash()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (facesContext == null)
      {
         throw new ContextNotActiveException("FacesContext is not active");
      }

      Flash ctx = facesContext.getExternalContext().getFlash();
      if (ctx == null)
      {
         throw new ContextNotActiveException("Flash is not active");
      }

      return ctx;
   }
}
