package org.jboss.seam.faces.event;

import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_APPLY_VALUES;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_INVOKE_APPLICATION;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_PROCESS_VALIDATION;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_RENDER_RESPONSE;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_RESTORE_VIEW;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.AFTER_UPDATE_MODEL_VALUES;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_APPLY_VALUES;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_INVOKE_APPLICATION;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_PROCESS_VALIDATION;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_RENDER_RESPONSE;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_RESTORE_VIEW;
import static org.jboss.seam.faces.event.PhaseEventObserver.Observation.BEFORE_UPDATE_MODEL_VALUES;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.MockLogger;
import org.jboss.seam.faces.cdi.BeanManagerAware;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PhaseEventBridgeTest
{
   private static List<PhaseId> ALL_PHASES = new ArrayList<PhaseId>()
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

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(PhaseEventObserver.class, PhaseEventBridge.class, BeanManagerAware.class, MockLogger.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   PhaseEventBridge phaseEventBridge;
   @Inject
   PhaseEventObserver observer;

   private final MockFacesContext facesContext = new MockFacesContext();
   private final MockLifecycle lifecycle = new MockLifecycle();

   @Test
   public void testBeforeAnyPhaseObserver()
   {
      observer.reset();
      fireAllBeforePhases();
      observer.assertAllBeforePhasesObserved();
   }

   private void fireAllBeforePhases()
   {
      fireBeforePhases(ALL_PHASES);
   }

   private void fireBeforePhases(List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireBeforePhase(phaseId);
      }
   }

   private void fireBeforePhase(PhaseId phaseId)
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }

   @Test
   public void testAfterAnyPhaseObserver()
   {
      observer.reset();
      fireAllAfterPhases();
      observer.assertAllAfterPhasesObserved();
   }

   private void fireAllAfterPhases()
   {
      fireAfterPhases(ALL_PHASES);
   }

   private void fireAfterPhases(List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireAfterPhase(phaseId);
      }
   }

   private void fireAfterPhase(PhaseId phaseId)
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }

   @Test
   public void testBeforeRenderResponseObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.RENDER_RESPONSE);
      observer.assertSingleObservation(BEFORE_RENDER_RESPONSE);
   }

   @Test
   public void testAfterRenderResponseObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.RENDER_RESPONSE);
      observer.assertSingleObservation(AFTER_RENDER_RESPONSE);
   }

   @Test
   public void testBeforeApplyRequestValuesObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.APPLY_REQUEST_VALUES);
      observer.assertSingleObservation(BEFORE_APPLY_VALUES);
   }

   @Test
   public void testAfterApplyRequestValuesObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.APPLY_REQUEST_VALUES);
      observer.assertSingleObservation(AFTER_APPLY_VALUES);
   }

   @Test
   public void testBeforeInvokeApplicationObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.INVOKE_APPLICATION);
      observer.assertSingleObservation(BEFORE_INVOKE_APPLICATION);
   }

   @Test
   public void testAfterInvokeApplicationObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.INVOKE_APPLICATION);
      observer.assertSingleObservation(AFTER_INVOKE_APPLICATION);
   }

   @Test
   public void testBeforeProcessValidationsObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.PROCESS_VALIDATIONS);
      observer.assertSingleObservation(BEFORE_PROCESS_VALIDATION);
   }

   @Test
   public void testAfterProcessValidationsObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.PROCESS_VALIDATIONS);
      observer.assertSingleObservation(AFTER_PROCESS_VALIDATION);
   }

   @Test
   public void testBeforeRestoreViewObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.RESTORE_VIEW);
      observer.assertSingleObservation(BEFORE_RESTORE_VIEW);
   }

   @Test
   public void testAfterRestoreViewObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.RESTORE_VIEW);
      observer.assertSingleObservation(AFTER_RESTORE_VIEW);
   }

   @Test
   public void testBeforeUpdateModelValuesObserver()
   {
      observer.reset();
      fireBeforePhase(PhaseId.UPDATE_MODEL_VALUES);
      observer.assertSingleObservation(BEFORE_UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAfterUpdateModelValuesObserver()
   {
      observer.reset();
      fireAfterPhase(PhaseId.UPDATE_MODEL_VALUES);
      observer.assertSingleObservation(AFTER_UPDATE_MODEL_VALUES);
   }

}
