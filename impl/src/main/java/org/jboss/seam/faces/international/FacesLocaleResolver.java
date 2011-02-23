package org.jboss.seam.faces.international;

import java.util.Locale;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.faces.qualifier.Faces;

/**
 * A specialized version of the LocaleProducer that returns the Locale
 * associated with the current UIViewRoot or, if the UIViewRoot has not been
 * established, uses the ViewHandler to calculate the Locale.
 * 
 * @author Dan Allen
 */
public class FacesLocaleResolver // extends LocaleResolver
{
   @Inject
   FacesContext facesContext;

   public boolean isActive()
   {
      return (facesContext != null) && (facesContext.getCurrentPhaseId() != null);
   }

   @Produces
   @Faces
   public Locale getLocale()
   {
      if (facesContext.getViewRoot() != null)
      {
         return facesContext.getViewRoot().getLocale();
      }
      else
      {
         return facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
      }
   }
}
