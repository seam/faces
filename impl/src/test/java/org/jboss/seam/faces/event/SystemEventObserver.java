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
package org.jboss.seam.faces.event;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;

import org.jboss.seam.faces.event.qualifier.Component;

/**
 * 
 * @author Nicklas Karlsson
 * 
 */
@ApplicationScoped
public class SystemEventObserver
{
   public static boolean componentSystemEvent;
   public static boolean excecptionQueuedEvent;
   public static boolean postConstructApplicationEvent;
   public static boolean postConstructCustomScopeEvent;
   public static boolean preDestroyApplicationEvent;
   public static boolean preDestroyCustomScopeEvent;
   public static boolean specificComponentValidationEvent;

   public void observeSpecificComponentValidation(@Observes @Component("foo") PostValidateEvent e)
   {
      specificComponentValidationEvent = true;
   }

   public void observeComponentSystemEvent(@Observes ComponentSystemEvent e)
   {
      componentSystemEvent = true;
   }

   public void observeExceptionQueuedEvent(@Observes ExceptionQueuedEvent e)
   {
      excecptionQueuedEvent = true;
   }

   public void observePostConstructApplicationEvent(@Observes PostConstructApplicationEvent e)
   {
      postConstructApplicationEvent = true;
   }

   public void observePreDestroyApplicationEvent(@Observes PreDestroyApplicationEvent e)
   {
      preDestroyApplicationEvent = true;
   }

   public void observePostConstructCustomScopeEvent(@Observes PostConstructCustomScopeEvent e)
   {
      postConstructCustomScopeEvent = true;
   }

   public void observePreDestroyCustomScopeEvent(@Observes PreDestroyCustomScopeEvent e)
   {
      preDestroyCustomScopeEvent = true;
   }
}
