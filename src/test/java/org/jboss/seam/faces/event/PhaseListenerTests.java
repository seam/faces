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
   @Inject Observer observer;

   MockFacesEnvironment environment = MockFacesEnvironment.createEnvironment();
   MockLifecycle lifecycle = new MockLifecycle();
   
   @Test
   public void testBeforeRenderResponse()
   {
      phaseEventListener.beforePhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RENDER_RESPONSE, lifecycle));
      assert observer.observeBeforeRenderResponse;
   }
   
   @Test
   public void testAfterRenderResponse()
   {
      phaseEventListener.afterPhase(new PhaseEvent(environment.getFacesContext(), PhaseId.RENDER_RESPONSE, lifecycle));
      assert observer.observeAfterRenderResponse;
   }
   
}
