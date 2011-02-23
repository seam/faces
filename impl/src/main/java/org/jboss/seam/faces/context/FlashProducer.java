package org.jboss.seam.faces.context;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class FlashProducer
{
   @Produces
   @RequestScoped
   public Flash getFlash()
   {
      FacesContext context = FacesContext.getCurrentInstance();
      if (context == null)
      {
         throw new IllegalStateException("FacesContext was null, cannot access Flash outside of the JSF lifecycle.");
      }
      Flash flash = context.getExternalContext().getFlash();
      return flash;
   }
}
