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
package org.jboss.seam.faces;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.seam.faces.event.PhaseEventBridge;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PhaseTestBase
{
   @Inject
   PhaseEventBridge phaseEventBridge;

   protected MockFacesContext facesContext = new MockFacesContext();
   protected final MockLifecycle lifecycle = new MockLifecycle();

   static List<PhaseId> ALL_PHASES = new ArrayList<PhaseId>()
   {
      private static final long serialVersionUID = 1L;

      {
         add(PhaseId.APPLY_REQUEST_VALUES);
         add(PhaseId.INVOKE_APPLICATION);
         add(PhaseId.PROCESS_VALIDATIONS);
         add(PhaseId.RENDER_RESPONSE);
         add(PhaseId.RESTORE_VIEW);
         add(PhaseId.UPDATE_MODEL_VALUES);
      }
   };

   protected void fireAllPhases()
   {
      fireAllBeforePhases();
      fireAllAfterPhases();
   }

   protected void fireAllBeforePhases()
   {
      fireBeforePhases(ALL_PHASES);
   }

   protected void fireBeforePhases(final List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireBeforePhase(phaseId);
      }
   }

   protected void fireBeforePhase(final PhaseId phaseId)
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }

   protected void fireAllAfterPhases()
   {
      fireAfterPhases(ALL_PHASES);
   }

   protected void fireAfterPhases(final List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireAfterPhase(phaseId);
      }
   }

   protected void fireAfterPhase(final PhaseId phaseId)
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }
}
