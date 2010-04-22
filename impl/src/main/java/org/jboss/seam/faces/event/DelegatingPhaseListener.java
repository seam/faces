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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.weld.extensions.beanManager.BeanManagerAware;

/**
 * Provide CDI injection to PhaseListener artifacts by delegating through this
 * class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class DelegatingPhaseListener extends BeanManagerAware implements PhaseListener
{
   private static final long serialVersionUID = 8454616175394888259L;

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   public void beforePhase(final PhaseEvent event)
   {
      for (PhaseListener listener : getPhaseListeners())
      {
         if (shouldProcessPhase(listener, event))
         {
            listener.beforePhase(event);
         }
      }
   }

   public void afterPhase(final PhaseEvent event)
   {
      for (PhaseListener listener : getPhaseListeners())
      {
         if (shouldProcessPhase(listener, event))
         {
            listener.afterPhase(event);
         }
      }
   }

   /**
    * Determine if the {@link PhaseListener} should process the given
    * {@link PhaseEvent}.
    */
   private boolean shouldProcessPhase(final PhaseListener listener, final PhaseEvent event)
   {
      return (PhaseId.ANY_PHASE.equals(listener.getPhaseId()) || event.getPhaseId().equals(listener.getPhaseId()));
   }

   @SuppressWarnings("unchecked")
   private List<PhaseListener> getPhaseListeners()
   {
      BeanManager manager = getBeanManager();
      List<PhaseListener> result = new ArrayList<PhaseListener>();

      Bean<PhaseEventBridge> bean = (Bean<PhaseEventBridge>) manager.getBeans(PhaseEventBridge.class).iterator().next();
      CreationalContext<PhaseEventBridge> context = manager.createCreationalContext(bean);
      PhaseEventBridge listener = (PhaseEventBridge) manager.getReference(bean, PhaseEventBridge.class, context);

      result.add(listener);

      return result;
   }

}
