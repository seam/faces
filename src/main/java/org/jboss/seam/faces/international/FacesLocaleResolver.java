package org.jboss.seam.faces.international;

import java.util.Locale;

import javax.inject.Inject;
import javax.faces.context.FacesContext;

import org.jboss.seam.beans.RuntimeSelected;
import org.jboss.seam.beans.RuntimeSelectedBean;
import org.jboss.seam.international.LocaleResolver;

/**
 * A specialized version of the LocaleProducer that returns
 * the Locale associated with the current UIViewRoot or,
 * if the UIViewRoot has not been established, uses the
 * ViewHandler to calculate the Locale.
 * 
 * @author Dan Allen
 */
public
@RuntimeSelected
class FacesLocaleResolver extends LocaleResolver implements RuntimeSelectedBean
{
   @Inject FacesContext facesContext;
   
   public boolean isActive()
   {
      return facesContext != null && facesContext.getCurrentPhaseId() != null;
   }
   
   @Override
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
