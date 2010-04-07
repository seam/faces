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
package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.jboss.seam.faces.cdi.BeanManagerAware;

/**
 * Provide CDI injection to SystemEventListener artifacts by delegating through
 * this class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class DelegatingSystemEventListener extends BeanManagerAware implements SystemEventListener
{

   public boolean isListenerForSource(final Object source)
   {
      return true;
   }

   public void processEvent(final SystemEvent event) throws AbortProcessingException
   {
      for (SystemEventListener l : getEventListeners())
      {
         if (l.isListenerForSource(event.getSource()))
         {
            l.processEvent(event);
         }
      }
   }

   @SuppressWarnings("unchecked")
   private List<SystemEventListener> getEventListeners()
   {
      BeanManager manager = getBeanManager();
      List<SystemEventListener> result = new ArrayList<SystemEventListener>();

      Bean<SystemEventBridge> bean = (Bean<SystemEventBridge>) manager.getBeans(SystemEventBridge.class).iterator().next();
      CreationalContext<SystemEventBridge> context = manager.createCreationalContext(bean);
      SystemEventBridge listener = (SystemEventBridge) manager.getReference(bean, SystemEventBridge.class, context);

      result.add(listener);

      return result;
   }

}
