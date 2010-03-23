package org.jboss.seam.faces.event;

import static org.junit.Assert.assertEquals;

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
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(PhaseEventObserver.class, PhaseEventBridge.class, BeanManagerAware.class, MockLogger.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   PhaseEventBridge phaseEventBridge;

   private final MockFacesContext facesContext = new MockFacesContext();
   private final MockLifecycle lifecycle = new MockLifecycle();

   @Test
   public void testBeforeAnyPhaseObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assertEquals(3, PhaseEventObserver.beforeAnyPhaseCount);
   }

   @Test
   public void testAfterAnyPhaseObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assertEquals(3, PhaseEventObserver.afterAnyPhaseCount);
   }

   @Test
   public void testBeforeRenderResponseObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      assert PhaseEventObserver.observeBeforeRenderResponse;
   }

   @Test
   public void testAfterRenderResponseObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      assert PhaseEventObserver.observeAfterRenderResponse;
   }

   @Test
   public void testBeforeApplyRequestValuesObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert PhaseEventObserver.observeBeforeApplyRequestValues;
   }

   @Test
   public void testAfterApplyRequestValuesObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert PhaseEventObserver.observeAfterApplyRequestValues;
   }

   @Test
   public void testBeforeInvokeApplicationObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle));
      assert PhaseEventObserver.observeBeforeInvokeApplication;
   }

   @Test
   public void testAfterInvokeApplicationObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle));
      assert PhaseEventObserver.observeAfterInvokeApplication;
   }

   @Test
   public void testBeforeProcessValidationsObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert PhaseEventObserver.observeBeforeProcessValidations;
   }

   @Test
   public void testAfterProcessValidationsObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert PhaseEventObserver.observeAfterProcessValidations;
   }

   @Test
   public void testBeforeRestoreViewObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, lifecycle));
      assert PhaseEventObserver.observeBeforeRestoreView;
   }

   @Test
   public void testAfterRestoreViewObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, lifecycle));
      assert PhaseEventObserver.observeAfterRestoreView;
   }

   @Test
   public void testBeforeUpdateModelValuesObserver()
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert PhaseEventObserver.observeBeforeUpdateModelValues;
   }

   @Test
   public void testAfterUpdateModelValuesObserver()
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert PhaseEventObserver.observeAfterUpdateModelValues;
   }

}
