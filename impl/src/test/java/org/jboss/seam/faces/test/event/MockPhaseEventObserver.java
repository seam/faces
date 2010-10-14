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
package org.jboss.seam.faces.test.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

/**
 * 
 * @author Nicklas Karlsson
 * 
 */
@ApplicationScoped
public class MockPhaseEventObserver
{
   private Map<String, List<PhaseId>> observations = new HashMap<String, List<PhaseId>>();

   private void recordObservation(String id, PhaseId observation)
   {
      List<PhaseId> observed = observations.get(id);
      if (observed == null)
      {
         observed = new ArrayList<PhaseId>();
         observations.put(id, observed);
      }
      observed.add(observation);
   }

   public void reset()
   {
      observations.clear();
   }

   public void assertObservations(String id, PhaseId... observations)
   {
      List<PhaseId> observed = this.observations.get(id);
      assert observed != null && observed.size() == observations.length;
      assert observed.containsAll(Arrays.asList(observations));
   }

   public void observeBeforeRenderResponse(@Observes @Before @RenderResponse final PhaseEvent e)
   {
      recordObservation("1", e.getPhaseId());
   }

   public void observeAfterRenderResponse(@Observes @After @RenderResponse final PhaseEvent e)
   {
      recordObservation("2", e.getPhaseId());
   }

   public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues final PhaseEvent e)
   {
      recordObservation("3", e.getPhaseId());
   }

   public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues final PhaseEvent e)
   {
      recordObservation("4", e.getPhaseId());
   }

   public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication final PhaseEvent e)
   {
      recordObservation("5", e.getPhaseId());
   }

   public void observeAfterInvokeApplication(@Observes @After @InvokeApplication final PhaseEvent e)
   {
      recordObservation("6", e.getPhaseId());
   }

   public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations final PhaseEvent e)
   {
      recordObservation("7", e.getPhaseId());
   }

   public void observeAfterProcessValidations(@Observes @After @ProcessValidations final PhaseEvent e)
   {
      recordObservation("8", e.getPhaseId());
   }

   public void observeBeforeRestoreView(@Observes @Before @RestoreView final PhaseEvent e)
   {
      recordObservation("9", e.getPhaseId());
   }

   public void observeAfterRestoreView(@Observes @After @RestoreView final PhaseEvent e)
   {
      recordObservation("10", e.getPhaseId());
   }

   public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues final PhaseEvent e)
   {
      recordObservation("11", e.getPhaseId());
   }

   public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues final PhaseEvent e)
   {
      recordObservation("12", e.getPhaseId());
   }

   public void observeAllRenderResponse(@Observes @RenderResponse final PhaseEvent e)
   {
      recordObservation("13", e.getPhaseId());
   }

   public void observeAllApplyRequestValues(@Observes @ApplyRequestValues final PhaseEvent e)
   {
      recordObservation("14", e.getPhaseId());
   }

   public void observeAllInvokeApplication(@Observes @InvokeApplication final PhaseEvent e)
   {
      recordObservation("15", e.getPhaseId());
   }

   public void observeAllProcessValidations(@Observes @ProcessValidations final PhaseEvent e)
   {
      recordObservation("16", e.getPhaseId());
   }

   public void observeAllRestoreView(@Observes @RestoreView final PhaseEvent e)
   {
      recordObservation("17", e.getPhaseId());
   }

   public void observeAllUpdateModelValues(@Observes @UpdateModelValues final PhaseEvent e)
   {
      recordObservation("18", e.getPhaseId());
   }

   public void observeAllBeforeEvents(@Observes @Before final PhaseEvent e)
   {
      recordObservation("19", e.getPhaseId());
   }

   public void observeAllAfterEvents(@Observes @Before final PhaseEvent e)
   {
      recordObservation("20", e.getPhaseId());
   }

   public void observeAllEvents(@Observes final PhaseEvent e)
   {
      recordObservation("21", e.getPhaseId());
   }

}
