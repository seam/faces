//$Id: FacesContext.java 5350 2007-06-20 17:53:19Z gavin $
package org.jboss.seam.faces.context;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * <p>A producer which retrieves the current JSF FacesContext by calling
 * {@link FacesContext#getCurrentInstance}, thus allowing it to be
 * injected.</p>
 * 
 * <p>QUESTION should we return null if there is no current phase id? (seems to be a common check)</p>
 * <p>QUESTION is it correct to use a @RequestScoped producer? If it is @Dependent, then a developer could unknowingly bind it to a wider-scoped bean</p>
 * 
 * @author Gavin King
 * @author Dan Allen
 */
public class FacesContextProducer
{
   public
   @Produces
   @RequestScoped
   FacesContext getFacesContext()
   {
      return FacesContext.getCurrentInstance();
   }
}
