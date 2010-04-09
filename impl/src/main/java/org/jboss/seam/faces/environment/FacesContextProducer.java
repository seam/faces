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
package org.jboss.seam.faces.environment;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A producer which retrieves the {@link FacesContext} for the current request
 * of the JavaServer Faces application by calling
 * {@link FacesContext#getCurrentInstance()} and stores the result as a
 * request-scoped bean instance.
 * </p>
 * 
 * <p>
 * This producer allows the {@link FacesContext} to be injected:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * FacesContext ctx;
 * </pre>
 * 
 * <p>
 * QUESTION is it correct to use a @RequestScoped producer? If it is @Dependent,
 * then a developer could unknowingly bind it to a wider-scoped bean
 * </p>
 * 
 * @author Gavin King
 * @author Dan Allen
 */
public class FacesContextProducer
{
   @Produces
   @RequestScoped
   public FacesContext getFacesContext()
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      if (ctx == null)
      {
         throw new ContextNotActiveException("FacesContext is not active");
      }
      return ctx;
   }
}
