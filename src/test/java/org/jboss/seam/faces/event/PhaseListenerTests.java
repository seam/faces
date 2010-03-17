package org.jboss.seam.faces.event;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.InvokeApplication;
import org.jboss.seam.faces.event.qualifier.ProcessValidations;
import org.jboss.seam.faces.event.qualifier.RenderResponse;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.faces.event.qualifier.UpdateModelValues;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;
import org.testng.annotations.Test;

@Test
public class PhaseListenerTests extends Arquillian
{

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class)
         .addClasses(Observer.class, PhaseEventListener.class, GenericEventListener.class)
         .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject PhaseEventListener phaseEventListener;

   MockFacesEnvironment environment = MockFacesEnvironment.createEnvironment();
   MockLifecycle lifecycle = new MockLifecycle();
   
   @Test
   public void testBeforeRenderResponse()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RENDER_RESPONSE, lifecycle));
      assert Observer.observeBeforeRenderResponse;
   }
   
   @Test
   public void testAfterRenderResponse()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RENDER_RESPONSE, lifecycle));
      assert Observer.observeAfterRenderResponse;
   }
   
   @Test
   public void testBeforeApplyRequestValues()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert Observer.observeBeforeApplyRequestValues;
   }
   
   @Test
   public void testAfterApplyRequestValues()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.APPLY_REQUEST_VALUES, lifecycle));
      assert Observer.observeAfterApplyRequestValues;
   }   

   @Test
   public void testBeforeInvokeApplication()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.INVOKE_APPLICATION, lifecycle));
      assert Observer.observeBeforeInvokeApplication;
   }
   
   @Test
   public void testAfterInvokeApplication()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.INVOKE_APPLICATION, lifecycle));
      assert Observer.observeAfterInvokeApplication;
   }   

   @Test
   public void testBeforeProcessValidations()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert Observer.observeBeforeProcessValidations;
   }
   
   @Test
   public void testAfterProcessValidations()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.PROCESS_VALIDATIONS, lifecycle));
      assert Observer.observeAfterProcessValidations;
   }   

   @Test
   public void testBeforeRestoreView()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RESTORE_VIEW, lifecycle));
      assert Observer.observeBeforeRestoreView;
   }
   
   @Test
   public void testAfterRestoreView()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RESTORE_VIEW, lifecycle));
      assert Observer.observeAfterRestoreView;
   }   

   @Test
   public void testBeforeUpdateModelValues()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert Observer.observeBeforeUpdateModelValues;
   }
   
   @Test
   public void testAfterUpdateModelValues()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.UPDATE_MODEL_VALUES, lifecycle));
      assert Observer.observeAfterUpdateModelValues;
   }   

}
