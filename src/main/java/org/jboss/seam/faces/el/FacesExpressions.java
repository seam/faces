//$Id: FacesExpressions.java 9684 2008-12-01 21:41:20Z dan.j.allen $
package org.jboss.seam.faces.el;

import javax.el.ELContext;
import javax.inject.Inject;
import javax.faces.context.FacesContext;

import org.jboss.seam.beans.RuntimeSelected;
import org.jboss.seam.beans.RuntimeSelectedBean;
import org.jboss.seam.el.Expressions;

/**
 * Factory for method and value bindings in a JSF environment.
 * 
 * @author Gavin King
 * @author Dan Allen
 */
public
@RuntimeSelected
class FacesExpressions extends Expressions implements RuntimeSelectedBean
{
   @Inject FacesContext facesContext;
   
   public boolean isActive()
   {
      return facesContext != null && facesContext.getCurrentPhaseId() != null;
   }

   /**
    * @return the JSF ELContext
    */
   @Override
   public ELContext getELContext()
   {
      return facesContext.getELContext();
   }

}