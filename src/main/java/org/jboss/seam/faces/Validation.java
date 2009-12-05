package org.jboss.seam.faces;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;

import org.jboss.seam.faces.events.ValidationFailedEvent;

/**
 * Allows the application to determine whether the JSF validation
 * phase completed successfully, or if a validation failure
 * occurred. This functionality is actually provided by JSF 2 now.
 * What this class does is raise a {@link ValidationFailedEvent}
 * so that observers can react accordingly.
 * 
 * @author Gavin king
 */
@Named
public class Validation
{
   private boolean succeeded;
   private boolean failed;
   
   @Inject BeanManager manager;
   
   public void afterProcessValidations(FacesContext facesContext)
   {
      failed = facesContext.isValidationFailed();
	   if (failed)
      {
         manager.fireEvent(new ValidationFailedEvent());
      }
      succeeded = !failed;
   }

   public boolean isSucceeded()
   {
      return succeeded;
   }

   public boolean isFailed()
   {
      return failed;
   }

   public void fail()
   {
      failed = true;
      succeeded = false;
   }
}
