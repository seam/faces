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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.SystemEvent;

import org.jboss.seam.faces.event.qualifier.Component;

/**
 * 
 * @author Nicklas Karlsson
 * 
 */
@ApplicationScoped
public class SystemEventObserver
{
   private Map<String, List<SystemEvent>> observations = new HashMap<String, List<SystemEvent>>();

   private void recordObservation(String id, SystemEvent observation)
   {
      List<SystemEvent> observed = observations.get(id);
      if (observed == null)
      {
         observed = new ArrayList<SystemEvent>();
         observations.put(id, observed);
      }
      observed.add(observation);
   }

   public void reset()
   {
      observations.clear();
   }

   public void assertObservations(String id, SystemEvent... observations)
   {
      List<SystemEvent> observed = this.observations.get(id);
      assert observed != null && observed.size() == observations.length;
      assert observed.containsAll(Arrays.asList(observations));
   }

   public void observe(@Observes PostConstructApplicationEvent e)
   {
      recordObservation("1", e);
   }

   public void observe(@Observes PreDestroyApplicationEvent e)
   {
      recordObservation("2", e);
   }

   public void observe(@Observes PostConstructCustomScopeEvent e)
   {
      recordObservation("3", e);
   }

   public void observe(@Observes PreDestroyCustomScopeEvent e)
   {
      recordObservation("4", e);
   }

   public void observe(@Observes ExceptionQueuedEvent e)
   {
      recordObservation("5", e);
   }

   public void observe(@Observes ComponentSystemEvent e)
   {
      recordObservation("6", e);
   }

   public void observe(@Observes PreValidateEvent e)
   {
      recordObservation("7", e);
   }
   
   public void observe2(@Observes @Component("foo") PreValidateEvent e)
   {
      recordObservation("8", e);
   }
   
   public void observe3(@Observes @Component("foo") ComponentSystemEvent e)
   {
      recordObservation("9", e);
   }
   
   public void observe(@Observes PostValidateEvent e)
   {
      recordObservation("10", e);
   }
   
   public void observe2(@Observes @Component("foo") PostValidateEvent e)
   {
      recordObservation("11", e);
   }
   
   public void observe(@Observes PostAddToViewEvent e)
   {
      recordObservation("12", e);
   }
   
   public void observe2(@Observes @Component("foo") PostAddToViewEvent e)
   {
      recordObservation("13", e);
   }   

   public void observe2(@Observes PostConstructViewMapEvent e)
   {
      recordObservation("14", e);
   } 

   public void observe(@Observes PostRestoreStateEvent e)
   {
      recordObservation("15", e);
   }
   
   public void observe2(@Observes @Component("foo") PostRestoreStateEvent e)
   {
      recordObservation("16", e);
   }
   
   public void observe2(@Observes PreDestroyViewMapEvent e)
   {
      recordObservation("17", e);
   }    
   
   public void observe(@Observes PreRemoveFromViewEvent e)
   {
      recordObservation("18", e);
   }
   
   public void observe2(@Observes @Component("foo") PreRemoveFromViewEvent e)
   {
      recordObservation("19", e);
   }
   
   public void observe(@Observes PreRenderComponentEvent e)
   {
      recordObservation("20", e);
   }
   
   public void observe2(@Observes @Component("foo") PreRenderComponentEvent e)
   {
      recordObservation("21", e);
   }     
   
   public void observe(@Observes PreRenderViewEvent e)
   {
      recordObservation("22", e);
   }    
}
