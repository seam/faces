package org.jboss.seam.faces.event;

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
   public static boolean observeBeforeRenderResponse;
   public static boolean observeAfterRenderResponse;
   public static boolean observeBeforeApplyRequestValues;
   public static boolean observeAfterApplyRequestValues;
   public static boolean observeBeforeInvokeApplication;
   public static boolean observeAfterInvokeApplication;
   public static boolean observeBeforeProcessValidations;
   public static boolean observeAfterProcessValidations;
   public static boolean observeBeforeRestoreView;
   public static boolean observeAfterRestoreView;
   public static boolean observeBeforeUpdateModelValues;
   public static boolean observeAfterUpdateModelValues;
   public static int beforeAnyPhaseCount;
   public static int afterAnyPhaseCount;

   public void observeBeforeAnyPhase(@Observes @Before @AnyPhase final PhaseEvent e)
   {
      beforeAnyPhaseCount++;
   }

   public void observeAfterAnyPhase(@Observes @After @AnyPhase final PhaseEvent e)
   {
      afterAnyPhaseCount++;
   }

   public void observeBeforeRenderResponse(@Observes @Before @RenderResponse final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeRenderResponse = true;
   }

   public void observeAfterRenderResponse(@Observes @After @RenderResponse final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterRenderResponse = true;
   }

   public void observeBeforeApplyRequestValues(@Observes @Before @ApplyRequestValues final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeApplyRequestValues = true;
   }

   public void observeAfterApplyRequestValues(@Observes @After @ApplyRequestValues final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterApplyRequestValues = true;
   }

   public void observeBeforeInvokeApplication(@Observes @Before @InvokeApplication final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeInvokeApplication = true;
   }

   public void observeAfterInvokeApplication(@Observes @After @InvokeApplication final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterInvokeApplication = true;
   }

   public void observeBeforeProcessValidations(@Observes @Before @ProcessValidations final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeProcessValidations = true;
   }

   public void observeAfterProcessValidations(@Observes @After @ProcessValidations final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterProcessValidations = true;
   }

   public void observeBeforeRestoreView(@Observes @Before @RestoreView final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeRestoreView = true;
   }

   public void observeAfterRestoreView(@Observes @After @RestoreView final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterRestoreView = true;
   }

   public void observeBeforeUpdateModelValues(@Observes @Before @UpdateModelValues final PhaseEvent e)
   {
      PhaseEventObserver.observeBeforeUpdateModelValues = true;
   }

   public void observeAfterUpdateModelValues(@Observes @After @UpdateModelValues final PhaseEvent e)
   {
      PhaseEventObserver.observeAfterUpdateModelValues = true;
   }

}
