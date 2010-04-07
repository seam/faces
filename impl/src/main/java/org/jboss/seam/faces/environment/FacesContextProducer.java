/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
   public @Produces @RequestScoped FacesContext getFacesContext()
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      if (ctx == null)
      {
         throw new ContextNotActiveException("FacesContext is not active");
      }
      return ctx;
   }
}
