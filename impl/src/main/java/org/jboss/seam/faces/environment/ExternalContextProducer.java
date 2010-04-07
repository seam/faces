/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A producer which retrieves the {@link ExternalContext} for the current
 * request of the JavaServer Faces application by calling
 * {@link FacesContext#getExternalContext()} and stores the result as a
 * request-scoped bean instance.
 * </p>
 * 
 * <p>
 * This producer allows the {@link ExternalContext} to be injected:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * ExternalContext ctx;
 * </pre>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Dan Allen
 */
public class ExternalContextProducer
{
   public @Produces @RequestScoped ExternalContext getExternalContext(final FacesContext context)
   {
      return context.getExternalContext();
   }
}
