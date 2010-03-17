package org.jboss.seam.faces.event;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;
import org.testng.annotations.Test;

@Test
public class PhaseListenerTests extends Arquillian
{

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class)
         .addClasses(PhaseListenerObserver.class, PhaseEventListener.class, GenericEventListener.class)
         .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject PhaseEventListener phaseEventListener;

   private MockFacesContext facesContext = new MockFacesContext();
   private MockLifecycle lifecycle = new MockLifecycle();
   
   @Test
   public void testBeforeRenderResponseObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      assert PhaseListenerObserver.observeBeforeRenderResponse;
   }
   
   @Test
   public void testAfterRenderResponseObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle));
      assert PhaseListenerObserver.observeAfterRenderResponse;
   }
   
   @Test
   public void testBeforeApplyRequestValuesObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert PhaseListenerObserver.observeBeforeApplyRequestValues;
   }
   
   @Test
   public void testAfterApplyRequestValuesObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert PhaseListenerObserver.observeAfterApplyRequestValues;
   }   

   @Test
   public void testBeforeInvokeApplicationObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle));
      assert PhaseListenerObserver.observeBeforeInvokeApplication;
   }
   
   @Test
   public void testAfterInvokeApplicationObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle));
      assert PhaseListenerObserver.observeAfterInvokeApplication;
   }   

   @Test
   public void testBeforeProcessValidationsObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert PhaseListenerObserver.observeBeforeProcessValidations;
   }
   
   @Test
   public void testAfterProcessValidationsObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert PhaseListenerObserver.observeAfterProcessValidations;
   }   

   @Test
   public void testBeforeRestoreViewObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, lifecycle));
      assert PhaseListenerObserver.observeBeforeRestoreView;
   }
   
   @Test
   public void testAfterRestoreViewObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, lifecycle));
      assert PhaseListenerObserver.observeAfterRestoreView;
   }   

   @Test
   public void testBeforeUpdateModelValuesObserver()
   {
      phaseEventListener.beforePhase(new PhaseEvent(facesContext, PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert PhaseListenerObserver.observeBeforeUpdateModelValues;
   }
   
   @Test
   public void testAfterUpdateModelValuesObserver()
   {
      phaseEventListener.afterPhase(new PhaseEvent(facesContext, PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert PhaseListenerObserver.observeAfterUpdateModelValues;
   }   

}
