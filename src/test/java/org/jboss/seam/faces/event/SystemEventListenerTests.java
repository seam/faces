package org.jboss.seam.faces.event;

import java.util.HashMap;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.application.MockApplication;
import org.jboss.test.faces.mock.component.MockUIComponent;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.testng.annotations.Test;

@Test
public class SystemEventListenerTests extends Arquillian
{

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class)
         .addClasses(SystemEventObserver.class, SystemEventListener.class, GenericEventListener.class)
         .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }
   
   @Inject SystemEventListener systemEventListener;

   private MockFacesContext facesContext = new MockFacesContext();
   private MockApplication application = new MockApplication();
   
   @SuppressWarnings("serial")
   @Test
   public void testComponentSystemEventObserver()
   {
      systemEventListener.processEvent(new ComponentSystemEvent(new MockUIComponent()){});
      assert SystemEventObserver.componentSystemEvent;
   }   
   
   @Test
   public void testExceptionQueuedEventObserver()
   {
      ExceptionQueuedEventContext eqec = new ExceptionQueuedEventContext(facesContext, new NullPointerException());
      ExceptionQueuedEvent eqe = new ExceptionQueuedEvent(eqec);
      systemEventListener.processEvent(eqe);
      assert SystemEventObserver.excecptionQueuedEvent;
   }   
   
   @Test
   public void testPostConstructApplicationEventObserver()
   {
      systemEventListener.processEvent(new PostConstructApplicationEvent(application));
      assert SystemEventObserver.postConstructApplicationEvent;
   }   
   
   @Test
   public void testPostConstructCustomScopeEvent()
   {
      ScopeContext sc = new ScopeContext("dummyscope", new HashMap<String, Object>());
      systemEventListener.processEvent(new PostConstructCustomScopeEvent(sc));
      assert SystemEventObserver.postConstructCustomScopeEvent;
   }
   
   @Test
   public void testPreDestroyApplicationEventObserver()
   {
      systemEventListener.processEvent(new PreDestroyApplicationEvent(application));
      assert SystemEventObserver.preDestroyApplicationEvent;
   }    
   
   @Test
   public void testPreDestroyCustomScopeEventObserver()
   {
      ScopeContext sc = new ScopeContext("dummyscope", new HashMap<String, Object>());
      systemEventListener.processEvent(new PreDestroyCustomScopeEvent(sc));
      assert SystemEventObserver.preDestroyCustomScopeEvent;
   }        
   
}
