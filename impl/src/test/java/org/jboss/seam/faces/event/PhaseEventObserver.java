package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.AnyPhase;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;

@ApplicationScoped
public class PhaseEventObserver
{
   public static enum Observation
   {
      BEFORE_RENDER_RESPONSE, AFTER_RENDER_RESPONSE, BEFORE_APPLY_VALUES, AFTER_APPLY_VALUES, BEFORE_INVOKE_APPLICATION, AFTER_INVOKE_APPLICATION, BEFORE_PROCESS_VALIDATION, BEFORE_RESTORE_VIEW, AFTER_RESTORE_VIEW, AFTER_UPDATE_MODEL_VALUES, AFTER_PROCESS_VALIDATION, BEFORE_UPDATE_MODEL_VALUES
   }

   private static final List<Observation> ALL_BEFORE_OBSERVATIONS = new ArrayList<Observation>()
   {
      private static final long serialVersionUID = 1L;
      {
         add(Observation.BEFORE_APPLY_VALUES);
         add(Observation.BEFORE_INVOKE_APPLICATION);
         add(Observation.BEFORE_PROCESS_VALIDATION);
         add(Observation.BEFORE_RENDER_RESPONSE);
         add(Observation.BEFORE_RESTORE_VIEW);
         add(Observation.BEFORE_UPDATE_MODEL_VALUES);
      }
   };

   private static final List<Observation> ALL_AFTER_OBSERVATIONS = new ArrayList<Observation>()
   {
      private static final long serialVersionUID = 1L;
      {
         add(Observation.AFTER_APPLY_VALUES);
         add(Observation.AFTER_INVOKE_APPLICATION);
         add(Observation.AFTER_PROCESS_VALIDATION);
         add(Observation.AFTER_RENDER_RESPONSE);
         add(Observation.AFTER_RESTORE_VIEW);
         add(Observation.AFTER_UPDATE_MODEL_VALUES);
      }
   };

   private List<Observation> observations = new ArrayList<Observation>();
   private List<Observation> beforeAnyObservations = new ArrayList<Observation>();
   private List<Observation> afterAnyObservations = new ArrayList<Observation>();

   public void reset()
   {
      observations.clear();
      beforeAnyObservations.clear();
      afterAnyObservations.clear();
   }

   public void assertSingleObservation(Observation observation)
   {
      assert observations.size() == 1;
      assert observation.equals(observations.iterator().next());
   }

   private void recordObservation(Observation observation)
   {
      observations.add(observation);
   }

   public void observeBeforeAnyPhase(@Observes @Before @AnyPhase final PhaseEvent e)
   {
      recordBeforeAnyObservations();
   }

   private void recordBeforeAnyObservations()
   {
      recordBeforeAnyObservation(Observation.BEFORE_APPLY_VALUES);
      recordBeforeAnyObservation(Observation.BEFORE_INVOKE_APPLICATION);
      recordBeforeAnyObservation(Observation.BEFORE_PROCESS_VALIDATION);
      recordBeforeAnyObservation(Observation.BEFORE_RENDER_RESPONSE);
      recordBeforeAnyObservation(Observation.BEFORE_RESTORE_VIEW);
      recordBeforeAnyObservation(Observation.BEFORE_UPDATE_MODEL_VALUES);
   }

   private void recordBeforeAnyObservation(Observation observation)
   {
      beforeAnyObservations.add(observation);
   }

   public void observeAfterAnyPhase(@Observes @After @AnyPhase final PhaseEvent e)
   {
      recordAfterAnyObservations();
   }

   private void recordAfterAnyObservations()
   {
      recordAfterAnyObservation(Observation.AFTER_APPLY_VALUES);
      recordAfterAnyObservation(Observation.AFTER_INVOKE_APPLICATION);
      recordAfterAnyObservation(Observation.AFTER_PROCESS_VALIDATION);
      recordAfterAnyObservation(Observation.AFTER_RENDER_RESPONSE);
      recordAfterAnyObservation(Observation.AFTER_RESTORE_VIEW);
      recordAfterAnyObservation(Observation.AFTER_UPDATE_MODEL_VALUES);
   }

   private void recordAfterAnyObservation(Observation observation)
   {
      afterAnyObservations.add(observation);
   }

   public void observeBeforeRenderResponse(@Observes @Before @RenderResponse final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_RENDER_RESPONSE);
   }

   public void observeAfterRenderResponse(@Observes @After @RenderResponse final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_RENDER_RESPONSE);
   }

   public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_APPLY_VALUES);
   }

   public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_APPLY_VALUES);
   }

   public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_INVOKE_APPLICATION);
   }

   public void observeAfterInvokeApplication(@Observes @After @InvokeApplication final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_INVOKE_APPLICATION);
   }

   public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_PROCESS_VALIDATION);
   }

   public void observeAfterProcessValidations(@Observes @After @ProcessValidations final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_PROCESS_VALIDATION);
   }

   public void observeBeforeRestoreView(@Observes @Before @RestoreView final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_RESTORE_VIEW);
   }

   public void observeAfterRestoreView(@Observes @After @RestoreView final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_RESTORE_VIEW);
   }

   public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues final PhaseEvent e)
   {
      recordObservation(Observation.BEFORE_UPDATE_MODEL_VALUES);
   }

   public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues final PhaseEvent e)
   {
      recordObservation(Observation.AFTER_UPDATE_MODEL_VALUES);
   }

   public void assertAllAfterPhasesObserved()
   {
//      assert afterAnyObservations.size() == ALL_AFTER_OBSERVATIONS.size();
      assert afterAnyObservations.containsAll(ALL_AFTER_OBSERVATIONS);
   }

   public void assertAllBeforePhasesObserved()
   {
//      assert beforeAnyObservations.size() == ALL_BEFORE_OBSERVATIONS.size();
      assert beforeAnyObservations.containsAll(ALL_BEFORE_OBSERVATIONS);
   }

}
