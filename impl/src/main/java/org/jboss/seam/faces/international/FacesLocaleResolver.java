/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.faces.international;

import java.util.Locale;
import javax.enterprise.inject.Produces;

import javax.inject.Inject;
import javax.faces.context.FacesContext;

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
      return facesContext != null && facesContext.getCurrentPhaseId() != null;
   }

   @Produces
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
